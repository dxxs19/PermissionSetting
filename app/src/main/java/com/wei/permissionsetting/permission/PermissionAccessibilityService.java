package com.wei.permissionsetting.permission;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * @author: WEI
 * @date: 2018/1/15
 */

public class PermissionAccessibilityService extends AccessibilityService
{
    public static AccessibilityListener sListenner;
    private static PermissionAccessibilityService sService = null;
    private final String TAG = getClass().getSimpleName();

    public static PermissionAccessibilityService getService() {
        return sService;
    }

    public static void setAccessibilityListener(AccessibilityListener listenner) {
        sListenner = listenner;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        Log.e(TAG, "eventType : " + eventType);
        if (sListenner != null)
        {
            sListenner.onAccessibilityEvent(event);
        }
    }

    @Override
    public void onInterrupt() {
        sService = null;
        if (sListenner != null)
        {
            sListenner.onInterrupt();
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        sService = this;
        Log.e(TAG, "onServiceConnected success!");
        if (sListenner != null)
        {
            sListenner.onServiceConnected(sService);
        }
    }

    public interface AccessibilityListener
    {
        void onAccessibilityEvent(AccessibilityEvent accessibilityEvent);
        void onInterrupt();
        void onServiceConnected(AccessibilityService accessibilityService);
    }
}
