package com.wei.permissionsetting;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.wei.permissionsetting.permission.activity.MainActivity;

/**
 * 经测试，如果app只开启自启动，执行一键清理，app所有进程都会被杀死，随后会复活。复活过程中，主进程只会执行完PermissionApp的onCreate()。其它进程则正常运行；
 * 如果开启自启动+无障碍服务，在小米手机上执行一键清理，app所有进程都会被杀死，随后所有进程都能正常复活。
 * author: WEI
 * date: 2018-01-20.
 */

public class PermissionApp extends Application
{
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
//        MainActivity.startMainActivity(this);
    }

}
