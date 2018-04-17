package graduationdesign.sharedparkingspaces.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import graduationdesign.sharedparkingspaces.R;

/**
 * Created by wangmengjie on 2018/4/16.
 */

public class AlertHelper {
    private static Context mContext;
    private static class SingletonHolder{
        private static final AlertHelper instance = new AlertHelper();
    }
    private AlertHelper() {

    }

    public static final AlertHelper getInstance(Context context){
        mContext = context;
        return SingletonHolder.instance;
    }

    public boolean showHintAlert(int title, int message, int negative, int positive) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        return false;
                    }
                })
                .setPositiveButton(R.string.sign_in_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        initView();
                    }
                })
                .create().show();
        return false;
    }
}
