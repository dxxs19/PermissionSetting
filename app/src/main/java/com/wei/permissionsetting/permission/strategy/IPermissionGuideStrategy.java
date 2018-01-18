package com.wei.permissionsetting.permission.strategy;

/**
 * @author: WEI
 * @date: 2018/1/15
 */

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.wei.permissionsetting.R;
import com.wei.permissionsetting.permission.PermissionAccessibilityService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class  IPermissionGuideStrategy
{
    protected final String TAG = getClass().getSimpleName();
    public static final int ACTION_CLICK;
    public static final int GLOBAL_ACTION_BACK;
    public static final int ACTION_SCROLL_FORWARD;
    public Context mContext = null;
    protected static Handler sHandler = new Handler();

    static {
        ACTION_CLICK = AccessibilityNodeInfo.ACTION_CLICK;
        ACTION_SCROLL_FORWARD = AccessibilityNodeInfo.ACTION_SCROLL_FORWARD;
        GLOBAL_ACTION_BACK = AccessibilityService.GLOBAL_ACTION_BACK;
    }

    public IPermissionGuideStrategy(Context paramContext) {
        mContext = paramContext;
    }

    /**
     * 跳到自启动设置界面
     */
    public abstract void actionAutoBootPermission();

    /**
     * 跳转到电池优化设置界面
     */
    public abstract void actionPowerPermisssion();

    /**
     * 跳转到权限管理界面
     */
    public abstract void actionPermissionsEditor();

    /**
     * 获取权限列表
     * @param resId
     * @return
     */
    public List<String> getPermissionList(@ArrayRes int resId)
    {
        List<String> mPermissionList = new ArrayList<>();
        String[] permissionArray = mContext.getResources().getStringArray(resId);
        for (int i = 0; i < permissionArray.length; i ++)
        {
            mPermissionList.add(permissionArray[i]);
        }
        return mPermissionList;
    }

    /**
     * 根据文字查找所有结点
     * @param root
     * @param targetTxt
     * @return
     */
    public List<AccessibilityNodeInfo> getNodeInfosByText(AccessibilityNodeInfo root, @NonNull String... targetTxt) {
        if (root == null) {
            Log.e(TAG, "root == null");
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfos = new ArrayList<>();
        int len = targetTxt.length;
        for (int i = 0; i < len; i ++)
        {
            List<AccessibilityNodeInfo> nodeInfos1 = root.findAccessibilityNodeInfosByText(targetTxt[i]);
            if (nodeInfos1 != null && nodeInfos1.size() > 0)
            {
                nodeInfos.addAll(nodeInfos1);
            }
        }
        return (nodeInfos != null && nodeInfos.size() > 0) ? nodeInfos : null;
    }

    /**
     * 根据viewId查找所有结点
     * @param root
     * @param viewId
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public List<AccessibilityNodeInfo> getNodeInfosById(AccessibilityNodeInfo root, String viewId)
    {
        if (root == null)
        {
            Log.e(TAG, "root == null");
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfos = root.findAccessibilityNodeInfosByViewId(viewId);
        Log.e(TAG, "getNodeInfosById, nodeInfos = " + ( nodeInfos == null ? 0 : nodeInfos.size()) );
        return (nodeInfos != null && nodeInfos.size() > 0) ? nodeInfos : null;
    }

    /**
     * 遍历查找到可以点击的父结点
     * @param accessibilityNodeInfo
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public AccessibilityNodeInfo getClickableParent(AccessibilityNodeInfo accessibilityNodeInfo)
    {
        if (accessibilityNodeInfo.isClickable())
        {
            return accessibilityNodeInfo;
        }
        AccessibilityNodeInfo parent = accessibilityNodeInfo.getParent();
        while (!parent.isClickable())
        {
            getClickableParent(parent);
        }
        return parent;
    }


    /**
     * 是否存在目标结点
     *
     * @param paramAccessibilityService
     * @param targetTxt
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean isTargetNodeExists (AccessibilityService paramAccessibilityService, String targetTxt)
    {
        AccessibilityNodeInfo rootInActiveWindow = paramAccessibilityService.getRootInActiveWindow();
        if (rootInActiveWindow != null) {
            List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByText(targetTxt);
            return nodeInfos != null && nodeInfos.size() > 0;
        }
        return false;
    }

    /**
     * 模拟全局事件处理。如，返回
     * @param action
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void performGlobalAction(int action)
    {
        PermissionAccessibilityService.getService().performGlobalAction(action);
    }

    /**
     * 模拟结点事件处理。如，滑动listview，点击结点......
     * @param nodeInfo
     * @param action
     */
    public void performAction(AccessibilityNodeInfo nodeInfo, int action)
    {
        nodeInfo.performAction(action);
    }

    /**
     * 向下滑动滚动条
     * @param rootInActiveWindow
     * @param viewId
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void performScrollForward(AccessibilityNodeInfo rootInActiveWindow, String viewId)
    {
        List<AccessibilityNodeInfo> listViewNodes = getNodeInfosById(rootInActiveWindow,viewId);
        if (listViewNodes != null) {
            AccessibilityNodeInfo nodeInfo = listViewNodes.get(0);
            performAction(nodeInfo, ACTION_SCROLL_FORWARD);
        }
    }

    /**
     * 跳转到app设置界面
     */
    public void actionAppDetailsSetting()
    {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
        mContext.startActivity(intent);
    }

    /**
     * 事件回调处理
     * @param paramAccessibilityEvent
     * @param paramAccessibilityService
     */
    public abstract void handleAccessbilityEvent(AccessibilityEvent paramAccessibilityEvent, AccessibilityService paramAccessibilityService);

    /**
     * 服务开启后进行相关配置
     * @param paramAccessibilityService
     */
    public abstract void configAccessbility(AccessibilityService paramAccessibilityService);
}
