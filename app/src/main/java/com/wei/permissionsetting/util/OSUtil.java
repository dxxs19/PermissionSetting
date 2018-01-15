package com.wei.permissionsetting.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: WEI
 * @date: 2018/1/15
 */

public class OSUtil
{
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
}
