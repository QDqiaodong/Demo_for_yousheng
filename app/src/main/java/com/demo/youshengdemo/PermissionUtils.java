package com.demo.youshengdemo;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;


public class PermissionUtils {

    /**
     * 判断是否具备所有权限
     *
     * @param permissions 所有权限
     * @return true 具有所有权限  false没有具有所有权限，此时包含未授予的权限
     */
    public static boolean isHasPermissions(Context context,String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        for (String permission : permissions) {
            if (!isHasPermission(context,permission))
                return false;
        }
        return true;
    }

    /**
     * 判断该权限是否已经被授予
     *
     * @param permission permission
     * @return true 已经授予该权限 ，false未授予该权限
     */
    public static boolean isHasPermission(Context context, String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(context, permission) ==
                        PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 请求权限,经测试发现TabActivity管理Activity时，在Activity中请求权限时需要传入父Activity对象，即TabActivity对象
     * 并在TabActivity管理Activity中重写onRequestPermissionsResult并分发到子Activity，否则回调不执行  。
     * TabActivity回调中调用
     * getLocalActivityManager().getCurrentActivity().onRequestPermissionsResult(requestCode, permissions, grantResults);
     * 分发到子Activity
     *
     * @param object      Activity or Fragment
     * @param requestCode 请求码
     * @param permissions 请求权限
     */
    public static void requestPermissions(Context context,Object object,
            int requestCode, String... permissions) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String permission : permissions) {
            if (!isHasPermissions(context,permission)) {
                arrayList.add(permission);
            }
        }
        if (arrayList.size() > 0) {
            if (object instanceof Activity) {
                Activity activity = (Activity) object;
                Activity activity1 = activity.getParent() != null &&
                        activity.getParent() instanceof TabActivity ? activity.getParent() : activity;
                ActivityCompat.requestPermissions(activity1, arrayList.toArray(new String[]{}), requestCode);
            } else if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                //当Fragment嵌套Fragment时使用getParentFragment(),然后在父Fragment进行分发，否则回调不执行
                Fragment fragment1 = fragment.getParentFragment() != null ? fragment.getParentFragment() : fragment;
                fragment1.requestPermissions(arrayList.toArray(new String[]{}), requestCode);
            } else {
                throw new RuntimeException("the object must be Activity or Fragment");
            }
        }
    }
}
