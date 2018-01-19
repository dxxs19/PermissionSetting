package com.wei.permissionsetting.util;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author: WEI
 * @date: 2018/1/15
 */

public class OSUtil
{
    private static final String TAG = "OSUtil";
    static Object localObject1;
    static Object localObject2 = null;

    public static boolean isMiuiV6()
    {
        return (isMiui()) && ("V6".equalsIgnoreCase(getMiuiVersionName()));
    }

    public static boolean isMiuiV7()
    {
        return (isMiui()) && ("V7".equalsIgnoreCase(getMiuiVersionName()));
    }

    public static boolean isMiuiV8()
    {
        return (isMiui()) && ("V8".equalsIgnoreCase(getMiuiVersionName()));
    }

    public static boolean isMiuiV9()
    {
        return (isMiui()) && ("V9".equalsIgnoreCase(getMiuiVersionName()));
    }

    public static boolean isMiui()
    {
        return android.os.Build.MANUFACTURER.equalsIgnoreCase("Xiaomi");
    }

    private static String getMiuiVersionName()
    {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }

    /**
     * 获取设备品牌
     * @return
     */
    public static String getMobileType(){
        return Build.MANUFACTURER;
    }

    /**
     * 判断悬浮窗口权限是否打开
     * @param context
     * @return
     */
    public static boolean checkAlertWindowsPermission(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = 24;
            arrayOfObject1[1] = Binder.getCallingUid();
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1));
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }

    public static boolean checkPermissionAllowed(Context context)
    {
        boolean allowed = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int result = appOpsManager.checkOpNoThrow("24", Binder.getCallingUid(), context.getPackageName());
//            int result = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW, Binder.getCallingUid(), context.getPackageName());
            if (result == AppOpsManager.MODE_ALLOWED)
            {
                allowed = true;
            }
            else if (result == AppOpsManager.MODE_IGNORED)
            {
                Log.e(TAG, "AppOpsManager.MODE_IGNORED");
            }
            else if (result == AppOpsManager.MODE_ERRORED)
            {
                Log.e(TAG, "AppOpsManager.MODE_ERRORED");
            }
            else if (result == 4)
            {
                Log.e(TAG, "4");
            }
        }
        return allowed;
    }

    public static int checkOp(Context context, int op){
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19){
            Object object = context.getSystemService("appops");
            Class c = object.getClass();
            try {
                Class[] cArg = new Class[3];
                cArg[0] = int.class;
                cArg[1] = int.class;
                cArg[2] = String.class;
                Method lMethod = c.getDeclaredMethod("checkOp", cArg);
                return (Integer) lMethod.invoke(object, op, Binder.getCallingUid(), context.getPackageName());
            } catch(NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
}
