package com.wei.permissionsetting.permission.strategy;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;

/**
 * @author: WEI
 * @date: 2018/1/15
 */

public class DefaultStrategy extends IPermissionGuideStrategy {
    public DefaultStrategy(Context paramContext) {
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

    @Override
    public void handleAccessbilityEvent(AccessibilityEvent paramAccessibilityEvent, AccessibilityService paramAccessibilityService) {

    }

    @Override
    public void configAccessbility(AccessibilityService paramAccessibilityService) {

    }
}
