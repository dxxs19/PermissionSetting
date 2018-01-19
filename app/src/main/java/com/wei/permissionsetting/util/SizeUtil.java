package com.wei.permissionsetting.util;

import android.content.Context;
import android.content.res.Resources;

/**
 * @author: WEI
 * @date: 2018/1/19
 */

public class SizeUtil
{
    public static int getStatusBarHeight(Context context)
    {
        Resources localResources = context.getResources();
        int i = 0;
        int j = localResources.getIdentifier("status_bar_height", "dimen", "android");
        if (j > 0) {
            i = localResources.getDimensionPixelSize(j);
        }
        return i;
    }
}
