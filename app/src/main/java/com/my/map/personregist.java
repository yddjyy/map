package com.my.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class personregist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personregist);
        final EditText resetpwd_edit_name=(EditText) findViewById(R.id.resetpwd_edit_name);
        final EditText resetpwd_edit_age=(EditText) findViewById(R.id.resetpwd_edit_age);
        final EditText resetpwd_edit_sex=(EditText) findViewById(R.id.resetpwd_edit_sex);
        final EditText resetpwd_edit_pwd_old=(EditText) findViewById(R.id.resetpwd_edit_pwd_old);
        final EditText resetpwd_edit_pwd_new=(EditText) findViewById(R.id.resetpwd_edit_pwd_new);
        final TextView resetpwd_edit_show=(TextView) findViewById(R.id.resetpwd_edit_show);
        Button register_btn_cancel=(Button)findViewById(R.id.register_btn_cancel);
        Button register_btn_sure=(Button) findViewById(R.id.register_btn_sure);
        register_btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  resetpwd_edit_name=((EditText) findViewById(R.id.resetpwd_edit_name)).getText().toString();
                String  resetpwd_edit_age=((EditText) findViewById(R.id.resetpwd_edit_age)).getText().toString();
                String  resetpwd_edit_sex=((EditText) findViewById(R.id.resetpwd_edit_sex)).getText().toString();
                String  resetpwd_edit_pwd_old=((EditText) findViewById(R.id.resetpwd_edit_pwd_old)).getText().toString();
                String  resetpwd_edit_pwd_new=((EditText) findViewById(R.id.resetpwd_edit_pwd_new)).getText().toString();
                String sex="";
                if(resetpwd_edit_sex.equals("女")){
                    sex="f";
                }
                else {
                    sex="m";
                }
                if(resetpwd_edit_pwd_old.equals(resetpwd_edit_pwd_new)){
                  if(resetpwd_edit_name!=""){
                      System.out.println("这是注册的"+resetpwd_edit_name);
                      String url = "http://192.168.1.109:8080/Android/Userregist?username="+resetpwd_edit_name+"&password="+resetpwd_edit_pwd_old+"&age="+resetpwd_edit_age+"&sex="+sex;
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
                                          Toast.makeText(personregist.this, "注册成功", Toast.LENGTH_SHORT).show();
                                          resetpwd_edit_show.setText("注册成功");
                                      }catch (Exception e){
                                          System.out.println(e);
                                      }
                                  }
                              });
                          }
                      });
                  }
                  else {
                      resetpwd_edit_show.setText("请填写用户名");
                  }
                }
                else {
                     resetpwd_edit_show.setText("两次密码输入不一致");
                }
            }
        });
       register_btn_cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(personregist.this,login.class);
               startActivity(intent);
           }
       });
    }
}
