package com.my.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.my.map.historys.showHistoryActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class history extends AppCompatActivity {
    private ListView myList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        myList =  findViewById(R.id.mylist);
        Intent  intent =getIntent();
        String jsonString = intent.getStringExtra("data");
        JSONArray jsonArray=null;
        try {
             jsonArray = new JSONArray(jsonString);
        List<Map<String,Object>> maplist=new ArrayList<Map<String,Object>>();
        for(int i=0;i<jsonArray.length();i++){
           JSONObject jsonObject = jsonArray.getJSONObject(i);
            Map<String,Object> listItem=new HashMap<String, Object>();
            listItem.put("start_time",jsonObject.optString("starttime",null));
            listItem.put("kilometer",jsonObject.optString("mileage")+"公里");
            listItem.put("end_time",jsonObject.optString("endtime"));
            maplist.add(listItem);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,maplist,R.layout.simple_layout,
                new String[]{"start_time","kilometer","end_time"},
                new int[]{R.id.start_time,R.id.kilometer,R.id.end_time});
        ListView listView =(ListView) findViewById(R.id.mylist);
        listView.setAdapter(simpleAdapter);
        }catch (Exception  e){

        }
        //为listView中的item设置点击事件
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               TextView startTime  = view.findViewById(R.id.start_time);
                TextView endTime  = view.findViewById(R.id.end_time);
                com.alibaba.fastjson.JSONObject jsonObject= new com.alibaba.fastjson.JSONObject();
                jsonObject.put("startTime",startTime.getText());
                jsonObject.put("endTime",endTime.getText());
                Intent intent = new Intent(history.this, showHistoryActivity.class);
                intent.putExtra("data", jsonObject.toJSONString());
                startActivity(intent);
            }
        });
    }
}
