package graduationdesign.sharedparkingspaces.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import graduationdesign.sharedparkingspaces.R;
import graduationdesign.sharedparkingspaces.model.Subscriber;
import graduationdesign.sharedparkingspaces.network.HttpHelper;
import graduationdesign.sharedparkingspaces.view.LoginActivity;

import static graduationdesign.sharedparkingspaces.view.LoginActivity.SIGN_IN;
import static graduationdesign.sharedparkingspaces.view.LoginActivity.SIGN_UP;

/**
 * Created by wangmengjie on 2018/4/14.
 */

public class LoginPresenter implements ILoginPresenter{
    private static final String TAG = "LoginPresenter";
    private LoginActivity.ILoginView mView;
    private Context mContext;
    private UserLoginTask mAuthTask = null;
    private Subscriber mUser;

    public LoginPresenter() {

    }

    @Override
    public void setView(LoginActivity.ILoginView view) {
        mView = view;
        mContext = mView.getAppContext();
    }

    @Override
    public boolean authIsNull() {
        return mAuthTask == null;
    }

    @Override
    public void sendSignInfo(String phoneNum, String password) {
        mAuthTask = new UserLoginTask(phoneNum, password);
        mAuthTask.execute((Void) null);
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPhoneNum;
        private final String mPassword;
        private int mStatus = -100;

        UserLoginTask(String phone, String password) {
            mPhoneNum = phone;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String content = "{" +
                        "\"tel\":" + "\"" + mPhoneNum + "\"" + "," +
                        "\"password\":" + "\"" + md5(mPassword) + "\"" +
                        "}";
                String url;
                if (mView.getSignWay() == SIGN_IN) {
                    url = "http://47.98.170.172:8000/user_login";
                    Log.d(TAG, "sign in");
                } else {
                    url = "http://47.98.170.172:8000/register";
                    Log.d(TAG, "sign up");
                }
                String result = HttpHelper.getInstance().post(url, content);
                Log.d(TAG, result);
                mStatus = parseJsonWithGson(result);
                if (mStatus == 0) {
                    if (mUser != null) {
                        mUser.setTel(mPhoneNum);
                        mUser.setPassword(mPassword);
                        Log.d(TAG, "user: " + mUser.toString());
                        saveUser(serializeUser(mUser));
                    }
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            mView.showProgress(false);

            if (success) {
                mView.signSuccess();
            } else {
                mView.signResult(mStatus);//处理错误结果
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            mView.showProgress(false);
        }
    }

    private void saveUser(String serial) {
        SharedPreferences sp = mView.getAppContext().getSharedPreferences("user", 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("user", serial);
        edit.apply();
    }

    private String serializeUser(Subscriber user) {
        long startTime = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        String serial = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(user);
            serial = byteArrayOutputStream.toString("ISO-8859-1");
            serial = java.net.URLEncoder.encode(serial, "UTF-8");
            Log.d(TAG, "serialize str =" + serial);
            objectOutputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        Log.d(TAG, "序列化耗时为:" + (endTime - startTime));
        return serial;
    }

    private int parseJsonWithGson(String result) {
        com.alibaba.fastjson.JSONObject response = JSON.parseObject(result);
        int status = response.getIntValue("status");
        if (status == 0) {
            mUser = new Subscriber();
            JSONObject values = response.getJSONObject("values");
            if (values != null) {
                mUser.setUserName(values.getString("username"));
                mUser.setSex(values.getIntValue("sex"));
                mUser.setBirthday(values.getString("birthday"));
                JSONArray plates = values.getJSONArray("plate_name");
                if (plates != null) {
                    for (int i = 0; i < plates.size(); i++) {
                        mUser.addPlateName(plates.getString(i));
                    }
                }
            }
        }
        Log.d(TAG, "parse status: " + status);
        return status;
    }



    public int judgePositiveByCode(int result) {
        if (mView.getSignWay() == SIGN_IN && (result == -1 || result == 2)) {//登陆出错
            return R.string.sign_in_again;
        } else if (mView.getSignWay() == SIGN_UP && result == 1) {//用户已存在
            return R.string.action_sign_in;
        } else if (mView.getSignWay() == SIGN_IN && result == 1) {//用户不存在
            return R.string.action_sign_up;
        } else {//注册出错
            return R.string.sign_up_again;
        }
    }

    public int judgeSignResultByCode(int code) {
        if (mView.getSignWay() == SIGN_IN) {
            switch (code) {
                case -1:return R.string.password_error;
                case 2: return R.string.sign_in_error_json_error;
                case 1:return R.string.user_not_exist;
            }
        } else {
            switch (code) {
                case -1:return R.string.sign_up_error;
                case 2: return R.string.sign_up_error_json_error;
                case 1:return R.string.user_exist;
            }
        }
        return 0;
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
