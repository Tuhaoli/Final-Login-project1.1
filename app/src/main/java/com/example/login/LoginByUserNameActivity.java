package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class LoginByUserNameActivity extends AppCompatActivity {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    TextView btLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_get_pwd)
    TextView tvGetPwd;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.bt_sms)
    Button mBtSms;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    private LoginByUserNameActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = this;

    }


    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_get_pwd,R.id.iv_scan,R.id.bt_sms})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String name = etName.getText().toString();
                String password = etPwd.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(context, "please enter user name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(context, "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                User bu2 = new User();
                bu2.setUsername(name);
                bu2.setPassword(password);
                bu2.login(new SaveListener<User>() {

                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            Toast.makeText(context, "login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginByUserNameActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                break;
            case R.id.tv_register:
                startActivity(new Intent(context, RegisterActivity.class));
                break;
            case R.id.tv_get_pwd:
                startActivity(new Intent(context, ResetPwdActivity.class));
                break;
            case R.id.iv_scan:
                startActivityForResult(new Intent(context, CaptureActivity.class),99);
                break;
            case R.id.bt_sms:
                startActivityForResult(new Intent(context, LoginBySmsActivity.class),100);
                break;

        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK){
            startActivity(new Intent(LoginByUserNameActivity.this, MainActivity.class));
            finish();
        }
        if (resultCode==0&&requestCode==99){
            if (data!=null){
                String result =  data.getStringExtra("qrcode_result");
                String[] arr = result.split(",");
                if (arr.length==2){
                    User bu2 = new User();
                    bu2.setUsername(arr[0]);
                    bu2.setPassword(arr[1]);
                    bu2.login(new SaveListener<User>() {

                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null) {
                                Toast.makeText(context, "login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginByUserNameActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }else{
                    Toast.makeText(context, "Invalid QR code", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
