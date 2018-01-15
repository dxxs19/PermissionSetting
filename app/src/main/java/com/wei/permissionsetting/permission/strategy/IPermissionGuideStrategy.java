package com.wei.permissionsetting.permission.strategy;

/**
 * @author: WEI
 * @date: 2018/1/15
 */

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;

public abstract class  IPermissionGuideStrategy
{
    public Context mContext = null;

    public IPermissionGuideStrategy(Context paramContext) {
        mContext = paramContext;
    }

    public void clickAutoBootPermission()
    {
        actionAutoBootPermission();
    }

    protected void actionAutoBootPermission(){}

    protected void actionPowerPermisssion(){}

    public void handleAccessbilityEvent(AccessibilityEvent paramAccessibilityEvent, AccessibilityService paramAccessibilityService) { }

    public void configAccessbility(AccessibilityService paramAccessibilityService) {}
}
