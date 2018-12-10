package com.cmcc.internalcontact.utils.callerfloat;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.cmcc.internalcontact.model.db.PersonModel;
import com.cmcc.internalcontact.utils.Constant;
import com.cmcc.internalcontact.utils.SharePreferencesUtils;

/**
 * 系统悬浮框管理类
 *
 * @author jarlen
 */
public class FloatWindowManager implements View.OnClickListener {
    private static final String TAG = FloatWindowManager.class.getSimpleName();
    private static volatile FloatWindowManager instance;

    private boolean isWindowDismiss = true;
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams mParams = null;
    private CallerIdsFloatView floatView = null;

    public static FloatWindowManager getInstance() {
        if (instance == null) {
            synchronized (FloatWindowManager.class) {
                if (instance == null) {
                    instance = new FloatWindowManager();
                }
            }
        }
        return instance;
    }

    public boolean isWindowDismiss() {
        return isWindowDismiss;
    }

    public void showCallerFloatWindow(Context context, String callerIds, PersonModel personBean) {
        if (!isWindowDismiss) {
            Log.e(TAG, "view is already added here");
            return;
        }

        isWindowDismiss = false;
        if (windowManager == null) {
            windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }

        mParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        mParams.format = PixelFormat.TRANSPARENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //init location
        mParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;//坐标原点的位置
        floatView = new CallerIdsFloatView(context);

        mParams.x = floatView.getInfoX();
        mParams.y = floatView.getInfoY();

        floatView.setParams(mParams);
        floatView.setIsShowing(true);

        floatView.setCallerAvatar(Constant.BASE_AVATRE_URL + personBean.getHeadPic());
        floatView.setCallerName(personBean.getUsername());
        floatView.setCallerPhone(callerIds);
        floatView.setOnCloseClickListener(this);

        try {
            windowManager.addView(floatView, mParams);
        } catch (WindowManager.BadTokenException badTokenException) {
            Log.e(TAG, "window add view exception: " + badTokenException.getMessage());
            SharePreferencesUtils sharePreferencesUtils = SharePreferencesUtils.getInstance();
            sharePreferencesUtils.setBoolean(Constant.SWITCH_INCOME_STATUE, false);
            dismissFloatWindow();
        } catch (Exception exception) {
            exception.printStackTrace();
            dismissFloatWindow();
        }
    }

    public void dismissFloatWindow() {
        isWindowDismiss = true;
        if (floatView != null) {
            floatView.setIsShowing(false);
            if (windowManager != null) {
                try {
                    windowManager.removeViewImmediate(floatView);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        dismissFloatWindow();
    }
}
