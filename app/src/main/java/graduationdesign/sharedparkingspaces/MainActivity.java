package graduationdesign.sharedparkingspaces;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import graduationdesign.sharedparkingspaces.presenter.MainPresenter;
import graduationdesign.sharedparkingspaces.view.IView;

public class MainActivity extends AppCompatActivity implements IView{
    private static final String TAG = "MainActivity";

    private MainPresenter mPresenter;

    private MapView mMapView = null;
//    定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
    private LocationClient mLocationClient = null;
    private BaiduMap mBaiduMap = null;
    private boolean mFirstLocation;
    private MyLocationConfiguration mLocationConfig;
    private BDLocation mCurrentLocation;

    private ImageButton mReLoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        initPresenter();
        initView();
        initData();
    }

    private void initPresenter() {
        mPresenter = new MainPresenter(this);
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mReLoc = (ImageButton) findViewById(R.id.reLoc);
        mReLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //有时会失败
//                Location location = mPresenter.getLocation();
                Log.d(TAG, "bdLocation is null: " + (mCurrentLocation == null));
                if (mCurrentLocation != null) {
                    LatLng xy = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    //描述地图状态将要发生变化  设置地图新中心点
                    MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(xy);
                    //以动画方式更新地图状态，动画耗时 300 ms
                    mBaiduMap.animateMapStatus(status);
                }
            }
        });
    }

    private BDAbstractLocationListener mLocListener = new BDAbstractLocationListener() {
        /**
         * 定位请求回调函数
         * @param bdLocation
         */
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // map view 销毁后不在处理新接收的位置
            if (bdLocation == null || mMapView == null)
                return;
            mCurrentLocation = bdLocation;
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    //定位精度
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100)
                    //百度纬度
                    .latitude(bdLocation.getLatitude())
                    //百度经度
                    .longitude(bdLocation.getLongitude())
                    .build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            Log.d(TAG, "Baidu纬度: " + bdLocation.getLatitude() + "\nBaidu经度: " + bdLocation.getLongitude());

            // 第一次定位时，将地图位置移动到当前位置
            if (mFirstLocation) {
                mFirstLocation = false;
                //地理坐标基本数据结构
                LatLng xy = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                //描述地图状态将要发生变化  设置地图新中心点
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(xy);
                //以动画方式更新地图状态，动画耗时 300 ms
                mBaiduMap.animateMapStatus(status);
            }

            //请求附近停车场
            //mPresenter.getNearbyParkingLots();
        }
    };

    private void initData() {
        mBaiduMap = mMapView.getMap();
        //描述地图状态将要发生变化  设置地图缩放级别
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15f);
        //改变地图状态
        mBaiduMap.setMapStatus(msu);
        initLocation();
        initMarkers();
    }

    private void initLocation() {
        //定位初始化
        mLocationClient = new LocationClient(this);
        mFirstLocation = true;
        //设置定位的相关配置
        LocationClientOption option = new LocationClientOption();
        //设置定位模式为高精度模式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        //设置坐标类型 取值有3个： 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
        option.setCoorType("bd09ll");
        //设置扫描间隔，单位是ms
        option.setScanSpan(30000);
        mLocationClient.setLocOption(option);

        //设置自定义图标 太丑了
        //mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.location_24px);
        //配置定位图层显示方式 参数：定位图层方式、是否允许显示方向信息、图标
        mLocationConfig = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
        mBaiduMap.setMyLocationConfiguration(mLocationConfig);

        mLocationClient.registerLocationListener(mLocListener);
    }

    private void initMarkers(){
        List<OverlayOptions> options = new ArrayList<>();
        BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.mipmap.punctuation);
        LatLng point1 = new LatLng(34.161472, 108.913053+0.0001);
        LatLng point2 = new LatLng(34.161472+0.0001, 108.913053);
        OverlayOptions option1 =  new MarkerOptions()
                .position(point1)
                .icon(bdA);
        OverlayOptions option2 =  new MarkerOptions()
                .position(point2)
                .icon(bdA);
        options.add(option1);
        options.add(option2);
        mBaiduMap.addOverlays(options);
    }


    @Override
    protected void onStart() {
        // 如果要显示位置图标,必须先开启图层定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
        {
            mLocationClient.start();
        }
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    //implement IView======================================================================
    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }
}
