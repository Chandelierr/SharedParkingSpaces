package graduationdesign.sharedparkingspaces.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import graduationdesign.sharedparkingspaces.view.IView;

/**
 * Created by wangmengjie on 2018/4/13.
 */

public class MainPresenter {
    private static final String TAG = "MainPresenter";

    private IView mView;
    private LocationManager mLocManager;
    private Context mContext;
    public MainPresenter(IView view) {
        mView = view;
        mContext = mView.getAppContext();
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
}
