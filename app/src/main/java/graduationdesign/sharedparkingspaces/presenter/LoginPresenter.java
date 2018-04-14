package graduationdesign.sharedparkingspaces.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import graduationdesign.sharedparkingspaces.network.HttpHelper;
import graduationdesign.sharedparkingspaces.view.ILoginView;

import static graduationdesign.sharedparkingspaces.view.LoginActivity.SIGN_IN;

/**
 * Created by wangmengjie on 2018/4/14.
 */

public class LoginPresenter implements ILoginPresenter{
    private static final String TAG = "LoginPresenter";
    private ILoginView mView;
    private Context mContext;
    private UserLoginTask mAuthTask = null;

    public LoginPresenter() {

    }

    @Override
    public void setView(ILoginView view) {
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

        UserLoginTask(String phone, String password) {
            mPhoneNum = phone;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String content = "{" +
                        "\"tel\":" + "\"" + mPhoneNum + "\"" + "," +
                        "\"password\":" + "\"" + mPassword + "\"" +
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
                parseJsonWithGson(result);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            mView.showProgress(false);

            if (success) {
                //finish();
                mView.signSuccess();
            } else {
                mView.passwordError();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            mView.showProgress(false);
        }
    }

    private void parseJsonWithGson(String result) {
        com.alibaba.fastjson.JSONObject response = JSON.parseObject(result);
        int status = response.getIntValue("status");
        Log.d(TAG, "parse status: " + status);
        if (mView.getSignWay() == SIGN_IN) {
            /**
             * status = -1 : 密码错误
             * status = 0 : 用户不存在，跳转到注册页面
             * status = 1 : 登陆成功
             * status = 2 : json 出错，登陆不成功
             */
        } else {
            /**
             * -1: 注册失败（添加数据库失败）
             * 0: 注册成功
             * 1：该手机号已注册
             * 2：json出错，注册不成功
             */
        }
    }

}
