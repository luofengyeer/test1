package com.cmcc.internalcontact.utils.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmcc.internalcontact.R;


/**
 * Created by hkb on 2018/4/27 0027.
 */

public class ToolbarButton extends LinearLayout {


    public final static int DEFAULT_HIGH_PRIORITY = 0;
    public final static int IMAGEVIEW_HIGH_PRIORITY = 1;
    public final static int TEXTVIEW_HIGH_PRIORITY = 2;

    public ToolbarButton(Context context) {
        super(context);
    }

    public ToolbarButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(getContext(), attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToolbarButton);
        TextView textButton = buildText(context, typedArray);
        ImageView imageButton = buildImageView(context, typedArray);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (imageButton != null) {
            addView(imageButton, params);
        }
        if (textButton != null) {
            addView(textButton, params);
        }
        typedArray.recycle();
    }


    private TextView buildText(Context context, TypedArray typedArray) {
        String text = typedArray.getString(R.styleable.ToolbarButton_buttonText);
        int textRes = typedArray.getResourceId(R.styleable.ToolbarButton_buttonText, -1);
        int textColor = typedArray.getResourceId(R.styleable.ToolbarButton_buttonTextColor, R.color.common_theme_color);
        int textSize = typedArray.getResourceId(R.styleable.ToolbarButton_buttonTextSize, R.dimen.common_widget_toolbar_title_button_text_size);

        TextView textButton = new TextView(context);
        textButton.setGravity(Gravity.CENTER);
        textButton.setTextColor(getResources().getColor(textColor));
        textButton.setId(View.generateViewId());
        textButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(textSize));
        if (TextUtils.isEmpty(text) && textRes == -1) {
            return textButton;

        }
        if (!TextUtils.isEmpty(text)) {
            textButton.setText(text);
        } else {
            textButton.setText(textRes);
        }
        return textButton;
    }

    public void setButtonIcon(@DrawableRes int id) {
        ImageView imageView = getImageView();
        if (imageView != null) {
            imageView.setImageResource(id);
        }
    }

    public void setButtonIcon(Bitmap bitmap) {
        ImageView imageView = getImageView();
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    private ImageView getImageView() {
        int childCount = getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                if (childAt instanceof ImageView) {
                    return (ImageView) childAt;
                }
            }
        }
        return null;
    }

    public void setButtonIcon(Drawable drawable) {
        ImageView imageView = getImageView();
        if (imageView != null) {
            imageView.setVisibility(VISIBLE);
            imageView.setImageDrawable(drawable);
        }
    }

    public void setColor(@ColorInt int color) {
        int childCount = getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                if (childAt instanceof ImageView) {
                    ImageView imageView = (ImageView) childAt;
                    Drawable originalDrawable = imageView.getDrawable();
                    if (null != originalDrawable) {
                        originalDrawable = originalDrawable.mutate();
                        Drawable wrappedDrawable = DrawableCompat.wrap(originalDrawable);
                        wrappedDrawable.setTint(color);
                        imageView.setImageDrawable(wrappedDrawable);
                    }
                }
                if (childAt instanceof TextView) {
                    TextView textView = (TextView) childAt;
                    textView.setTextColor(color);
                }
            }
        }
    }

    public void setButtonText(@StringRes int id) {
        TextView textView = getTextView();
        if (textView != null) {
            textView.setText(id);
        }
    }

    public void setButtonTextSize(float textSize) {
        TextView textView = getTextView();
        if (textView != null) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    private TextView getTextView() {
        int childCount = getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                if (childAt instanceof TextView) {
                    return (TextView) childAt;
                }
            }
        }
        return null;
    }

    public void setButtonText(String text) {
        TextView textView = getTextView();
        if (textView != null) {
            textView.setText(text);
        }
    }


    private ImageView buildImageView(Context context, TypedArray typedArray) {
        int iconRes = typedArray.getResourceId(R.styleable.ToolbarButton_buttonIcon, -1);

        ImageView imageButton = new ImageView(context);
        imageButton.setId(View.generateViewId());
        if (iconRes == -1) {
            return imageButton;
        }
        VectorDrawableCompat vectorDrawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_common_widget_toolbar_back, context.getTheme());
        if (vectorDrawable == null) {
            return imageButton;
        }
        imageButton.setImageDrawable(ActivityCompat.getDrawable(getContext(), R.drawable.ic_common_widget_toolbar_back));
        return imageButton;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        boolean pressed = isPressed();
        for (int i = 0; i < this.getChildCount(); i++) {
            View childAt = this.getChildAt(i);
            if (childAt == null) {
                continue;
            }
            childAt.setAlpha(pressed ? 0.7f : 1f);
        }
        invalidate();
    }

    public void setIconVisibility(int visibility) {
        if (getImageView() != null) {
            getImageView().setVisibility(visibility);
        }
    }

    public void setTextVisibility(int visibility) {
        if (getTextView() != null) {
            getTextView().setVisibility(visibility);
        }
    }

    public void setDisplayPriority(int priority) {
        //默认就是两个同时显示
        if (priority == DEFAULT_HIGH_PRIORITY) {
            return;
        }
        ImageView imageView = getImageView();
        TextView textView = getTextView();
        if (imageView == null || textView == null) {
            return;
        }
        //图片优先
        if (imageView.getDrawable() != null && priority == IMAGEVIEW_HIGH_PRIORITY) {
            textView.setVisibility(GONE);
        }
        //文字优先
        if (textView.getText() != null && priority == TEXTVIEW_HIGH_PRIORITY) {
            imageView.setVisibility(GONE);
        }
    }

    private Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }
}
