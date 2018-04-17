package graduationdesign.sharedparkingspaces.presenter;


import graduationdesign.sharedparkingspaces.view.LoginActivity;

/**
 * Created by wangmengjie on 2018/4/14.
 */

public interface ILoginPresenter {
    void setView(LoginActivity.ILoginView view);
    boolean authIsNull();
    void sendSignInfo(String phoneNum, String password);
    int judgePositiveByCode(int result);
    int judgeSignResultByCode(int code);
}
