package com.my.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.my.map.constant.Information;
import com.alibaba.fastjson.JSONObject;
import com.my.map.utils.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        String username = ((EditText) findViewById(R.id.username)).getText().toString();
                        String password = ((EditText) findViewById(R.id.password)).getText().toString();
                        if(username.trim().equals("")||password.trim().equals(""))
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(login.this, "请输入正确的用户名和密码", Toast.LENGTH_SHORT).show();
                                }
                            });
                        if (UserService.signIn(username, password)) {
                            String url = "http://192.168.1.109:8080/Android/UserSelect?username=" + username + "&password=" + password;//TODO
                            OkHttpClient okHttpClient = new OkHttpClient();
                            final Request request = new Request.Builder()
                                    .url(url)
                                    .get()//默认就是GET请求，可以不写
                                    .build();
                            Call call = okHttpClient.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(login.this, "登录失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                @Override
                                public void onResponse(Call call, final Response response) throws IOException {
                                    JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                                    Information.setId(jsonObject.getString("id"));
                                    Information.setUsername(jsonObject.getString("username"));
                                    Information.setPassworrd(jsonObject.getString("password"));
                                    Information.setAge(jsonObject.getString("age"));
                                    Information.setSex(jsonObject.getString("sex"));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Intent intent = new Intent(login.this, navigation01.class);
                                                startActivity(intent);
                                                finish();//销毁activity
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
                        super.run();
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
