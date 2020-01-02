package com.my.map.ui.notifications;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSONObject;
import com.my.map.constant.Information;
import com.my.map.R;
import com.my.map.history;
import com.my.map.person;
import com.my.map.personmessage;
import com.my.map.personupdata;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        TextView textView01 =(TextView) root.findViewById(R.id.per_message);//切记用root不能用getActivity
        TextView textView02= (TextView) root.findViewById(R.id.per_history);
        TextView textView03=(TextView) root.findViewById(R.id.per_revise);
        final TextView textViewusername=(TextView) root.findViewById(R.id.username);
        final TextView textViewid=(TextView)root.findViewById(R.id.pass_id) ;
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textViewusername.setText(Information.getUsername());
                textViewid.setText(Information.getId());
            }
        });
        textView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),personmessage.class);
                startActivity(i);
            }
        });
        textView02.setOnClickListener(new View.OnClickListener() {
             // JSONObject jsonObject;
            @Override
            public void onClick(View v) {
                String url = "http://192.168.1.109:8080/Android/LocationSelectId";
                RequestBody requestBody = new FormBody.Builder()
                        .add("userid", Information.getId())
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                final Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)//默认就是GET请求，可以不写
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("失败");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        Log.e(TAG,"成功：" );//resonse.body只能调用一次
//               JSONObject jsonObject = JSONObject.parseObject(response.body().string());
//               Log.e(TAG,"成功：" +jsonObject.getString("username"));
                        Intent i=new Intent(getActivity(),history.class);
                        i.putExtra("data",response.body().string());
                        startActivity(i);
                        //JSONObject jsonObject = JSONObject.parseObject(response.body().string());
//                        getActivity().runOnUiThread(new Runnable(){
//                            @Override
//                            public void run() {
//                                try {
//                                    JSONObject jsonObject = JSONObject.parseObject(response.body().string());
//                                }catch (Exception e){
//                                    System.out.println(e);
//                                }
//                            }
//                        });
//                         jsonObject = JSONObject.parseObject(response.body().string());
                    }
                });
            }
        });
       textView03.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(getActivity(),personupdata.class);
               startActivity(intent);
           }
       });
        return root;
    }
}