package com.example.login;

import android.content.Intent;
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
import cn.bmob.v3.listener.SaveListener;


public class RegisterStepSmsActivity extends AppCompatActivity {


    @BindView(R.id.et_sms)
    EditText etSms;
    @BindView(R.id.btn_sms)
    Button btnSms;
    @BindView(R.id.bt_login)
    TextView btLogin;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.at_title)
    TextView mAtTitle;
    private RegisterStepSmsActivity context;
    private String phone;
    private String pwd;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smscode);
        ButterKnife.bind(this);
        mAtTitle.setText("验证验证码");
        context = this;
        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        name = getIntent().getStringExtra("name");
    }

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            btnSms.setText("Countdown" + (millisUntilFinished / 1000));
            btnSms.setSelected(false);
        }

        @Override
        public void onFinish() {
            btnSms.setClickable(true);
            btnSms.setText("code");
            btnSms.setSelected(true);
        }
    };


    @OnClick({R.id.btn_sms, R.id.bt_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sms:
                timer.start();
                btnSms.setClickable(false);
                BmobSMS.requestSMSCode(phone, "Register", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {//验证码发送成功
                            showToast("success");
                        }
                    }
                });
                break;
            case R.id.bt_login:
                String sms = etSms.getText().toString().trim();
                if (TextUtils.isEmpty(sms)) {
                    showToast("verification code must be filled");
                    return;
                }
                User user = new User();
                user.setMobilePhoneNumber(phone);//设置手机号码（必填）
                user.setPassword(pwd);
                user.setUsername(name);
                user.signOrLogin(sms, new SaveListener<BmobUser>() {

                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e == null) {
                            showToast("注册成功");
                            startActivity(new Intent(RegisterStepSmsActivity.this, LoginByUserNameActivity.class));
                            finish();

                        } else {
                            showToast(e.getMessage());
                        }
                    }

                });
                break;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(RegisterStepSmsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
