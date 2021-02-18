package com.rocktech.boarddriver.tools;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {
    private Activity mActivity;
    private int mReqCode;
    private CallBack mCallBack;

    //定义一个回调接口
    public static interface CallBack {
        //接受
        void grantAll();

        //拒绝
        void denied();
    }

    //定义一个构造函数
    public PermissionUtils(Activity activity) {
        this.mActivity = activity;
    }

    //定义请求权限的方法
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void request(List<String> needPermissions, int reqCode, CallBack callBack) {

        mReqCode = reqCode;
        mCallBack = callBack;

        //因为6.0之后才需要动态权限申请
        if (Build.VERSION.SDK_INT < 23) {
            //6.0之前是默认获取全部权限
            callBack.grantAll();
            return;
        }

        //判空，并抛出异常
        if (mActivity == null) {
            throw new IllegalArgumentException("activity is null.");
        }


        //将需要申请的权限，因为有些权限已经赋予
        List<String> reqPermission = new ArrayList<>();
        for (String permission : needPermissions) {
            if (mActivity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                reqPermission.add(permission);
            }
        }

        //如果没有要授予的权限，则直接返回
        if (reqPermission.isEmpty()) {
            callBack.grantAll();
            return;
        }

        //真正开始申请
        mActivity.requestPermissions(reqPermission.toArray(new String[]{}), reqCode);

    }

    //处理权限返回的回调
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == mReqCode) {
            boolean grantAll = true;
            //遍历每一个授权结果
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    grantAll = false;
                    Toast.makeText(mActivity, permissions[i] + "未授权", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

            if (grantAll) {
                mCallBack.grantAll();
            } else {
                mCallBack.denied();
            }
        }
    }

}
