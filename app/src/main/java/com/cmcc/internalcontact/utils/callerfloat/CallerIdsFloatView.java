package com.cmcc.internalcontact.utils.callerfloat;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cmcc.internalcontact.R;


/**
 * 来电浮框View
 */
public class CallerIdsFloatView extends FrameLayout {

    //保存移动的位置
    private String PHONE_ALRET_VIEW = "PHONE_ALRET_VIEW";
    private String PHONE_ALRET_VIEW_INFO_X = "PHONE_ALRET_VIEW_INFO_X";
    private String PHONE_ALRET_VIEW_INFO_Y = "PHONE_ALRET_VIEW_INFO_Y";

    public static final int VIEW_INFO_VALUE_DEFAULT = -1;

    private static final String TAG = "AVCallFloatView";
    private boolean isShowing = false;
    private static WindowManager wManager = null;
    private static WindowManager.LayoutParams mParams = null;
    private Context mContext;

    private float lastX, lastY;

    private View floatView;

    int screenWidth;
    int screenHeight;

    public CallerIdsFloatView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        wManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wManager.getDefaultDisplay().getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        floatView = inflater.inflate(R.layout.layout_incoming_float, null);
        addView(floatView);
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setIsShowing(boolean isShowing) {
        this.isShowing = isShowing;
    }

    public void setOnCloseClickListener(OnClickListener listener) {
        ImageView closeIv = floatView.findViewById(R.id.iv_close);
        closeIv.setOnClickListener(listener);
    }

    public void setCallerName(String callerName) {
        TextView userNameTv = floatView.findViewById(R.id.tv_user_name);
        userNameTv.setText(callerName);
    }

    public void setCallerAvatar(String avatarUrl, String name, long id) {
        ImageView userAvatarIv = floatView.findViewById(R.id.iv_user_avatar);
        Glide.with(mContext).load(avatarUrl).into(userAvatarIv);
    }

    public void setCallerPhone(String callerPhone) {
        TextView userPhoneTv = floatView.findViewById(R.id.tv_user_phone);
        userPhoneTv.setText(callerPhone);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                mParams.x += (int) (x - lastX) / 5; // 减小偏移量,防止过度抖动
                mParams.y += (int) (y - lastY) / 5; // 减小偏移量,防止过度抖动
                wManager.updateViewLayout(this, mParams);
                break;
            case MotionEvent.ACTION_UP:
                setInfoX(mParams.x);
                setInfoY(mParams.y);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public int getInfoX() {
        return mContext.getSharedPreferences(PHONE_ALRET_VIEW, Context.MODE_PRIVATE).getInt(PHONE_ALRET_VIEW_INFO_X, CallerIdsFloatView.VIEW_INFO_VALUE_DEFAULT);
    }

    public int getInfoY() {
        return mContext.getSharedPreferences(PHONE_ALRET_VIEW, Context.MODE_PRIVATE).getInt(PHONE_ALRET_VIEW_INFO_Y, (int) (screenHeight * 0.5));
    }

    public boolean setInfoX(int infoX) {
        return mContext.getSharedPreferences(PHONE_ALRET_VIEW, Context.MODE_PRIVATE).edit().putInt(PHONE_ALRET_VIEW_INFO_X, infoX).commit();
    }

    public boolean setInfoY(int infoY) {
        return mContext.getSharedPreferences(PHONE_ALRET_VIEW, Context.MODE_PRIVATE).edit().putInt(PHONE_ALRET_VIEW_INFO_Y, infoY).commit();
    }
}
