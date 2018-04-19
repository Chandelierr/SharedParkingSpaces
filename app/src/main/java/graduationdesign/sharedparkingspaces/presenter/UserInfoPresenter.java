package graduationdesign.sharedparkingspaces.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import graduationdesign.sharedparkingspaces.model.Subscriber;
import graduationdesign.sharedparkingspaces.network.HttpHelper;
import graduationdesign.sharedparkingspaces.view.UserInfoActivity;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static graduationdesign.sharedparkingspaces.util.SerialUtil.serializeUser;

/**
 * Created by wangmengjie on 2018/4/19.
 */

public class UserInfoPresenter implements  IUserInfoPresenter{
    private static final String TAG = "UserInfoPresenter";
    private UserInfoActivity.IUserInfoView mView;
    public UserInfoPresenter() {
    }

    @Override
    public void setView(UserInfoActivity.IUserInfoView view) {
        mView = view;
    }

    @Override
    public void changeUserInfo(final String newInfo) {
        io.reactivex.Observable
                .create(new ObservableOnSubscribe<Subscriber>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Subscriber> emitter) throws Exception {
                        String result = HttpHelper.getInstance().post("http://47.98.170.172:8000/modify_info", newInfo);
                        Log.d(TAG, "result: " + result);
                        JSONObject response = JSON.parseObject(result);
                        int status = response.getIntValue("status");
                        Log.d(TAG, "status: " + status);
                        if (status == 0) {
                            JSONObject newRe = response.getJSONObject("values");
                            Log.d(TAG, "newRe: " + newRe.toJSONString());
                            Subscriber user = JSON.parseObject(newRe.toJSONString(), Subscriber.class);
                            emitter.onNext(user);
                        }else {
                            emitter.onError(new Throwable());
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Subscriber>() {
                    @Override
                    public void accept(Subscriber subscriber) throws Exception {
                        Log.d(TAG, "user: " + subscriber.toString());
                        subscriber.setPassword(mView.getUserPassword());
                        saveUser(serializeUser(subscriber));
                        mView.modifySuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "有错误");
                    }
                });
    }
    private void saveUser(String serial) {
        SharedPreferences sp = mView.getAppContext().getSharedPreferences("user", 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("user", serial);
        edit.apply();
    }

}
