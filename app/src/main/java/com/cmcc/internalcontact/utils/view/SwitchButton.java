package com.cmcc.internalcontact.utils.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cmcc.internalcontact.R;


/**
 * Created by hkb on 2018/5/17 0017.
 */

public class SwitchButton extends AppCompatImageView {

    private boolean isOpen = false;
    private OnStateChangeListener onStateChangeListener;
    private OnClickListener onClickListener;

    public SwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(getResources().getColor(android.R.color.transparent));
        setOpen(isOpen);
    }

    public void setOnClickButtonListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isOpen = !isOpen;
                if (onClickListener != null) {
                    onClickListener.onClick(this, isOpen);
                }
                if (onStateChangeListener == null) {
                    setOpen(isOpen);
                } else {
                    onStateChangeListener.onChange(this, isOpen);
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
        setImageResource(isOpen ? R.drawable.ic_switch_on : R.drawable.ic_switch_off);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public interface OnStateChangeListener {
        void onChange(View v, boolean isOpen);
    }

    public interface OnClickListener {
        void onClick(View v, boolean isOpen);
    }
}
