package com.wei.permissionsetting.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
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
}
