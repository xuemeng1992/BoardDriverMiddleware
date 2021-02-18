package com.rocktech.boarddriver.tools;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rocktech.boarddriver.R;


/**
 * Created by Tony.Fan on 2017/8/4.
 */

public class DialogManager {
    private static MaterialDialog progressDialog;
    private static MaterialDialog.Builder tipsDialogBuilder;
    private static MaterialDialog tipsDialog;


    public static MaterialDialog showDialog(@NonNull Context context) {
        return showDialog(context, null);
    }

    public static MaterialDialog showDialog(@NonNull Context context, boolean canCancel) {
        return showDialog(context, canCancel, null);
    }

    public static MaterialDialog showDialog(@NonNull Context context, DialogInterface
            .OnCancelListener listener) {
        return showDialog(context, true, listener);
    }

    public static MaterialDialog showDialog(@NonNull Context context, Boolean canCancel, DialogInterface
            .OnCancelListener listener) {
        return showProgressDialog(context, canCancel, listener);
    }

    public static MaterialDialog showProgressDialog(@NonNull Context context, boolean canCancelable, DialogInterface
            .OnCancelListener listener) {
        if (isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .show();
        return progressDialog;
    }

    public static MaterialDialog showSetupBoardTipsDialog(Context context, String string, String s1, String s2, MaterialDialog.SingleButtonCallback callback) {
        tipsDialogBuilder = new MaterialDialog.Builder(context);
        tipsDialogBuilder.title(R.string.progress_dialog);
        tipsDialogBuilder.content("设置" + string + ": 开启温度 " + s1 + "℃      关闭温度 " + s2 + "℃  ？");
        tipsDialogBuilder.positiveText("确定");
        tipsDialogBuilder.titleGravity(GravityEnum.CENTER);
        tipsDialogBuilder.buttonsGravity(GravityEnum.START);
        tipsDialogBuilder.negativeText("取消");
        tipsDialogBuilder.cancelable(false);
        tipsDialog = tipsDialogBuilder.build();
        tipsDialog.show();
        tipsDialogBuilder.onAny(callback);
        return tipsDialog;
    }


    public static void dismissProgressDialog() {
        if (progressDialog == null) {
            return;
        }
        if (isShowing()) {
            try {
                progressDialog.dismiss();
                progressDialog = null;
            } catch (Exception e) {
                //捕获异常
            }
        }
    }

    public static boolean isShowing() {
        return (progressDialog != null && progressDialog.isShowing());
    }

}
