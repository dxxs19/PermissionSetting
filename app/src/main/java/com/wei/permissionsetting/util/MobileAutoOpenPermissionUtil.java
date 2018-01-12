package com.wei.permissionsetting.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * @author: WEI
 * @date: 2018/1/10
 */

public class MobileAutoOpenPermissionUtil
{
    public static void autoStartManagementActivity(Context context)
    {
        ComponentName componentName = null;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String mobileType = getMobileType();
        if ("Xiaomi".equals(mobileType))
        {
            componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
        }
        else if ("HUAWEI".equals(mobileType))
        {

        }
        else if ("samsung".equals(mobileType))
        {

        }
        else if ("vivo".equals(mobileType))
        {

        }
        else if ("Meizu".equals(mobileType))
        {

        }
        intent.setComponent(componentName);
        context.startActivity(intent);
    }

    /**
     * 获取设备品牌
     * @return
     */
    private static String getMobileType(){
        return Build.MANUFACTURER;
    }
}
