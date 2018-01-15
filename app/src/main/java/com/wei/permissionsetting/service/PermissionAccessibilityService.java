package com.wei.permissionsetting.service;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.wei.permissionsetting.R;
import com.wei.permissionsetting.util.MobileAutoOpenPermissionUtil;

import java.util.List;

/**
 * @author: WEI
 * @date: 2018/1/10
 */

public class PermissionAccessibilityService extends AccessibilityService {
    private final String TAG = getClass().getSimpleName();
    private String mCurrentClass = null;
    private final Handler mHandler = new Handler();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final String tempClass = event.getClassName().toString();
        Log.e(TAG, "tempClass : " + tempClass);
        int type = event.getEventType();
        switch (type) {
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                break;

            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                mCurrentClass = tempClass;
                Log.e(TAG, "currentclass : " + mCurrentClass);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    handleEvent();
                }
                break;

            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                break;
            default:
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void handleEvent() { // android.widget.CheckBox
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) {
            Log.e(TAG, "root == null");
            return;
        }

        List<AccessibilityNodeInfo> nodeInfoList = root.findAccessibilityNodeInfosByText("免疫内分泌科");
        root.getContentDescription();
        Log.e(TAG, "nodeInfoList = " + nodeInfoList);

//        root.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        String appName = getResources().getString(R.string.app_name);
        Log.e(TAG, "appName : " + appName);
        if ("com.miui.permcenter.autostart.AutoStartManagementActivity".equals(mCurrentClass)) {
            List<AccessibilityNodeInfo> nodeInfos = root.findAccessibilityNodeInfosByText(appName);
            if (null != nodeInfos && nodeInfos.size() > 0) {
                recycle(nodeInfos.get(0));
                performGlobalAction(GLOBAL_ACTION_HOME);
            }
        }
    }

    private void recycle(AccessibilityNodeInfo appNodeInfo) {
        AccessibilityNodeInfo parentNodeInfo = appNodeInfo.getParent();
        int childCount = parentNodeInfo.getChildCount();
        Log.e(TAG, "parentNodeInfo : " + parentNodeInfo.getClassName() + ", childCount : " + childCount);
        if (childCount <= 1) {
            recycle(parentNodeInfo);
        } else {
            final AccessibilityNodeInfo nodeInfo = parentNodeInfo.getChild(childCount - 1);
            Log.e(TAG, "nodeInfo.getClassName() : " + nodeInfo.getClassName());
            if ("android.widget.CheckBox".equals(nodeInfo.getClassName())) {
                Log.e(TAG, "准备开启自启动");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!nodeInfo.isChecked()) {
                            Log.e(TAG, nodeInfo.isChecked() + "");
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);

                        }
                    }
                }, 500);

            }
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.e(TAG, "服务已连接！");
//        startService(new Intent(this, KeepAliveService.class));
        MobileAutoOpenPermissionUtil.autoStartManagementActivity(this);
    }

    @Override
    public void onInterrupt() {
        Log.e(TAG, "--- onInterrupt ---");
        Log.e(TAG, "服务已中断！");
    }
}
