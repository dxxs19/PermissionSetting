package com.wei.permissionsetting.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * 包工具类。判断某些应用是否安装，获取版本号等等
 * @author: WEI
 * @date: 2018/1/15
 */

public class PackageUtil
{
    /**
     * 获取版本号
     * @param paramContext
     * @param paramString
     * @return
     */
    public static String getVersionName(Context paramContext, String paramString)
    {
        PackageManager localPackageManager = paramContext.getPackageManager();
        try
        {
            paramString = localPackageManager.getPackageInfo(paramString, 0).versionName;
            return paramString;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 是否安装了某应用
     * @param context
     * @param pkg
     * @return
     */
    public static boolean isPackageInstalled(Context context, String pkg) {
        return doIsPackageInstalled(context, pkg);
    }

    public static boolean doIsPackageInstalled(Context context, String paramString)
    {
        try
        {
            context.getPackageManager().getPackageInfo(paramString, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public static void autoStartManagementActivity(Context context)
    {
        ComponentName componentName = null;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String mobileType = OSUtil.getMobileType();
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


}
