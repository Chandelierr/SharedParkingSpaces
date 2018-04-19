package graduationdesign.sharedparkingspaces.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wangmengjie on 2018/4/14.
 */

public class Subscriber implements Serializable{
    private String tel;
    private String password;
    private String username;
    private int sex;
    private String birthday;
    private ArrayList<String> plate_number;

    public Subscriber() {
    }

    public Subscriber(String tel) {
        this.tel = tel;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getPlateName() {
        return plate_number;
    }

    public void setPlateName(ArrayList<String> plateName) {
        this.plate_number = plateName;
    }
    public boolean addPlateName(String plate) {
        if (plate_number == null) {
            plate_number = new ArrayList<>();
        }
        if (plate_number.size() < 10) {
            plate_number.add(plate);
            return true;
        }
        return false;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }


    @Override
    public String toString() {
        return "Subscriber{" +
                "birthday='" + birthday + '\'' +
                ", tel='" + tel + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", sex=" + sex +
                ", plate_number=" + plate_number +
                '}';
    }
}
