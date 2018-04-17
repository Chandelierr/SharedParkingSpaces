package graduationdesign.sharedparkingspaces.util;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import graduationdesign.sharedparkingspaces.model.Subscriber;

/**
 * Created by wangmengjie on 2018/4/17.
 */

public class SerialUtil {
    private static final String TAG = "SerialUtil";

    public static String serializeUser(Subscriber user) {
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

    public static Subscriber deSerialUser(String deSerial) {
        long startTime = System.currentTimeMillis();
        String redStr = null;
        Subscriber user = null;
        try {
            redStr = java.net.URLDecoder.decode(deSerial, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            user = (Subscriber) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        Log.d(TAG, "反序列化耗时为:" + (endTime - startTime));
        return user;
    }
}
