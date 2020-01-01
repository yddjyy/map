package com.my.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.my.map.constant.Information;

public class personmessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personmessage);
        TextView per_username=(TextView)findViewById(R.id.per_username);
        TextView per_zhanghao=(TextView)findViewById(R.id.per_zhanghao);
        TextView per_userage=(TextView)findViewById(R.id.per_userage);
        TextView per_usersex=(TextView)findViewById(R.id.per_usersex);
        Button btn_exit=(Button)findViewById(R.id.btn_exit);
        per_username.setText(Information.getUsername());
        per_zhanghao.setText(Information.getId());
        per_userage.setText(Information.getAge());
        if(Information.getSex().equals("f")){
            per_usersex.setText("女");
        }else {
            per_usersex.setText("男");
        }
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 onBackPressed();
            }
        });
    }
}
