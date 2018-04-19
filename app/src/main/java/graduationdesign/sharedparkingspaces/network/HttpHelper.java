package graduationdesign.sharedparkingspaces.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by wangmengjie on 2018/4/14.
 */

public class HttpHelper {
    private static final String TAG = "HttpHelper";
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient mOkHttpClient;

    private static class SingletonHolder{
        private static final HttpHelper instance = new HttpHelper();
    }
    private HttpHelper() {
        mOkHttpClient = new OkHttpClient();
    }

    public static HttpHelper getInstance(){
        return SingletonHolder.instance;
    }

    public String getOp(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = mOkHttpClient.newCall(request).execute();
        String str;
        if (response.isSuccessful()){
            ResponseBody responseBody = response.body();
            str = responseBody.string();
        }else{
            return "error";
        }
        return str;
    }

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Log.d(TAG, body.contentType().toString());
        Response response = mOkHttpClient.newCall(request).execute();
        Log.d(TAG, "response code: " + response.code());
        if (response.isSuccessful()){
            ResponseBody responseBody = response.body();
            Log.d(TAG, "response body type: " + responseBody.contentType());
            return responseBody.string();
        }else{
            return "请求失败";
        }
    }

}
