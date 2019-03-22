package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.at_title)
    TextView mAtTitle;
    private RegisterActivity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mAtTitle.setText("register account");
        context = this;
    }

    @OnClick(R.id.bt_login)
    public void onClick() {
        String phone = etName.getText().toString();
        String name = etUsername.getText().toString();
        String pwd = etPwd.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(context, "Please enter phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(context, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Please enter your username", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.length() != 11) {
            Toast.makeText(context, "Phone number length is incorrect", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.setClass(this, RegisterStepSmsActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("pwd", pwd);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
