package com.my.map.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    private static String result;
    private  static String ip="192.168.1.109";

    public static  String httpService(Map<String,String> map,String uri)
    {
        String url = "http://"+ip+":8080/Android/"+uri;

        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String, String> vo : map.entrySet()){
            vo.getKey();
            vo.getValue();
            System.out.println(vo.getKey()+"  "+vo.getValue());
            builder.add(vo.getKey(),vo.getValue());
        }
        RequestBody requestBody = builder.build();

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               result=null;
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                result=response.body().string();
            }
        });
        return null;
    }
}
