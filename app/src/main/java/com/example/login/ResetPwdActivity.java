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
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;


public class ResetPwdActivity extends AppCompatActivity {
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.captcha)
    EditText captcha;
    @BindView(R.id.get_captcha)
    Button getCaptcha;
    @BindView(R.id.newPwd)
    EditText newPwd;
    @BindView(R.id.bt_ok)
    Button btOk;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.at_title)
    TextView mAtTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        ButterKnife.bind(this);
        mAtTitle.setText("Retrieve password");
    }

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            getCaptcha.setText("Countdown" + (millisUntilFinished / 1000));
            getCaptcha.setSelected(false);
        }

        @Override
        public void onFinish() {
            getCaptcha.setClickable(true);
            getCaptcha.setText("code");
            getCaptcha.setSelected(true);
        }
    };

    @OnClick({R.id.get_captcha, R.id.bt_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_captcha:
                String phoneNum = phone.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    onToast("Phone number cannot be empty");
                    return;
                }
                timer.start();
                getCaptcha.setClickable(false);
                BmobSMS.requestSMSCode(phoneNum, "Login", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {//验证码发送成功
                            onToast("Sent successfully");
                        }
                    }
                });

                break;
            case R.id.bt_ok:
                String captchaNum = captcha.getText().toString().trim();
                if (TextUtils.isEmpty(captchaNum)) {
                    onToast("verification code must be filled");
                    return;
                }
                String pwd = newPwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    onToast("New password cannot be empty");
                    return;
                }
                BmobUser.resetPasswordBySMSCode(captchaNum, pwd, new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            onToast("Password reset succeeded");
                            finish();
                        } else {
                            onToast("fail" + e.getLocalizedMessage());
                        }
                    }


                });
                break;
        }
    }

    private void onToast(String msg) {
        Toast.makeText(ResetPwdActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
