package com.my.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.my.map.constant.Information;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button signin = (Button) findViewById(R.id.Login);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        String username = ((EditText) findViewById(R.id.username)).getText().toString();
                        String password = ((EditText) findViewById(R.id.password)).getText().toString();
                        /*UserService.signIn(username, password)
                        if (true)
                        {
                            Information.setUsername("闫帅军");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(login.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(login.this, navigation01.class);
                                    startActivity(intent);
                                }
                            });
                        }*/
                        if (UserService.signIn(username, password)) {
                            String url = "http://192.168.1.109:8080/Android/UserSelect?username=" + username + "&password=" + password;
                            OkHttpClient okHttpClient = new OkHttpClient();
                            final Request request = new Request.Builder()
                                    .url(url)
                                    .get()//默认就是GET请求，可以不写
                                    .build();
                            Call call = okHttpClient.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, final Response response) throws IOException {
                                    // Log.e(TAG,"成功：" );//resonse.body只能调用一次
               /* JSONObject jsonObject = JSONObject.parseObject(response.body().string());
               Log.e(TAG,"成功：" +jsonObject.getString("username"));*/
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                                                System.out.println("--------------" + jsonObject.getString("username"));
                                                Information.setId(jsonObject.getString("id"));
                                                Information.setUsername(jsonObject.getString("username"));
                                                Information.setPassworrd(jsonObject.getString("password"));
                                                Information.setAge(jsonObject.getString("age"));
                                                Information.setSex(jsonObject.getString("sex"));
                                                System.out.println("1111--------------1111" + Information.getUsername());
                                                Intent intent = new Intent(login.this, navigation01.class);
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                System.out.println(e);
                                            }
                                        }
                                    });
                                }
                            });

                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(login.this, "登录失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }.start();

            }
        });
        Button signup = (Button) findViewById(R.id.register);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                System.out.println(username + "," + password + "-----------------------------");
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
