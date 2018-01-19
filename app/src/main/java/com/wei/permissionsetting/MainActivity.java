package com.wei.permissionsetting;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.wei.permissionsetting.permission.PermissionAccessibilityService;
import com.wei.permissionsetting.permission.PermissionGuideGenerator;
import com.wei.permissionsetting.permission.strategy.IPermissionGuideStrategy;
import com.wei.permissionsetting.service.MyService;
import com.wei.permissionsetting.widget.ToastTips;

/**
 * 主界面
 * @author: WEI
 * @date: 2018/1/15
 */
public class MainActivity extends AppCompatActivity
{
    private final String TAG = getClass().getSimpleName();
    private AccessibilityService mService;
    private IPermissionGuideStrategy mStrategy;
    public static boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionAccessibilityService.setAccessibilityListener(mListenner);
        startService(new Intent(this, MyService.class));
        isFinish = false;
    }

    private PermissionAccessibilityService.AccessibilityListener mListenner = new PermissionAccessibilityService.AccessibilityListener()
    {
        @Override
        public void onAccessibilityEvent(AccessibilityEvent paramAnonymousAccessibilityEvent)
        {
            if ((mService != null) && (mStrategy != null))
            {
                mStrategy.handleAccessbilityEvent(paramAnonymousAccessibilityEvent, mService);
            }
        }

        @Override
        public void onInterrupt()
        {
            Log.e(TAG, "onInterrupt");
        }

        @Override
        public void onServiceConnected(AccessibilityService paramAnonymousAccessibilityService)
        {
            Log.e(TAG, "onServiceConnected");
            mService = PermissionAccessibilityService.getService();
            mStrategy = PermissionGuideGenerator.generateGuideStratagy(MainActivity.this, true);
            if (mStrategy != null)
            {
                mStrategy.configAccessbility(paramAnonymousAccessibilityService);
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "--- onRestart ---" + Build.VERSION.SDK_INT + ", " + Build.VERSION_CODES.N );
        try {
            mStrategy.removeFloatWindow();
        }
        catch (Exception e)
        {
            Log.e(TAG, "移除悬浮窗");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mService.disableSelf();
        }
        isFinish = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        showToast();
//        Button button = new Button(this);
//        button.setOnClickListener();

    }

    public void showToast()
    {
        ToastTips.makeText(this, "签到成功", Toast.LENGTH_SHORT).show();
    }

    public void setPermission(View view)
    {
        Log.e(TAG, "--- setPermission ---");
        Intent mAccessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        mAccessibleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mAccessibleIntent);
    }
}
