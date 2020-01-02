package com.my.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.my.map.constant.Information;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class personupdata extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personupdata);
        EditText perup_username=((EditText)findViewById(R.id.perup_username));
        EditText perup_password=((EditText)findViewById(R.id.perup_password));
        EditText perup_id=((EditText)findViewById(R.id.perup_zhanghao));
        EditText perup_sex=((EditText)findViewById(R.id.perup_usersex));
        EditText perup_age=((EditText)findViewById(R.id.perup_userage));
        perup_id.setText(Information.getId());
        perup_username.setText(Information.getUsername());
        perup_password.setText(Information.getPassworrd());
        perup_sex.setText(Information.getSex());
        perup_age.setText(Information.getAge());
        Button submit=(Button)findViewById(R.id.btn_submit);
        Button back=(Button)findViewById(R.id.btn_back);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String perup_username=((EditText)findViewById(R.id.perup_username)).getText().toString();
                String perup_password=((EditText)findViewById(R.id.perup_password)).getText().toString();
                String perup_id=((EditText)findViewById(R.id.perup_zhanghao)).getText().toString();
                String perup_sex=((EditText)findViewById(R.id.perup_usersex)).getText().toString();
                String perup_age=((EditText)findViewById(R.id.perup_userage)).getText().toString();
                if(perup_username!=""&&perup_id!=""&&perup_sex!=""&&perup_age!=""){
                    String url = "http://192.168.1.109:8080/Android/UserRevise?username="+perup_username+"&password="+perup_password+"&id="+perup_id+"&age="+perup_age+"&sex="+perup_sex;
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
                                        Toast.makeText(personupdata.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    }catch (Exception e){
                                        System.out.println(e);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(personupdata.this,login.class);
                startActivity(intent);
                finish();//销毁
            }
        });
    }
}
