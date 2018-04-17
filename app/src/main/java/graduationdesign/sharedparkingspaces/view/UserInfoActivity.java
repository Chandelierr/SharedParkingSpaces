package graduationdesign.sharedparkingspaces.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import graduationdesign.sharedparkingspaces.R;
import graduationdesign.sharedparkingspaces.model.Subscriber;

public class UserInfoActivity extends AppCompatActivity {
    private static final String TAG = "UserInfoActivity";
    public static final int FROM_USER_INFO_ACTIVITY = 2000;

    private Subscriber mUser;
    private TextView mTel;
    private TextView mUserName;
    private TextView mSex;
    private TextView mBirthday;
    private TextView mPlates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mUser = (Subscriber) getIntent().getSerializableExtra("user");
        mTel = (TextView)findViewById(R.id.info_tel);
        mUserName = (TextView)findViewById(R.id.info_user_name);
        mSex = (TextView)findViewById(R.id.info_sex);
        mBirthday = (TextView)findViewById(R.id.info_birthday);
        mPlates = (TextView)findViewById(R.id.info_plates);
        if (mUser!= null) {
            mTel.setText(mUser.getTel());
            mUserName.setText(mUser.getUserName());
            mSex.setText(mUser.getSex());
            mBirthday.setText(mUser.getBirthday());
            mPlates.setText(mUser.getPlateName().hashCode());
        }
    }
}
