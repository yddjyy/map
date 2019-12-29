package com.my.map;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button signin = (Button) findViewById(R.id.Login);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                if (UserService.signIn(username, password))
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(login.this, "登录成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(login.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        Button signup = (Button) findViewById(R.id.register);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                if (UserService.signUp(username, password))
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(login.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(login.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
