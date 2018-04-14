package graduationdesign.sharedparkingspaces.view;

/**
 * Created by wangmengjie on 2018/4/14.
 */

public interface ILoginView extends IView{
    int getSignWay();
    void showProgress(boolean isShow);

    void passwordError();

    void signSuccess();
}
