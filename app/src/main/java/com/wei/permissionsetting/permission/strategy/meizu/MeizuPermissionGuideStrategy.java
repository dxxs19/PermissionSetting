package com.wei.permissionsetting.permission.strategy.meizu;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.wei.permissionsetting.permission.strategy.IPermissionGuideStrategy;

/**
 * 魅族权限设置辅助策略
 * @author: WEI
 * @date: 2018/1/17
 */

public class MeizuPermissionGuideStrategy extends IPermissionGuideStrategy
{
    public MeizuPermissionGuideStrategy(Context paramContext, boolean paramBoolean) {
        super(paramContext);
    }

    @Override
    public void actionAutoBootPermission() {

    }

    @Override
    public void actionPowerPermisssion() {

    }

    @Override
    public void actionPermissionsEditor() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void handleAccessbilityEvent(AccessibilityEvent paramAccessibilityEvent, AccessibilityService paramAccessibilityService) {
        int eventType = paramAccessibilityEvent.getEventType();
        String currentPkg = (String) paramAccessibilityEvent.getPackageName();
        String currentClz = (String) paramAccessibilityEvent.getClassName();
        Log.e(TAG, "eventType = " + eventType + ", pkgname = " + currentPkg + ", clsname = " + currentClz);
        AccessibilityNodeInfo rootInActiveWindow;
        if (((eventType == 4096) || (eventType == 2048) || (eventType == 32)) && (paramAccessibilityEvent.getPackageName() != null)) {
            rootInActiveWindow = paramAccessibilityService.getRootInActiveWindow();
            if (rootInActiveWindow != null) {

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void configAccessbility(AccessibilityService paramAccessibilityService) {
        AccessibilityServiceInfo localAccessibilityServiceInfo = new AccessibilityServiceInfo();
        localAccessibilityServiceInfo.packageNames = new String[] { "com.meizu.safe", "com.android.settings" };
        localAccessibilityServiceInfo.eventTypes = -1;
        localAccessibilityServiceInfo.feedbackType = 16;
        localAccessibilityServiceInfo.notificationTimeout = 1000L;
        localAccessibilityServiceInfo.flags |= 0x10;
        paramAccessibilityService.setServiceInfo(localAccessibilityServiceInfo);
        paramAccessibilityService.performGlobalAction(1);
    }
}
