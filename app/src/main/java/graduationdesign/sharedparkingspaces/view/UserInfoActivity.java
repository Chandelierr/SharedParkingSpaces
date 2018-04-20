package graduationdesign.sharedparkingspaces.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.List;

import graduationdesign.sharedparkingspaces.MainActivity;
import graduationdesign.sharedparkingspaces.R;
import graduationdesign.sharedparkingspaces.model.Subscriber;
import graduationdesign.sharedparkingspaces.presenter.UserInfoPresenter;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UserInfoActivity";
    public static final int FROM_USER_INFO_ACTIVITY = 2000;
    public static final int MODIFY_USER_INFO = 2001;

    private UserInfoPresenter mPresenter;

    private Subscriber mUser;

    private Toolbar mUserInfoToolbar;
    private TextView mTel;
    private TextView mUserName;
    private Spinner mSex;
    private TextView mBirthday;
    private TextView mPlates;
    private Button mSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mUser = (Subscriber) getIntent().getSerializableExtra("user");
        initPresenter();
        initView();
    }

    private void initPresenter() {
        mPresenter = new UserInfoPresenter();
        mPresenter.setView(mView);
    }

    private void initView() {
        mUserInfoToolbar = (Toolbar) findViewById(R.id.user_info_toolbar);
        mUserInfoToolbar.setNavigationIcon(R.mipmap.back);
        mUserInfoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mUserInfoToolbar.setTitle(R.string.user_info);
        mUserInfoToolbar.setTitleTextColor(getResources().getColor(R.color.color_707070));
        if (mUser != null) {
            mTel = (TextView)findViewById(R.id.info_tel);
            mUserName = (TextView)findViewById(R.id.info_user_name);
            mSex = (Spinner)findViewById(R.id.info_sex);
            mSaveBtn = (Button) findViewById(R.id.save);
            mSex.setSelection(mUser.getSex() - 1);
            mBirthday = (TextView)findViewById(R.id.info_birthday);
            mPlates = (TextView)findViewById(R.id.info_plates);
            if (mUser!= null) {
                mTel.setText(mUser.getTel());
                mTel.setOnClickListener(this);
                mUserName.setText(mUser.getUserName());
                mUserName.setOnClickListener(this);
                mSex.setSelection(mUser.getSex()-1);
                if (mUser.getBirthday() == null) {
                    mBirthday.setText(R.string.edit_birth);
                } else {
                    mBirthday.setText(mUser.getBirthday());
                }
                mBirthday.setOnClickListener(this);
                mPlates.setText(R.string.click_to_manage);
                mPlates.setOnClickListener(this);
                mSaveBtn.setOnClickListener(this);
            }
        } else {
            Toast.makeText(this, getString(R.string.no_user), Toast.LENGTH_SHORT).show();
        }

    }
    private DatePickerDialog mDateDialog;
    private DatePickerDialog.OnDateSetListener mDateListener;
    private AlertDialog.Builder mModifyUserNameBuilder;
    private AlertDialog.Builder mModifyPlatesBuilder;
    private List<String> mNewPlates;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.info_tel) {
            Toast.makeText(this, getResources().getString(R.string.can_not_edit), Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.info_user_name) {
            Log.d(TAG, "user name click");
            final EditText inputName = new EditText(this);
            inputName.setText(mUserName.getText());
            if (mModifyUserNameBuilder == null) {
                mModifyUserNameBuilder = new AlertDialog.Builder(this);
                mModifyUserNameBuilder.setTitle(R.string.edit_user_name)
                        .setNegativeButton(R.string.back, null);
            }
            mModifyUserNameBuilder
                    .setView(inputName)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String input = inputName.getText().toString();
                            mUserName.setText(input);
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else if (v.getId() == R.id.info_birthday) {
            Log.d(TAG, "birthday click");
            if (mDateListener == null) {
                mDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mBirthday.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                };
            }
            if (mDateDialog == null) {
                mDateDialog = new DatePickerDialog(this, android.app.AlertDialog.THEME_HOLO_LIGHT, mDateListener, 1996, 4, 24);
            }
            mDateDialog.show();
        } else if (v.getId() == R.id.info_plates) {
            Log.d(TAG, "plate manage click");
            final PlateManageView plateView = new PlateManageView(this);
            plateView.setPlatesData(mUser.getPlateNumber());;
            if (mModifyPlatesBuilder == null) {
                mModifyPlatesBuilder = new AlertDialog.Builder(this);
                mModifyPlatesBuilder.setTitle(R.string.plate_manage);
            }
            mModifyPlatesBuilder.setView(plateView)
                    .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mNewPlates = plateView.getData();
                        }
                    })
                    .create()
                    .show();
        } else if (v.getId() == R.id.save) {
            Log.d(TAG, "plates list: " + mNewPlates);
            Log.d(TAG, "plate to json:" + JSON.toJSONString(mNewPlates));
            String newBirth = mBirthday.getText().toString().equals(getResources().getString(R.string.edit_birth)) ? null : mBirthday.getText().toString();
            String newInfo = "{" +
                    "\"tel\":" + "\"" + mUser.getTel() + "\"," +
                    "\"username\":" + "\"" + mUserName.getText().toString() + "\"," +
                    "\"sex\":" + "\"" + (mSex.getSelectedItemPosition() + 1) + "\"," +
                    "\"birthday\":" + "\"" + newBirth + "\"," +
                    "\"plate_number\":" + JSON.toJSONString(mNewPlates) + "}";
            Log.d(TAG, "newInfo: " + newInfo);
            mPresenter.changeUserInfo(newInfo);
        }
    }

    //implement IView=====================================================================================
    public interface IUserInfoView extends IView{
        void modifySuccess();

        String getUserPassword();
    }
    private IUserInfoView mView = new IUserInfoView() {
        @Override
        public void modifySuccess() {
            Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
            setResult(MODIFY_USER_INFO, intent);
            finish();
        }

        @Override
        public String getUserPassword() {
            return mUser.getPassword();
        }

        @Override
        public Context getAppContext() {
            return getApplicationContext();
        }
    };
}
