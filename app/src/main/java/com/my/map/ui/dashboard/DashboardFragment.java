package com.my.map.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.my.map.R;

import java.util.Date;

import static android.content.ContentValues.TAG;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private BaiduMap mBaiduMap=null;
    private MapView mMapView=null;
    public LocationClient mLocationClient = null;
    private LatLng latLng;
    private TextView textView=null;
    private View root=null;
    private   float zoom=19;
    private ImageButton reloaction;
    private Button startStackBtn;//开始和结束轨迹记录
    private static Trace mTrace = null;//轨迹服务
    public long  serviceId = 218663;//服务id
    public LBSTraceClient mTraceClient = null;//轨迹客户端
    /**
     * Entity标识  TODO
     */
    public String entityName = "myTrace";
    private Boolean state=true;
    // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
    boolean isNeedObjectStorage = true;
    private static OnTraceListener mTraceListener=null;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
         root = inflater.inflate(R.layout.fragment_dashboard, container, false);
       mMapView= root.findViewById(R.id.bmapView);
       mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
       mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
       //注册重定位按钮监听
        reloaction=root.findViewById(R.id.reLocation);
        reloaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReLocation();
            }
        });
        startStackBtn=root.findViewById(R.id.btnGetStack);
        startStackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state){
                    state=false;
                    startStackBtn.setText("停止采集");
                    mTraceClient.startGather(mTraceListener);
                }else{
                    state=true;
                    startStackBtn.setText("开始采集");
                    mTraceClient.stopGather(mTraceListener);
                }
            }
        });
        initLocationOption();

        return root;
    }
    //轨迹记录服务
    public void traceService()
    {
        // 初始化轨迹服务
        mTrace = new Trace(serviceId, entityName, isNeedObjectStorage);
        // 初始化轨迹服务客户端
        mTraceClient = new LBSTraceClient(getActivity().getApplicationContext());
        // 定位周期(单位:秒)
        int gatherInterval = 5;
        // 打包回传周期(单位:秒)
        int packInterval = 10;
        // 设置定位和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval);
        // 初始化轨迹服务监听器
        mTraceListener = new OnTraceListener() {
            @Override
            public void onBindServiceCallback(int i, String s) {
                Log.e(TAG, "-----i:"+i+"--------message:"+i+"--------------------onBindServiceCallback" );
            }

            // 开启服务回调
            @Override
            public void onStartTraceCallback(int status, String message) {
                Toast.makeText(getActivity(),"开启服务:"+message,Toast.LENGTH_SHORT ).show();
                Log.e(TAG, "-----status:"+status+"--------message:"+message+"--------------------onStartTraceCallback" );
            }
            // 停止服务回调
            @Override
            public void onStopTraceCallback(int status, String message) {
                Toast.makeText(getActivity(),"结束服务:"+message,Toast.LENGTH_SHORT    ).show();
                //record.setEndDate(df.format(new Date()));
                Log.e(TAG, "-----status:"+status+"--------message:"+message+"--------------------onStopTraceCallback" );
            }
            // 开启采集回调
            @Override
            public void onStartGatherCallback(int status, String message) {
                Toast.makeText(getActivity(),"开启采集:"+message,Toast.LENGTH_SHORT).show();
                //record.setStartDate(df.format(new Date()));
                Log.e(TAG, "-----status:"+status+"--------message:"+message+"--------------------onStartGatherCallback" );
            }
            // 停止采集回调
            @Override
            public void onStopGatherCallback(int status, String message) {
                Toast.makeText(getActivity(),"结束采集:"+message,Toast.LENGTH_SHORT    ).show();
                //record.setEndDate(df.format(new Date()));
                Log.e(TAG, "-----status:"+status+"--------message:"+message+"--------------------onStopGatherCallback" );
            }
            // 推送回调
            @Override
            public void onPushCallback(byte messageNo, PushMessage message) {
                Toast.makeText(getActivity(),"messageNo:"+messageNo+",message:"+message,Toast.LENGTH_SHORT    ).show();
                Log.e(TAG, "-----messageNo:"+messageNo+"--------message:"+message+"--------------------onPushCallback" );
            }

            @Override
            public void onInitBOSCallback(int i, String s) {
                //Toast.makeText(MainActivity.this,"i:"+i+",message:"+s,Toast.LENGTH_SHORT    ).show();
                Log.e(TAG, "-----i:"+i+"--------s:"+s+"--------------------onInitBOSCallback" );
            }

        };
        mTraceClient.startTrace(mTrace, mTraceListener);
    }
    //实现手动定位
    public void ReLocation()
    {
        Log.e(TAG, "手动定位: " );
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(mapStatusUpdate);//以动画的方式跟新地图
        //btn1.setText("纬度："+latLng.latitude+"经度："+latLng.longitude);
    }

    /**
     * 初始化定位参数配置
     */

    private void initLocationOption() {
//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        LocationClient locationClient = new LocationClient(getActivity().getApplicationContext());
//声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
//
        BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                 zoom = mapStatus.zoom;
               if(mBaiduMap.getMapStatus().zoom-zoom>0.1)
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(zoom).build()));
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

            }
        };
        //注册监听
        mBaiduMap.setOnMapStatusChangeListener(onMapStatusChangeListener);
//注册监听函数
        locationClient.registerLocationListener(myLocationListener);
//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd09ll");
//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1000);
//可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
//可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
//可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
//可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
        //设置定位方向：
        locationOption.setNeedDeviceDirect(true);
//可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(2000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
//开始定位
        locationClient.start();
    }
    /**
     * 实现定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        String result=null;
        @Override
        public void onReceiveLocation(final BDLocation location){
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //获取纬度信息
            double latitude = location.getLatitude();
            //获取经度信息
            double longitude = location.getLongitude();
            //获取定位精度，默认值为0.0f
            float radius = location.getRadius();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = location.getCoorType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            result="维度："+latitude+",精度："+longitude+",定位精度："+radius+",坐标类型:"+coorType;
            //构造地图
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            setPosition2Center(mBaiduMap, location, true);
            String addr = location.getAddrStr();    //获取详细地址信息
            Log.e(TAG, "onReceiveLocation: "+result );
            //textView.setText(result+","+addr+";"+location.getSpeed());
        }
    }
    //显示定位  重要！！！！TODO
    public void setPosition2Center(BaiduMap map, BDLocation bdLocation, Boolean isShowLoc) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                .direction(bdLocation.getRadius()).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        map.setMyLocationData(locData);
        if (isShowLoc) {
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());//
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(zoom);//设置地图操作中心点
            map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));///以动画的方式更行地图
        }
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mTraceClient.stopTrace(mTrace, mTraceListener);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}