package graduationdesign.sharedparkingspaces.presenter;

import graduationdesign.sharedparkingspaces.view.ILoginView;

/**
 * Created by wangmengjie on 2018/4/14.
 */

public interface ILoginPresenter {
    void setView(ILoginView view);
    boolean authIsNull();
    void sendSignInfo(String phoneNum, String password);
}
