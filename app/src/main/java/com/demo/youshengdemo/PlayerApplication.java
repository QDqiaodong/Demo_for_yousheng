package com.demo.youshengdemo;

import android.app.Application;
import android.content.Context;

/**
 * author : Qiaodong
 * e-mail : qiaodong@okay.cn
 * date   : 2019/5/1812:39 PM
 * <p>
 * desc   :
 */
public class PlayerApplication extends Application {

    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this.getApplicationContext();
    }

    public static Context getPlayerApplication() {
        return applicationContext;
    }
}
