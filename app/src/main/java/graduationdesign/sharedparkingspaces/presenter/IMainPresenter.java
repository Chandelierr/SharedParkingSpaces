package graduationdesign.sharedparkingspaces.presenter;

import graduationdesign.sharedparkingspaces.MainActivity;

/**
 * Created by lenovo on 2018/4/17.
 */

public interface IMainPresenter {
    void setView(MainActivity.IMainView view);
    void getUser();
}
