package graduationdesign.sharedparkingspaces.presenter;

import graduationdesign.sharedparkingspaces.view.UserInfoActivity;

/**
 * Created by wangmengjie on 2018/4/19.
 */

public interface IUserInfoPresenter {
    void setView(UserInfoActivity.IUserInfoView view);

    void changeUserInfo(String newInfo);
}
