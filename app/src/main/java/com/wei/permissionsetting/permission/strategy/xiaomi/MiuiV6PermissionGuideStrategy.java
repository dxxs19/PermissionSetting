package com.wei.permissionsetting.permission.strategy.xiaomi;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.wei.permissionsetting.R;
import com.wei.permissionsetting.permission.strategy.IPermissionGuideStrategy;
import com.wei.permissionsetting.util.OSUtil;
import com.wei.permissionsetting.util.PackageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 小米权限辅助设置策略
 * @author: WEI
 * @date: 2018/1/15
 */

public class MiuiV6PermissionGuideStrategy extends IPermissionGuideStrategy
{
    private static final String APP_PERMISSION_ACTIVITY_NAME = "com.miui.permcenter.permissions.PermissionsEditorActivity";
    private static final String APP_V813_PERMISSION_ACTIVITY_NAME = "com.miui.permcenter.permissions.AppPermissionsEditorActivity";
    private static final String APP_V91_PERMISSION_ACTIVITY_NAME = "com.miui.appmanager.AppManagerMainActivity";
    private static final String MIUI_SECERITYENTER = "com.miui.securitycenter";
    private static final String[] MIUI_PACKAGES = new String[] { "com.miui.securitycenter", "com.android.packageinstaller",
            "com.android.settings", "com.miui.powerkeeper" };
    private boolean mAutoGuide;
    private boolean mDetectTimeout = false;
    private Handler mH = null;
    private String mMiuiVersion;
    private VERSION mVersion;
    private List<String> mPermissionList = new ArrayList<>();

    private enum VERSION
    {
        COMMON,  SPECIAL,  SPECIAL_2,  SPECIAL_9_1_3;
        VERSION() {}
    }

    public MiuiV6PermissionGuideStrategy(Context paramContext, boolean paramBoolean) {
        super(paramContext);
        mPermissionList = getPermissionList(R.array.Xiaomi_miuiV6_permissions);
        if (Build.VERSION.SDK_INT == 25)
        {
            this.mDetectTimeout = true;
            this.mH = new Handler();
        }
        this.mMiuiVersion = Build.VERSION.INCREMENTAL;
        this.mAutoGuide = paramBoolean;
        String versionName;
        if (this.mMiuiVersion.startsWith("V9.1.3"))
        {
            this.mVersion = VERSION.SPECIAL_9_1_3;
            return;
        }
        try
        {
            versionName = PackageUtil.getVersionName(paramContext, "com.miui.securitycenter");
            if ((versionName.startsWith("2.0")) || (versionName.startsWith("2.1")) || (versionName.startsWith("2.2")) || (versionName.startsWith("2.3")))
            {
                this.mVersion = VERSION.SPECIAL_2;
                return;
            }
        }
        catch (Exception e)
        {
            if ((this.mMiuiVersion.startsWith("6.9")) || (this.mMiuiVersion.startsWith("V8.1.5")) || (this.mMiuiVersion.startsWith("V8.1.3")))
            {
                this.mVersion = VERSION.SPECIAL;
                return;
            }
            if ((this.mMiuiVersion.startsWith("V8.1.1")) || (this.mMiuiVersion.startsWith("V8.2")) || (this.mMiuiVersion.startsWith("7.1")))
            {
                this.mVersion = VERSION.SPECIAL_2;
                return;
            }
            this.mVersion = VERSION.COMMON;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void configAccessbility(AccessibilityService paramAccessibilityService) {
        AccessibilityServiceInfo localAccessibilityServiceInfo = new AccessibilityServiceInfo();
        localAccessibilityServiceInfo.packageNames = MIUI_PACKAGES;
        localAccessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        localAccessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        localAccessibilityServiceInfo.notificationTimeout = 1000L;
        localAccessibilityServiceInfo.flags |= 0x10;
        paramAccessibilityService.setServiceInfo(localAccessibilityServiceInfo);
        paramAccessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        mIsFinish = false;
        // 跳到自启动设置界面
        actionAutoBootPermission();
//        showFloatWindow(paramAccessibilityService);
    }

    private boolean mIsFinish = false;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void handleAccessbilityEvent(AccessibilityEvent paramAccessibilityEvent, AccessibilityService paramAccessibilityService) {
        int eventType = paramAccessibilityEvent.getEventType();
        String currentPkg = (String) paramAccessibilityEvent.getPackageName();
        String currentClz = (String) paramAccessibilityEvent.getClassName();
        Log.e(TAG, "eventType = " + eventType + ", pkgname = " + currentPkg + ", clsname = " + currentClz);
        AccessibilityNodeInfo rootInActiveWindow;
        if (((eventType == 4096) || (eventType == 2048) || (eventType == 32)) && (paramAccessibilityEvent.getPackageName() != null))
        {
            rootInActiveWindow = paramAccessibilityService.getRootInActiveWindow();
            if (rootInActiveWindow != null)
            {
                final String appName = mContext.getResources().getString(R.string.app_name);
                if (MIUI_PACKAGES[0].equals(currentPkg))
                {
                    if (mIsFinish)
                    {
                        performGlobalAction(GLOBAL_ACTION_BACK);
                        mIsFinish = false;
                        removeFloatWindow();
                        return;
                    }
                    if ("com.miui.permcenter.autostart.AutoStartManagementActivity".equals(currentClz))
                    { // 开启自启动
                        while (!isTargetNodeExists(paramAccessibilityService, appName)) {
                            List<AccessibilityNodeInfo> listViewNodes = getNodeInfosById(rootInActiveWindow,
                                    "com.miui.securitycenter:id/list_view");
                            if (listViewNodes != null) {
                                AccessibilityNodeInfo nodeInfo = listViewNodes.get(0);
                                nodeInfo.performAction(ACTION_SCROLL_FORWARD);
                            }
                        }
                        List<AccessibilityNodeInfo> nodeInfos = getNodeInfosByText(rootInActiveWindow, appName);
                        if (null != nodeInfos && nodeInfos.size() > 0) {
                            recycle(nodeInfos.get(0));
                            actionPowerPermisssion();
                        }
                    }
                    else if ("com.miui.permcenter.autostart.AutoStartDetailManagementActivity".equals(currentClz))
                    {
                        // 自启详情
                        List<AccessibilityNodeInfo> nodeInfos = getNodeInfosByText(rootInActiveWindow, "允许");
                        if (nodeInfos != null)
                        {
                            for (AccessibilityNodeInfo nodeInfo:nodeInfos) {
                                recycle(nodeInfo);
                            }
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    }
                    else if ("com.miui.permcenter.permissions.PermissionsEditorActivity".equals(currentClz))
                    { // 权限管理
                        if (mPermissionList.size() == 0)
                        {
                            Log.e(TAG, "finish");
                            mIsFinish = true;
                            goBack();
                            return;
                        }
                        String text = mPermissionList.get(0);
                        Log.e(TAG, "nodes size = " + mPermissionList.size() + ", text : " + text);
                        if (null != text)
                        {
                            List<AccessibilityNodeInfo> nodeInfos = getNodeInfosByText(rootInActiveWindow, text);
                            if (nodeInfos != null)
                            {
                                AccessibilityNodeInfo nodeInfo = nodeInfos.get(0);
                                performAction(getClickableParent(nodeInfo), AccessibilityNodeInfo.ACTION_CLICK);
                                mPermissionList.remove(text);
                            }
                        }
                    }
                    else if ("android.widget.ListView".equals(currentClz))
                    {
                        List<AccessibilityNodeInfo> nodeInfos = getNodeInfosByText(rootInActiveWindow, "允许");
                        if (nodeInfos != null)
                        {
                            AccessibilityNodeInfo nodeInfo = nodeInfos.get(0);
                            performAction( getClickableParent( nodeInfo ), AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }
                else if (MIUI_PACKAGES[3].equals(currentPkg))
                {   // 保持后台运行，耗电
                    if (mIsFinish)
                    {
                        performGlobalAction(GLOBAL_ACTION_BACK);
                        return;
                    }
                    if ("com.miui.powerkeeper.ui.HiddenAppsContainerManagementActivity".equals(currentClz))
                    {  // 智能省电界面
                        while (!isTargetNodeExists(paramAccessibilityService, appName)) {
                            List<AccessibilityNodeInfo> listViewNodes = rootInActiveWindow.
                                    findAccessibilityNodeInfosByViewId("com.miui.powerkeeper:id/apps_list");
                            if (listViewNodes != null && listViewNodes.size() > 0) {
                                AccessibilityNodeInfo nodeInfo = listViewNodes.get(0);
                                nodeInfo.performAction(ACTION_SCROLL_FORWARD);
                            }
                        }
                        List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByText(appName);
                        if (nodeInfos != null && nodeInfos.size() > 0) {
                            AccessibilityNodeInfo nodeInfo = getClickableParent(nodeInfos.get(0));
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                    else if ("com.miui.powerkeeper.ui.HiddenAppsConfigActivity".equals(currentClz))
                    {    // 后台配置界面
                        List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByText("无限制");
                        if (null != nodeInfos && nodeInfos.size() > 0) {
                            AccessibilityNodeInfo nodeInfo = getClickableParent(nodeInfos.get(0));
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            actionAppDetailsSetting();
                        }
                    }
                }
                else if (MIUI_PACKAGES[2].equals(currentPkg))
                {
                    if (mIsFinish)
                    {
                        performGlobalAction(GLOBAL_ACTION_BACK);
                        return;
                    }
                    if ("com.android.settings.applications.InstalledAppDetailsTop".equals(currentClz))
                    {
                        while (!isTargetNodeExists(paramAccessibilityService, "权限管理")) {
                            List<AccessibilityNodeInfo> listViewNodes = rootInActiveWindow.
                                    findAccessibilityNodeInfosByViewId("android:id/list");
                            if (listViewNodes != null && listViewNodes.size() > 0) {
                                AccessibilityNodeInfo nodeInfo = listViewNodes.get(0);
                                nodeInfo.performAction(ACTION_SCROLL_FORWARD);
                            }
                        }
                        List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByText("权限管理");
                        if (nodeInfos != null && nodeInfos.size() > 0) {
                            AccessibilityNodeInfo nodeInfo = getClickableParent(nodeInfos.get(0));
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void goBack()
    {
        if (mIsFinish)
        {
            performGlobalAction(GLOBAL_ACTION_BACK);
            return;
        }
    }

    /**
     * 跳到自启动设置界面
     */
    @Override
    public void actionAutoBootPermission()
    {
        try
        {
            Intent intent = new Intent();
            intent.setClassName(MIUI_PACKAGES[0], "com.miui.permcenter.autostart.AutoStartManagementActivity");
            intent.putExtra("packageName", mContext.getPackageName());
            mContext.startActivity(intent);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 跳转到电池优化设置界面
     */
    @Override
    public void actionPowerPermisssion() {
        try
        {
            Intent localIntent1 = new Intent();
            localIntent1.setClassName(MIUI_PACKAGES[3], "com.miui.powerkeeper.ui.HiddenAppsContainerManagementActivity");
            mContext.startActivity(localIntent1);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 跳转到权限管理界面
     */
    @Override
    public void actionPermissionsEditor() {
        try
        {
            Intent intent = new Intent();
            intent.setClassName(MIUI_PACKAGES[0], "com.miui.permcenter.permissions.PermissionsEditorActivity");
            mContext.startActivity(intent);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void recycle(AccessibilityNodeInfo appNodeInfo) {
        AccessibilityNodeInfo parentNodeInfo = appNodeInfo.getParent();
        int childCount = parentNodeInfo.getChildCount();
        Log.e(TAG, "parentNodeInfo : " + parentNodeInfo.getClassName() + ", childCount : " + childCount);
        if (childCount <= 1) {
            recycle(parentNodeInfo);
        } else {
            final AccessibilityNodeInfo nodeInfo = parentNodeInfo.getChild(childCount - 1);
            Log.e(TAG, "nodeInfo.getClassName() : " + nodeInfo.getClassName());
            if ("android.widget.CheckBox".equals(nodeInfo.getClassName())) {
                Log.e(TAG, "准备开启自启动");
                if (!nodeInfo.isChecked()) {
                    Log.e(TAG, nodeInfo.isChecked() + "");
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }
}
