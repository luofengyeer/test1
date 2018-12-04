package com.cmcc.internalcontact.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.utils.view.CustomDialog;
import com.xm.permissions.OnPermissionResult;
import com.xm.permissions.PermissionUtils;
import com.xm.permissions.PermissionsManager;

import java.util.Arrays;
import java.util.List;

/**
 * 检查权限的工具类
 * <p/>
 * Created by wangkezhi on 17/12/27.
 */
public class PermissionsUtils {
    //启动所需权限
    public final static String[] LAUNCH_PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SYNC_SETTINGS,
            Manifest.permission.WRITE_SYNC_SETTINGS,
            Manifest.permission.GET_ACCOUNTS,
            "android.permission.AUTHENTICATE_ACCOUNTS"
    };

    //拍照所需权限
    public final static String[] TAKEPHOTO_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    //相册所需权限
    public final static String[] IMAGESELECT_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //文件选择所需权限
    public final static String[] FILESELECT_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //录视频所需权限
    public final static String[] TAKEVIDEO_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    //位置所需权限
    public final static String[] LOCATION_PERMISSION = {Manifest.permission.ACCESS_COARSE_LOCATION
    };

    //录音所需权限
    public final static String[] RECORD_AUDIO_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
    };

    //来电提示框所需权限
    public final static String[] INCOME_CALL_PERMISSION = {Manifest.permission.SYSTEM_ALERT_WINDOW
//            , Manifest.permission.READ_PHONE_STATE
//            , Manifest.permission.PROCESS_OUTGOING_CALLS
    };


    /**
     * 是否包含权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean havePermission(Context context, String permission) {
        return PermissionUtils.hasPermission(context, Arrays.asList(permission));
    }

    /**
     * 是否包含权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean havePermission(Context context, String... permission) {
        return PermissionUtils.hasPermission(context, Arrays.asList(permission));
    }

    /**
     * 是否包含权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean havePermission(Context context, List<String> permission) {
        return PermissionUtils.hasPermission(context, permission);
    }

    /**
     * 是否包含权限
     *
     * @param activity
     * @param requestCode
     * @param onResultListener
     * @param permission
     * @return
     */
    public static boolean havePermissionWithRequest(final Activity activity, int requestCode, final OnResultListener onResultListener, String... permission) {

        boolean result = PermissionUtils.hasPermission(activity, Arrays.asList(permission));
        if (result) {
            return true;
        }

        PermissionsUtils.permissionRequest(activity, requestCode, new PermissionsUtils.OnPermissionListener() {

            @Override
            public void onSucceed(int requestCode) {
                if (onResultListener != null) {
                    onResultListener.onSucceed();
                }
            }

            @Override
            public void onFailed(int requestCode) {
                PermissionsUtils.showDefaultPermissionDialog(activity, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onResultListener != null) {
                            onResultListener.onDialogCancel();
                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PermissionsUtils.goSettingActivity(activity, 205);
                    }
                });
            }
        }, permission);
        return result;
    }

    /**
     * 跳转设置界面
     *
     * @param activity
     * @param requestCode
     */
    public static void goSettingActivity(@NonNull Activity activity, int requestCode) {
        PermissionUtils.goSettingActivity(activity, requestCode);
    }

    /**
     * 请求permission
     *
     * @param activity
     * @param requsetCode
     * @param onPermissionListener
     * @param permissions
     */
    public static void permissionRequest(final Activity activity, final int requsetCode, final OnPermissionListener onPermissionListener, final String... permissions) {
        permissionRequest(activity, requsetCode, onPermissionListener, Arrays.asList(permissions));
    }

    public static void permissionRequest(final Activity activity, final int requsetCode, final OnPermissionListener onPermissionListener, final List<String> permissionList) {
        String[] permissions = new String[permissionList.size()];
        permissionList.toArray(permissions);
        PermissionsManager permissionsManager = new PermissionsManager(activity);
        permissionsManager.setPermissions(permissions)
                .setOnPermissionResult(new OnPermissionResult() {
                    @Override
                    public void onSuccess() {
                        if (onPermissionListener != null) {
                            onPermissionListener.onSucceed(requsetCode);
                        }

                    }

                    @Override
                    public void onFailed(boolean onRationale) {
                        if (onPermissionListener != null) {
                            onPermissionListener.onFailed(requsetCode);
                        }
                    }
                })
                .request();
    }

    /**
     * 默认申请权限对话框
     *
     * @param context
     * @param save
     * @param cancel
     */
    public static void showDefaultPermissionDialog(Context context, View.OnClickListener save, View.OnClickListener cancel) {
        showPermissionDialog(context, context.getString(R.string.common_permission_title), context.getString(R.string.common_permission_msg), context.getString(R.string.common_permission_pos), context.getString(R.string.common_permission_nega), save, cancel);
    }

    /**
     * 创建对话框
     *
     * @param context
     * @param title
     * @param msg
     * @param posBtn
     * @param negaBtn
     * @param save
     * @param cancel
     */
    public static void showPermissionDialog(Context context, String title, String msg, String posBtn, String negaBtn, final View.OnClickListener save, final View.OnClickListener cancel) {
        final CustomDialog draftDialog = new CustomDialog(context);
        if (!TextUtils.isEmpty(title)) {
            draftDialog.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            draftDialog.setMessage(msg);
        }
        draftDialog.setOkButton(negaBtn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draftDialog.dismiss();
                if (cancel != null) {
                    cancel.onClick(v);
                }
            }
        });
        draftDialog.setCancelButton(posBtn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draftDialog.dismiss();
                if (save != null) {
                    save.onClick(v);
                }
            }
        });
        draftDialog.show();
    }

    /**
     * 请求权限并跳转设置
     *
     * @param activity
     * @param requestCode
     * @param requestSettingCode
     * @param onResultListener
     * @param permission
     */
    public static void request2GoSetting(final Activity activity, int requestCode, final int requestSettingCode, final OnResultListener onResultListener, String... permission) {
        if (!PermissionsUtils.havePermission(activity, permission)) {
            //没有权限时,申请权限
            PermissionsUtils.permissionRequest(activity, requestCode, new PermissionsUtils.OnPermissionListener() {
                @Override
                public void onSucceed(int requestCode) {
                    if (onResultListener != null) {
                        onResultListener.onSucceed();
                    }
                }

                @Override
                public void onFailed(int requestCode) {
                    PermissionsUtils.showDefaultPermissionDialog(activity, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onResultListener != null) {
                                onResultListener.onDialogCancel();
                            }
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PermissionsUtils.goSettingActivity(activity, requestSettingCode);
                        }
                    });
                }
            }, permission);
        } else {
            if (onResultListener != null) {
                onResultListener.onSucceed();
            }
        }
    }

    /**
     * 业务封装回调接口
     */
    public interface OnResultListener {
        /**
         * 业务成功回调
         */
        void onSucceed();

        /**
         * 对话框取消回调
         */
        void onDialogCancel();
    }

    /**
     * 权限回调接口
     */
    public interface OnPermissionListener {
        void onSucceed(int requestCode);

        void onFailed(int requestCode);
    }
}
