package com.liveproject.ycce.iimp.filemanager;

import android.Manifest;
import android.app.ListFragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.liveproject.ycce.iimp.R;


/**
 * Created by Laptop on 15-02-2017.
 */
public class Activity_File extends FragmentActivity {
    private ListFragment mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getActionBar().setDisplayHomeAsUpEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2);
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //
            }
        }, 100000);


        setContentView(R.layout.activity_file);

        mView = (ListFragment) getFragmentManager().findFragmentById(R.id.file_list);



    }
}
