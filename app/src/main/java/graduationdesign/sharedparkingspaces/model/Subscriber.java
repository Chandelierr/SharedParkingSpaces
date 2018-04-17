package graduationdesign.sharedparkingspaces.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wangmengjie on 2018/4/14.
 */

public class Subscriber implements Serializable{
    private String mTel;
    private String mPassword;
    private String mUserName;
    private int mSex;
    private String mBirthday;
    private ArrayList<String> mPlateName;

    /**
     * "username": "zhangzhao",
     "sex": 1,  #1表示男， 2表示女， 3表示保密
     "birthday": "2010-03-24", #没有完善信息时为null
     "plate_number": ["陕A-AE86", "陕A-6666"]  #没有添加车牌时， 为空列表[]
     * @param tel
     */
    public Subscriber() {
    }

    public Subscriber(String tel) {
        mTel = tel;
    }

    public String getBirthday() {
        return mBirthday;
    }

    public void setBirthday(String birthday) {
        mBirthday = birthday;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public ArrayList<String> getPlateName() {
        return mPlateName;
    }

    public void setPlateName(ArrayList<String> plateName) {
        mPlateName = plateName;
    }
    public boolean addPlateName(String plate) {
        if (mPlateName == null) {
            mPlateName = new ArrayList<>();
        }
        if (mPlateName.size() < 10) {
            mPlateName.add(plate);
            return true;
        }
        return false;
    }

    public int getSex() {
        return mSex;
    }

    public void setSex(int sex) {
        mSex = sex;
    }

    public String getTel() {
        return mTel;
    }

    public void setTel(String tel) {
        mTel = tel;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }


    @Override
    public String toString() {
        return "Subscriber{" +
                "mBirthday='" + mBirthday + '\'' +
                ", mTel='" + mTel + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mUserName='" + mUserName + '\'' +
                ", mSex=" + mSex +
                ", mPlateName=" + mPlateName +
                '}';
    }
}
