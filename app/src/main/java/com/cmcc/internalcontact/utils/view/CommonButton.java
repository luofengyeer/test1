package com.cmcc.internalcontact.utils.view;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;


public class CommonButton extends AppCompatButton {

    private float enableAlpha = 0.4f;
    private float pressAlpha = 0.7f;

    public CommonButton(Context context) {
        this(context, null);
    }

    private void init() {
        setStateListAnimator(null);
        setEnable();
    }

    public CommonButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        setPress();
        setEnable();
        invalidate();
    }

    /**
     * 设置enable属性
     */
    private void setEnable() {
        boolean enabled = isEnabled();
        if (getAlpha() != 0) {
            setAlpha(enabled ? 1f : enableAlpha);
        }
    }

    /**
     * 设置press属性
     */
    private void setPress() {
        boolean pressed = isPressed();
        Drawable backgroundDrawable = getBackground();
        if (null != backgroundDrawable) {
            backgroundDrawable.setAlpha(pressed ? (int) (255 * pressAlpha) : 255);
        }
        ColorStateList colorStateList = getTextColors();
        if (null != colorStateList) {
            colorStateList = colorStateList.withAlpha(pressed ? (int) (255 * pressAlpha) : 255);
            setTextColor(colorStateList);
        }
    }
}
