package com.example.login;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;



public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Bmob.initialize(this, "b31a61b730e192c26d3a1e97b86c86d3");
    }
    public static Context getContext(){
        return mContext;
    }
}
