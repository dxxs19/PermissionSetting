package com.wei.permissionsetting.permission.strategy;

import android.content.Context;

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
}
