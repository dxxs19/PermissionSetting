package com.wei.permissionsetting.permission;

import android.content.Context;

import com.wei.permissionsetting.permission.strategy.DefaultStrategy;
import com.wei.permissionsetting.permission.strategy.IPermissionGuideStrategy;
import com.wei.permissionsetting.permission.strategy.meizu.MeizuPermissionGuideStrategy;
import com.wei.permissionsetting.permission.strategy.xiaomi.MiuiV6PermissionGuideStrategy;
import com.wei.permissionsetting.util.OSUtil;
import com.wei.permissionsetting.util.PackageUtil;

/**
 * @author: WEI
 * @date: 2018/1/15
 */

public class PermissionGuideGenerator
{
    public static IPermissionGuideStrategy generateGuideStratagy(Context paramContext)
    {
        return generateGuideStratagy(paramContext, false);
    }

    public static IPermissionGuideStrategy generateGuideStratagy(Context paramContext, boolean paramBoolean)
    {
        if ((OSUtil.isMiuiV6()) || (OSUtil.isMiuiV7()) || (OSUtil.isMiuiV8()))
        {
            return new MiuiV6PermissionGuideStrategy(paramContext, paramBoolean);
        }
        if (OSUtil.isMiuiV9())
        {
            return new MiuiV6PermissionGuideStrategy(paramContext, paramBoolean);
        }
        if (PackageUtil.isPackageInstalled(paramContext, "com.meizu.safe"))
        {
            return new MeizuPermissionGuideStrategy(paramContext, paramBoolean);
        }
        return new DefaultStrategy(paramContext);
    }
}
