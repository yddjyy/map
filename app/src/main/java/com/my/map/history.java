package com.my.map;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class history extends AppCompatActivity {
    private ListView myList;
    private String[] times=new String[]{"11月1日","10月2日","9月3日","8月4日"};
    private String[] kilometer=new String[]{"5公里","6公里","7公里","8公里","9公里"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        List<Map<String,Object>> maplist=new ArrayList<Map<String,Object>>();
        for(int i=0;i<times.length;i++){
            Map<String,Object> listItem=new HashMap<String, Object>();
            listItem.put("times",times[i]);
            listItem.put("kilometer",kilometer[i]);
            maplist.add(listItem);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,maplist,R.layout.simple_layout,
                new String[]{"times","kilometer"},
                new int[]{R.id.times,R.id.kilometer});
        ListView listView =(ListView) findViewById(R.id.mylist);
        listView.setAdapter(simpleAdapter);
    }
}
