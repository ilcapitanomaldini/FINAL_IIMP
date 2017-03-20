package com.liveproject.ycce.iimp.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.Validation;
import com.liveproject.ycce.iimp.filemanager.Activity_File;
import com.liveproject.ycce.iimp.networkservice.updateservice.UpdateService;

import java.io.File;

/**
 * Created by Laptop on 23-02-2017.
 */
public class Activity_CreateNews extends AppCompatActivity {


    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private byte[] multipartBody;


    private EditText title, body;
    private Button create, image_selector;
    private ImageView imageView;
    private String imageloc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news);
        final Intent intent = new Intent(this, Activity_File.class);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tv_title.setText("Create News");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("Toolbar", "onCreate: " + e.toString());
        }

        title = (EditText) findViewById(R.id.et_cnews_title_name);
        body = (EditText) findViewById(R.id.et_cnews_message_body);
        create = (Button) findViewById(R.id.bt_cnews_create);
        image_selector = (Button) findViewById(R.id.bt_cnews_image_path);
        imageView = (ImageView) findViewById(R.id.iv_cnews_image_selected);

        image_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Receive the path here and make the imageView visible and set its source.

                startActivityForResult(intent, 1);

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            // make use of "data"

            try {
                final File file = new File(data.getStringExtra("FilePath"));
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageURI(android.net.Uri.parse(file.toURI().toString()));
                imageloc = file.toURI().toString();
            } catch (NullPointerException e) {
                Log.d("CreateNews : ", "onActivityResult: null file");
            }

            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Send the data to the database!
                    News news = new News();
                    news.setNid("null");
                    news.setTitle(title.getText().toString());
                    news.setPoster(DatabaseService.fetchID());
                    news.setMessage(body.getText().toString());
                    news.setImage_loc(imageloc);
                    DatabaseService.insertNewNews(news);
                    if (Validation.isOnline(v.getContext())) {
                        Intent intent1 = new Intent(v.getContext(), UpdateService.class);
                        v.getContext().startService(intent1);
                    }
                    //Trial to send the image file onto the server.
/*

                    byte[] fileData1 = getFileDataFromDrawable(getBaseContext(),R.drawable.iimp);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(bos);
                    try {
                        // the first file
                        buildPart(dos, fileData1, "winner.png");

                        // send multipart form data necesssary after file data
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                        // pass to multipart body
                        multipartBody = bos.toByteArray();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String url = "http://creasy-analyzer.000webhostapp.com/uploads/fileupload.php";
                    MultipartRequestT multipartRequest = new MultipartRequestT(url, null, mimeType, multipartBody, new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Toast.makeText(getBaseContext(), "Upload successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getBaseContext(), "Upload failed!\r\n" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });






                    multipartRequest.setRetryPolicy(new DefaultRetryPolicy(1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                    requestQueue.add(multipartRequest);

*/
                    finish();
                }
            });
        }
    }

    /*   private void buildPart(DataOutputStream dataOutputStream, byte[] fileData, String fileName) throws IOException {
           dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
           dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""
                   + fileName + "\"" + lineEnd);
           dataOutputStream.writeBytes(lineEnd);

           ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
           int bytesAvailable = fileInputStream.available();

           int maxBufferSize = 1024 * 1024;
           int bufferSize = Math.min(bytesAvailable, maxBufferSize);
           byte[] buffer = new byte[bufferSize];

           // read file and write it into form...
           int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

           while (bytesRead > 0) {
               dataOutputStream.write(buffer, 0, bufferSize);
               bytesAvailable = fileInputStream.available();
               bufferSize = Math.min(bytesAvailable, maxBufferSize);
               bytesRead = fileInputStream.read(buffer, 0, bufferSize);
           }

           dataOutputStream.writeBytes(lineEnd);
       }

       public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
           Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
           ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
           bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
           return byteArrayOutputStream.toByteArray();
       }
       private byte[] getFileDataFromDrawable(Context context, int id) {
           Drawable drawable = ContextCompat.getDrawable(context, id);
           Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
           ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
           bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
           return byteArrayOutputStream.toByteArray();
       }
   */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }
}

