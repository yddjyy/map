package com.my.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class person extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        TextView textView01 =(TextView) findViewById(R.id.per_message);
        TextView textView02= (TextView) findViewById(R.id.per_history);
        textView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName componentName=new ComponentName(person.this,personmessage.class);
                Intent intent=new Intent();
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });
        textView02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName componentName=new ComponentName(person.this,history.class);
                Intent intent=new Intent();
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });
    }
}
