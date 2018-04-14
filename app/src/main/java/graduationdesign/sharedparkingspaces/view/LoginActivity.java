package graduationdesign.sharedparkingspaces.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graduationdesign.sharedparkingspaces.R;
import graduationdesign.sharedparkingspaces.presenter.LoginPresenter;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String PHONE_NUMBER_REG =
            "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
    public static final int SIGN_IN = 1;//1:登陆 0：注册
    public static final int SIGN_UP = 0;
    public static final int FROM_LOGIN_ACTIVITY = 1000;

    private int mSignIn = SIGN_IN;

    private LoginPresenter mPresenter;

    //UI
    private Toolbar mSignToolbar;

    private AutoCompleteTextView mPhoneNumView;
    private EditText mPasswordView;
    private EditText mPasswordAgainView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mSignButton;

    private View mOtherSignWaysView;
    private TextView mSignUpView;

    //Data
    private String mPhoneNum;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initPresenter();
        initView();
    }
    private ILoginView mView = new ILoginView() {
        @Override
        public int getSignWay() {
            return mSignIn;
        }

        @Override
        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        public void showProgress(final boolean show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

                mOtherSignWaysView.setVisibility(show ? View.GONE : View.VISIBLE);
                mOtherSignWaysView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mOtherSignWaysView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mProgressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            } else {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                mOtherSignWaysView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        }

        @Override
        public void passwordError() {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }

        @Override
        public void signSuccess() {
            Intent intent = new Intent();
            intent.putExtra("tel", mPhoneNum);
            intent.putExtra("password", mPassword);
            setResult(1,intent);
            finish();
        }

        @Override
        public Context getAppContext() {
            return getApplicationContext();
        }
    };

    private void initPresenter() {
        mPresenter = new LoginPresenter();
        mPresenter.setView(mView);
    }

    private void initView() {
        mSignToolbar = (Toolbar) findViewById(R.id.sign_toolbar);
        mSignToolbar.setNavigationIcon(R.mipmap.back);
        mSignToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSignToolbar.setTitle(R.string.action_sign_in);
        mSignToolbar.setTitleTextColor(getResources().getColor(R.color.color_707070));

        //设置登陆模块
        mPhoneNumView = (AutoCompleteTextView) findViewById(R.id.phone_num);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordAgainView = (EditText) findViewById(R.id.password_again);

        mSignButton = (Button) findViewById(R.id.sign_in_button);
        mSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        //其他方式
        mOtherSignWaysView = findViewById(R.id.other_sign_ways);
        mSignUpView = (TextView) findViewById(R.id.sign_up);
        mSignUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSignUpStatus();
            }
        });
    }

    private void changeSignUpStatus() {
        mSignIn = SIGN_UP;
        mPasswordAgainView.setVisibility(View.VISIBLE);
        mSignButton.setText(getResources().getString(R.string.action_sign_up));
        mOtherSignWaysView.setVisibility(View.GONE);
        mSignToolbar.setTitle(R.string.action_sign_up);
    }


    private void attemptLogin() {
        if (!mPresenter.authIsNull()) {
            return;
        }

        //重置错误提示信息
        mPhoneNumView.setError(null);
        mPasswordView.setError(null);

        mPhoneNum = mPhoneNumView.getText().toString();
        mPassword = mPasswordView.getText().toString();
        String passwordAgain = mPasswordAgainView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //检查手机号码
        if (TextUtils.isEmpty(mPhoneNum)) {
            mPhoneNumView.setError(getString(R.string.error_field_required));
            focusView = mPhoneNumView;
            cancel = true;
        } else if (!isPhoneNumValid(mPhoneNum)) {
            mPhoneNumView.setError(getString(R.string.error_invalid_email));
            focusView = mPhoneNumView;
            cancel = true;
        }

        //检查密码
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(mPassword)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (mSignIn == SIGN_UP) {
            //再次检查密码
            if (TextUtils.isEmpty(passwordAgain)) {
                mPasswordAgainView.setError(getString(R.string.error_field_required));
                focusView = mPasswordAgainView;
                cancel = true;
            } else if (!isPasswordAgainValid(passwordAgain)) {
                mPasswordAgainView.setError(getString(R.string.error_invalid_password_again));
                focusView = mPasswordAgainView;
                cancel = true;
            }
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mView.showProgress(true);
            mPresenter.sendSignInfo(mPhoneNum, mPassword);
        }
    }

    private boolean isPhoneNumValid(String phone) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REG);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }


    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    private boolean isPasswordAgainValid(String password) {
        return password.equals(mPasswordView.getText().toString());
    }

}

