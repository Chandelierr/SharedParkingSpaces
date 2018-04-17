package graduationdesign.sharedparkingspaces.presenter;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;

import graduationdesign.sharedparkingspaces.MainActivity;
import graduationdesign.sharedparkingspaces.model.Subscriber;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangmengjie on 2018/4/13.
 */

public class MainPresenter implements IMainPresenter{
    private static final String TAG = "MainPresenter";

    private MainActivity.IMainView mView;
    private LocationManager mLocManager;
    private Context mContext;
    public MainPresenter() {
    }

    @Override
    public void setView(MainActivity.IMainView view) {
        mView = view;
    }

    /**
     * 通过网络和GPS获取位置信息
     * @return
     */
    public Location getLocation() {
        Location location;
        mLocManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            location = mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else if (mLocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            location = mLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } else {
            Toast.makeText(mContext, "请检查网络或GPS是否打开", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (location != null){
            Log.d(TAG, "GPS纬度: " + location.getLatitude() + "\nGPS经度: " + location.getLongitude());
        }
        return location;
    }

    public void getNearbyParkingLots() {
        //return curParkingLot;
    }

    public void getUser() {
        Observable.create(new ObservableOnSubscribe<Subscriber>() {
            @Override
            public void subscribe(ObservableEmitter<Subscriber> e) throws Exception {
                Log.d(TAG,"thread name: " + Thread.currentThread().getName());
                SharedPreferences sp = mView.getAppContext().getSharedPreferences("user", 0);
                String deSerial = sp.getString("user", null);
                Subscriber user = deSerialUser(deSerial);
                if (user != null){
                    e.onNext(user);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Subscriber>() {
                    @Override
                    public void accept(Subscriber subscriber) throws Exception {
                        mView.setUser(subscriber);
                    }
                });
    }

    private Subscriber deSerialUser(String deSerial) {
        long startTime = System.currentTimeMillis();
        String redStr = null;
        Subscriber user = null;
        try {
            redStr = java.net.URLDecoder.decode(deSerial, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            user = (Subscriber) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        Log.d(TAG, "反序列化耗时为:" + (endTime - startTime));
        return user;
    }
}
