package com.wei.permissionsetting.permission.strategy.meizu;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.wei.permissionsetting.R;
import com.wei.permissionsetting.permission.strategy.IPermissionGuideStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * 魅族权限设置辅助策略
 * @author: WEI
 * @date: 2018/1/17
 */

public class MeizuPermissionGuideStrategy extends IPermissionGuideStrategy
{
    private static final String MEIZU_SEC_APP_SETTINGS_ACTIVITY = "com.meizu.safe.security.AppSecActivity";
    private static final String MEIZU_SEC_PACKAGE_NAME = "com.meizu.safe";
    private static final String MEIZU_RUNIN_BACKGROUND = "后台管理";
    private static final String MEIZU_RUNIN_BACKGROUND_ALLOW = "允许后台运行";
    private List<String> mPermissionList = new ArrayList<>();

    public MeizuPermissionGuideStrategy(Context paramContext, boolean paramBoolean) {
        super(paramContext);
        mPermissionList = getPermissionList(R.array.Meizu_permissions);
    }

    @Override
    public void actionAutoBootPermission() {
        Intent localIntent;
        try {
            localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            localIntent.setClassName(MEIZU_SEC_PACKAGE_NAME, MEIZU_SEC_APP_SETTINGS_ACTIVITY);
            localIntent.putExtra("packageName", mContext.getPackageName());
            mContext.startActivity(localIntent);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void actionPowerPermisssion() {

    }

    @Override
    public void actionPermissionsEditor() {

    }

    private List<AccessibilityNodeInfo> mAccessibilityNodeInfos = new ArrayList<>();
    private boolean isDialogShowing = false;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void handleAccessbilityEvent(AccessibilityEvent paramAccessibilityEvent, AccessibilityService paramAccessibilityService) {
        int eventType = paramAccessibilityEvent.getEventType();
        String currentPkg = (String) paramAccessibilityEvent.getPackageName();
        String currentClz = (String) paramAccessibilityEvent.getClassName();
        Log.e(TAG, "eventType = " + eventType + ", pkgname = " + currentPkg + ", clsname = " + currentClz);
        AccessibilityNodeInfo rootInActiveWindow;
        if (((eventType == 4096) || (eventType == 2048) || (eventType == 32)) && (MEIZU_SEC_PACKAGE_NAME.equals(paramAccessibilityEvent.getPackageName()))) {
            rootInActiveWindow = paramAccessibilityService.getRootInActiveWindow();
            if (rootInActiveWindow != null) {
                if ("android.app.AlertDialog".equals(currentClz))
                {
                    List<AccessibilityNodeInfo> nodeInfos = getNodeInfosByText(rootInActiveWindow, MEIZU_RUNIN_BACKGROUND_ALLOW);
                    if (nodeInfos == null)
                    {
                        nodeInfos = getNodeInfosByText(rootInActiveWindow, "允许");
                    }
                    if (nodeInfos != null)
                    {
                        AccessibilityNodeInfo nodeInfo = nodeInfos.get(0);
                        performAction(nodeInfo, ACTION_CLICK);
                        isDialogShowing = false;
                    }
                }
                else if (getNodeInfosById(rootInActiveWindow, "android:id/list") != null)
                {
                    List<AccessibilityNodeInfo> nodeInfos1 = getNodeInfosByText(rootInActiveWindow, "智能后台", "禁止后台运行", "询问", "禁止");
                    if (nodeInfos1 == null)
                    {
                        performScrollForward(rootInActiveWindow, "android:id/list");
                        sHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mAccessibilityNodeInfos.size() <= 0)
                                {
                                    performGlobalAction(GLOBAL_ACTION_BACK);
                                }
                            }
                        }, 500);
                    }
                    else {
                        if (mAccessibilityNodeInfos.size() <= 0)
                        {
                            mAccessibilityNodeInfos.addAll(nodeInfos1);
                        }
                        if (mAccessibilityNodeInfos.size() >= 0)
                        {
                            if (!isDialogShowing)
                            {
                                isDialogShowing = true;
                                AccessibilityNodeInfo nodeInfo = mAccessibilityNodeInfos.get(0);
                                performAction(getClickableParent(nodeInfo), ACTION_CLICK);
                                mAccessibilityNodeInfos.remove(nodeInfo);
                            }
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void configAccessbility(AccessibilityService paramAccessibilityService) {
        AccessibilityServiceInfo localAccessibilityServiceInfo = new AccessibilityServiceInfo();
        localAccessibilityServiceInfo.packageNames = new String[] { MEIZU_SEC_PACKAGE_NAME, "com.android.settings" };
        localAccessibilityServiceInfo.eventTypes = -1;
        localAccessibilityServiceInfo.feedbackType = 16;
        localAccessibilityServiceInfo.notificationTimeout = 1000L;
        localAccessibilityServiceInfo.flags |= 0x10;
        paramAccessibilityService.setServiceInfo(localAccessibilityServiceInfo);
        paramAccessibilityService.performGlobalAction(1);
        performGlobalAction(GLOBAL_ACTION_BACK);
        actionAutoBootPermission();
    }
}
