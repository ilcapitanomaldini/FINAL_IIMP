package com.liveproject.ycce.iimp.polling;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.LocalIdGen;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.messaging.groupmessaging.Message;

import java.util.ArrayList;


/**
 * Created by Laptop on 31-01-2017.
 */
public class Activity_CreatePoll extends AppCompatActivity {

    private EditText Title, Number;
    Button create;
    LinearLayout llm;
    Poll poll;
    ArrayList<PollMapping> pollMappings;
    ArrayList<EditText> editTexts;
    String gid;
    int num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        llm = (LinearLayout) findViewById(R.id.ll_cpoll_main);
        Title = (EditText) findViewById(R.id.et_cpoll_title_name);
        Number = (EditText) findViewById(R.id.et_cpoll_num_ans);
        create = (Button) findViewById(R.id.bt_cpoll_create);
        gid = getIntent().getStringExtra("gid");
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check whether the number provided is less than 10.
                num = Integer.parseInt(Number.getText().toString());
                if (num > 4 && num < 2) {
                    Toast.makeText(Activity_CreatePoll.this, "Enter a number that is less than or equal to 10.", Toast.LENGTH_LONG).show();
                } else {
                    LinearLayout ll;
                    TextView textView;
                    EditText editText;
                    Button button;
                    //Hide the button.
                    create.setVisibility(View.GONE);
                    poll = new Poll();
                    final String pid = new LocalIdGen().nextLocalId();
                    pollMappings = new ArrayList<PollMapping>();
                    editTexts = new ArrayList<EditText>();

                    //Code for dynamic generation of EditText boxes where answers can be provided.
                    for (int i = 0; i < num; i++) {
                        ll = new LinearLayout(Activity_CreatePoll.this);
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        textView = new TextView(Activity_CreatePoll.this);
                        textView.setPadding(4, 4, 4, 4);
                        textView.setText("Answer " + (i + 1) + " : ");
                        textView.setTextSize(16);
                        editText = new EditText(Activity_CreatePoll.this);
                        editText.setTextSize(16);
                        editText.setId(i + 1);
                        editText.setTag(Integer.toString(i+1));
                        editTexts.add(editText);
                        ll.addView(textView, 0);
                        ll.addView(editText, 1);
                        llm.addView(ll);
                        PollMapping pollMapping = new PollMapping();
                        pollMapping.setAid(new LocalIdGen().nextLocalId());
                        pollMapping.setPid(pid);
                        //pollMapping.setAnswerTitle(editText.toString());
                        pollMapping.setNumberOfVotes("0");
                        pollMappings.add(pollMapping);
                    }
                    button = new Button(Activity_CreatePoll.this);
                    button.setText("Confirm");
                    button.setTextSize(16);
                    button.setPadding(2, 2, 2, 2);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String[] result = new String[num];
                            float[] resultvalue = new float[num];
                            /*for(int i=0;    i<num;  i++)
                            {
                                EditText et;
                                et= (EditText) findViewById(i+1);
                                String sresult = et.getText().toString();
                                result[i] = sresult;
                                // TO DO :: Remove this later!! Just for testing..
                                resultvalue[i] = 100/num;
                            }
                            Intent i = new Intent(Activity_CreatePoll.this,Activity_PollResults.class);
                            i.putExtra("Title",Title.getText().toString());
                            i.putExtra("Result",result);
                            i.putExtra("ResultValue",resultvalue);
                            startActivity(i);*/
                            int i = 0;
                            for (PollMapping pollMapping :
                                    pollMappings) {
                                //pollMapping.setAnswerTitle(v.findViewById(i++).toString());
                                EditText et = editTexts.get(i++);
                                pollMapping.setAnswerTitle(et.getText().toString());
                            }
                            poll.setPm(pollMappings);
                            poll.setCreatorId(DatabaseService.fetchID());
                            // TODO : Set Duration.
                            poll.setDuration("Add Later");
                            poll.setNumber_answers(num);
                            poll.setTitle(Title.getText().toString());
                            poll.setPid(pid);
                            DatabaseService.insertPoll(poll);
                            Message message = new Message();
                            message.setEventID("null");
                            message.setGid(gid);
                            message.setLocalID(pid);
                            message.setType("poll");
                            message.setPollID(pid);
                            message.setMid("null");
                            message.setSender(DatabaseService.fetchID());
                            message.setMessage(poll.getTitle());
                            DatabaseService.insertMessage(message);
                        }
                    });
                    llm.addView(button);
                }
            }
        });

    }
}
