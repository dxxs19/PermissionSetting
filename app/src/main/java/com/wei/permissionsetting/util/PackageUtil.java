package com.wei.permissionsetting.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * @author: WEI
 * @date: 2018/1/15
 */

public class PackageUtil
{
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
}
