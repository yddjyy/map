package com.my.map.historys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.SortType;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TransportMode;
import com.my.map.R;
import com.my.map.constant.Information;
import com.my.map.utils.TraceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
public class showHistoryActivity extends AppCompatActivity {
    private BaiduMap mBaiduMap=null;
    private MapView mMapView=null;
    /**218663
     * 轨迹服务ID 218668
     */
    public long serviceId = 218743;
    /**
     * 轨迹客户端
     */
    public LBSTraceClient mTraceClient = null;
    /**
     * Entity标识
     */
    public String entityName = Information.getId();
    private LatLng latLng=null;
    private List<LatLng> trackPoints = new ArrayList<>();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        mMapView=findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        // 初始化轨迹服务客户端
        mTraceClient = new LBSTraceClient(getApplicationContext());
        Intent intent =getIntent();
        String jsonString = intent.getStringExtra("data");
        JSONObject jsonObject=null;
        jsonObject = JSONObject.parseObject(jsonString);
        Toast.makeText(this, "startTime : "+jsonObject.getString("startTime")+",endTime : "+jsonObject.getString("endTime") , Toast.LENGTH_SHORT).show();
        requestLocation(jsonObject.getString("startTime"),jsonObject.getString("endTime"));
    }
    //实现手动定位
    public void ReLocation( )
    {
        Log.e(TAG, "手动定位: " );
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(mapStatusUpdate);//以动画的方式跟新地图
    }
    //查询历史记录绘画路径
    public void requestLocation(String startDate,String endDate)
    {
        // 请求标识
        int tag = 1;
        HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest(tag, serviceId, entityName);
        try {
            // 设置开始时间
            historyTrackRequest.setStartTime(df.parse(startDate).getTime()/1000);
            // 设置结束时间
            historyTrackRequest.setEndTime(df.parse(endDate).getTime()/1000);
        }catch (Exception e ){

        }

// 设置需要纠偏
        historyTrackRequest.setProcessed(true);
// 创建纠偏选项实例
        ProcessOption processOption = new ProcessOption();
// 设置需要去噪
        processOption.setNeedDenoise(true);
// 设置需要抽稀
        processOption.setNeedVacuate(true);
// 设置需要绑路
        processOption.setNeedMapMatch(true);
// 设置精度过滤值(定位精度大于100米的过滤掉)
        processOption.setRadiusThreshold(100);
// 设置交通方式为步行
        processOption.setTransportMode(TransportMode.walking);
// 设置纠偏选项
        historyTrackRequest.setProcessOption(processOption);
// 设置里程填充方式为步行
        historyTrackRequest.setSupplementMode(SupplementMode.walking);
// 初始化轨迹监听器
        OnTrackListener mTrackListener = new OnTrackListener() {
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                // 历史轨迹回调
                trackPoints=new ArrayList<>();
                Log.e(TAG, "---历史轨迹回调--response:"+response.getTotal() );
                int total = response.getTotal();
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    Toast.makeText(showHistoryActivity.this, "结果为：" + response.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (0 == total) {
                    Toast.makeText(showHistoryActivity.this, "未查询到历史轨迹", Toast.LENGTH_SHORT).show();
                } else {
                    latLng=new LatLng(response.getStartPoint().getLocation().getLatitude(),response.getStartPoint().getLocation().getLongitude());
                    System.out.println("原始数据start");
                    for(TrackPoint point :response.getTrackPoints()){
                        System.out.println(point.getLocation().getLatitude()+","+point.getLocation().getLongitude());
                    }
                    System.out.println("原始数据end");
                    List<TrackPoint> points = response.getTrackPoints();
                    if (null != points) {
                        for (TrackPoint trackPoint : points) {
                            if (!TraceUtil.isZeroPoint(trackPoint.getLocation().getLatitude(),
                                    trackPoint.getLocation().getLongitude())) {
                                trackPoints.add(TraceUtil.convertTrace2Map(trackPoint.getLocation()));
                            }
                        }
                    }
                }
                TraceUtil traceUtil=new TraceUtil();
                traceUtil.drawHistoryTrack(mBaiduMap,trackPoints, SortType.asc);
                ReLocation();
            }

        };
// 查询轨迹
        mTraceClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
    }
}
