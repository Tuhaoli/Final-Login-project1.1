package com.example.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;


public class LoginBySmsActivity extends AppCompatActivity {
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.captcha)
    EditText captcha;
    @BindView(R.id.get_captcha)
    Button getCaptcha;
    @BindView(R.id.bt_ok)
    Button btOk;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.at_title)
    TextView mAtTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smslogin);
        ButterKnife.bind(this);
        mAtTitle.setText("Verification code login");
    }

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            getCaptcha.setText("" + (millisUntilFinished / 1000));
            getCaptcha.setSelected(false);
        }

        @Override
        public void onFinish() {
            getCaptcha.setClickable(true);
            getCaptcha.setText("Verification");
            getCaptcha.setSelected(true);
        }
    };

    @OnClick({R.id.get_captcha, R.id.bt_ok})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.get_captcha:
                String phoneNum = phone.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    showToast("Please enter phone number");
                    return;
                }
                timer.start();
                getCaptcha.setClickable(false);
                BmobSMS.requestSMSCode(phoneNum, "Login", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {//验证码发送成功
                            showToast("success");
                        }
                    }
                });

                break;
            case R.id.bt_ok:
                String num = phone.getText().toString().trim();
                if (TextUtils.isEmpty(num)) {
                    showToast("Please enter phone number");
                    return;
                }
                String captchaNum = captcha.getText().toString().trim();
                if (TextUtils.isEmpty(captchaNum)) {
                    showToast("verification code must be filled");
                    return;
                }
                BmobUser.loginBySMSCode(num, captchaNum, new LogInListener<Object>() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            showToast("success");
                           setResult(RESULT_OK);
                           finish();
                        } else {
                            showToast("fail" + e.getLocalizedMessage());
                        }
                    }
                });

                break;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(LoginBySmsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
