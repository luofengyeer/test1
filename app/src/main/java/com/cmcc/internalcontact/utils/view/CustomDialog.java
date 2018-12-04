package com.cmcc.internalcontact.utils.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmcc.internalcontact.R;


/**
 * Created by liyingqing on 2017/5/10.
 */
public class CustomDialog {
    private boolean mCancel;
    private boolean mCancelable = true;
    private Context mContext;
    private AlertDialog mAlertDialog;
    private Builder mBuilder;
    private View mView;

    private CharSequence mTitle;
    private boolean mTitleAutoCenter = false;

    private CharSequence mMessage;
    private boolean mMessageAutoCenter = false;

    private boolean showPositiveBth;
    private boolean showNegativeBtn;
    private boolean mHasShow = false;

    private View mCustomView;


    private DialogInterface.OnDismissListener mOnDismissListener;
    private String mPositiveButtonTitle;
    private View.OnClickListener mPositiveButtonClick;

    private String mNegativeButtonTitle;
    private View.OnClickListener mNegativeButtonClick;

    private boolean isClick = false;


    public CustomDialog(Context context) {
        this.mContext = context;
    }

    public boolean isShowing() {
        if (mAlertDialog != null) {
            return mAlertDialog.isShowing();
        } else {
            return false;
        }
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public void show() {
        if (mHasShow == false)
            mBuilder = new Builder();
        else
            mAlertDialog.show();

        mHasShow = true;
    }

    public CustomDialog setView(View view) {
        mView = view;
        if (mBuilder != null) {
            mBuilder.setView(view);
        }
        return this;
    }

    public View getView() {
        if (mView != null) {
            return mView;
        } else {
            return null;
        }
    }

    public CustomDialog setCustomView(View view) {
        mCustomView = view;
        if (mBuilder != null) {
            mBuilder.setCustomContentView(view);
        }
        return this;
    }

    public View getCustomView() {
        return mCustomView;
    }

    /**
     * 取消提示框
     */
    public void dismiss() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }

    /**
     * 设置标题
     *
     * @param titleRes
     * @return
     */
    public CustomDialog setTitle(int titleRes) {
        return setTitle(titleRes, false);
    }

    public CustomDialog setTitle(CharSequence title) {
        return setTitle(title, false);
    }

    public CustomDialog setTitle(int titleRes, boolean autoCenter) {
        this.mTitle = mContext.getString(titleRes);
        return setTitle(mTitle, autoCenter);
    }

    public CustomDialog setTitleAutoCenter(boolean autoCenter) {
        this.mTitleAutoCenter = autoCenter;
        return this;
    }

    public CustomDialog setTitle(CharSequence title, boolean autoCenter) {
        mTitle = title;
        mTitleAutoCenter = autoCenter;
        if (mBuilder != null) {
            mBuilder.setTitle(mTitle);
        }
        return this;
    }


    public CustomDialog setMessage(@StringRes int messageRes) {
        return setMessage(messageRes, false);
    }

    public CustomDialog setMessage(CharSequence message) {
        return setMessage(message, false);
    }

    public CustomDialog setMessage(@StringRes int messageRes, boolean autoCenter) {
        this.mMessage = mContext.getString(messageRes);
        return setMessage(mMessage, autoCenter);
    }

    public CustomDialog setMessageAutoCenter(boolean autoCenter) {
        this.mMessageAutoCenter = autoCenter;
        return this;
    }

    public CustomDialog setMessage(CharSequence message, boolean autoCenter) {
        this.mMessage = message;
        this.mMessageAutoCenter = autoCenter;
        if (mBuilder != null) {
            mBuilder.setMessage(message);
        }
        return this;
    }

    public CustomDialog setOkButton(String text, final View.OnClickListener listener) {
        showPositiveBth = true;
        mPositiveButtonTitle = text;
        mPositiveButtonClick = listener;
        if (mBuilder != null) {
            mBuilder.setOkButton(text, listener);
        }
        return this;
    }

    public CustomDialog setOkButton(@StringRes int textRes, final View.OnClickListener listener) {
        String text = mContext.getResources().getString(textRes);
        showPositiveBth = true;
        mPositiveButtonTitle = text;
        mPositiveButtonClick = listener;
        if (mBuilder != null) {
            mBuilder.setOkButton(text, listener);
        }
        return this;
    }

    public CustomDialog setCancelButton(String text, final View.OnClickListener listener) {
        showNegativeBtn = true;
        mNegativeButtonTitle = text;
        mNegativeButtonClick = listener;
        if (mBuilder != null) {
            mBuilder.setCancelButton(text, listener);
        }
        return this;
    }

    public CustomDialog setCancelButton(@StringRes int textRes, final View.OnClickListener listener) {
        String text = mContext.getResources().getString(textRes);
        showNegativeBtn = true;
        mNegativeButtonTitle = text;
        mNegativeButtonClick = listener;
        if (mBuilder != null) {
            mBuilder.setCancelButton(text, listener);
        }
        return this;
    }

    /**
     * 设置是否外部点击可取消
     *
     * @param cancel 是否可取消
     * @return
     */
    public CustomDialog setCanceledOnTouchOutside(boolean cancel) {
        this.mCancel = cancel;
        if (mBuilder != null) {
            mBuilder.setCanceledOnTouchOutside(mCancel);
        }
        return this;
    }

    /**
     * 设置返回键是否可取消
     *
     * @param cancelable 是否可取消
     * @return
     */
    public CustomDialog setCancelable(boolean cancelable) {
        this.mCancelable = cancelable;
        if (mBuilder != null) {
            mBuilder.setCancelable(mCancelable);
        }
        return this;
    }

    public CustomDialog setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
        return this;
    }

    public TextView getmPositiveButton() {
        TextView positiveButton = null;
        if (mBuilder != null) {
            positiveButton = mBuilder.getmPositiveButton();
        }
        return positiveButton;
    }


    public TextView getmNegativeButton() {
        TextView negativeButton = null;
        if (mBuilder != null) {
            negativeButton = mBuilder.getCancelButton();
        }
        return negativeButton;
    }


    private class Builder {

        private TextView mDefaultTitleTv;
        private TextView mDefaultContentTv;

        private RelativeLayout defaultView;
        private View customDialogView;
        private CommonButton okButton;
        private CommonButton cancelButton;

        private FrameLayout mCustomViewContainer;
        private RelativeLayout rlDefaultBtn;
        private View pointBtnView;

        private Builder() {
            mAlertDialog = new AlertDialog.Builder(mContext).create();

            mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            customDialogView = LayoutInflater.from(mContext).inflate(R.layout.common_widget_custom_dialog, null);
            mAlertDialog.setView(customDialogView);

            mDefaultTitleTv = customDialogView.findViewById(R.id.tv_default_title);
            mDefaultContentTv = customDialogView.findViewById(R.id.tv_default_content);
            defaultView = customDialogView.findViewById(R.id.rl_default_view);
            okButton = customDialogView.findViewById(R.id.btn_dialog_ok);
            cancelButton = customDialogView.findViewById(R.id.btn_dialog_cancel);
            mCustomViewContainer = customDialogView.findViewById(R.id.fl_custom_view);

            pointBtnView = customDialogView.findViewById(R.id.point);
            rlDefaultBtn = customDialogView.findViewById(R.id.rl_default_btn);

            if (!TextUtils.isEmpty(mTitle)) {
                setTitle(mTitle);
            } else {
                mDefaultTitleTv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(mMessage)) {
                setMessage(mMessage);
            } else {
                mDefaultContentTv.setVisibility(View.GONE);
            }

            if (mCustomView != null) {
                defaultView.setVisibility(View.GONE);
                setCustomContentView(mCustomView);
            }

            if (showPositiveBth && showNegativeBtn) {
                setOkButton(mPositiveButtonTitle, mPositiveButtonClick);
                setCancelButton(mNegativeButtonTitle, mNegativeButtonClick);
                okButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                rlDefaultBtn.setVisibility(View.VISIBLE);
                pointBtnView.setVisibility(View.VISIBLE);
            } else if (showPositiveBth) {
                setOkButton(mPositiveButtonTitle, mPositiveButtonClick);
                okButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.GONE);
                rlDefaultBtn.setVisibility(View.GONE);
                rlDefaultBtn.setVisibility(View.VISIBLE);
                pointBtnView.setVisibility(View.GONE);
            } else if (showNegativeBtn) {
                setCancelButton(mNegativeButtonTitle, mNegativeButtonClick);
                cancelButton.setVisibility(View.VISIBLE);
                okButton.setVisibility(View.GONE);
                rlDefaultBtn.setVisibility(View.VISIBLE);
                pointBtnView.setVisibility(View.GONE);
            } else {
                rlDefaultBtn.setVisibility(View.GONE);
            }

            if (mView != null) {
                this.setView(mView);
            }
            mAlertDialog.setCanceledOnTouchOutside(mCancel);
            if (mOnDismissListener != null) {
                mAlertDialog.setOnDismissListener(mOnDismissListener);
            }

            mAlertDialog.setCancelable(mCancelable);
            mAlertDialog.show();

            customDialogView.post(new Runnable() {
                @Override
                public void run() {
                    if (customDialogView.getWidth() <= 0) {
                        customDialogView.post(this);
                        return;
                    }

                    int customDiaogContentWidth = customDialogView.getWidth() - DensityUtils.dip2px(customDialogView.getContext(), 40f);
                    //测量标题与内容,判断是否自动居中
                    int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

                    if (mTitleAutoCenter && mDefaultTitleTv.isShown()) {
                        mDefaultTitleTv.measure(spec, spec);
                        int measuredWidth = mDefaultTitleTv.getMeasuredWidth();
                        if (measuredWidth < customDiaogContentWidth) {
                            mDefaultTitleTv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    }

                    if (mMessageAutoCenter && mDefaultContentTv.isShown()) {
                        mDefaultContentTv.measure(spec, spec);
                        int measuredWidth = mDefaultContentTv.getMeasuredWidth();
                        if (measuredWidth < customDiaogContentWidth) {
                            mDefaultContentTv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    }

                    //测量按钮
                    if (showPositiveBth || showNegativeBtn) {
                        int middlePadding = DensityUtils.dip2px(customDialogView.getContext(), 20f);
                        int btnWidth = (customDiaogContentWidth - middlePadding) / 2;

                        ViewGroup.LayoutParams negativeButtonParams = cancelButton.getLayoutParams();
                        negativeButtonParams.width = btnWidth;
                        cancelButton.setLayoutParams(negativeButtonParams);

                        ViewGroup.LayoutParams positiveButtonParams = okButton.getLayoutParams();
                        positiveButtonParams.width = btnWidth;
                        okButton.setLayoutParams(positiveButtonParams);
                    }
                }
            });

        }

        public void setTitle(CharSequence title) {
            mDefaultTitleTv.setText(title);
            mDefaultTitleTv.setVisibility(View.VISIBLE);
            defaultView.setVisibility(View.VISIBLE);
        }

        public void setMessage(CharSequence message) {
            mDefaultContentTv.setText(message);
            mDefaultContentTv.setVisibility(View.VISIBLE);
            defaultView.setVisibility(View.VISIBLE);
        }

        public TextView getmPositiveButton() {
            return okButton;
        }

        public TextView getCancelButton() {
            return cancelButton;
        }

        /**
         * set positive button
         *
         * @param text     the name of button
         * @param listener
         */
        public void setOkButton(String text, View.OnClickListener listener) {
            if (TextUtils.isEmpty(text)) {
                text = "确定";
            }
            if (listener == null) {
                listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                };
            }
            okButton.setText(text);
            okButton.setOnClickListener(listener);
        }

        /**
         * set negative button
         *
         * @param text     the name of button
         * @param listener
         */
        public void setCancelButton(String text, View.OnClickListener listener) {
            if (TextUtils.isEmpty(text)) {
                text = "取消";
            }
            if (listener == null) {
                listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                };
            }
            cancelButton.setText(text);
            cancelButton.setOnClickListener(listener);
        }

        public void setView(View view) {
            mAlertDialog.setView(view);
        }

        public void setCustomContentView(View view) {
            mCustomViewContainer.removeAllViews();
            mCustomViewContainer.addView(view);
            mCustomViewContainer.setVisibility(View.VISIBLE);
        }

        public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            mAlertDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        }

        public void setCancelable(boolean cancelable) {
            mAlertDialog.setCancelable(cancelable);
        }
    }
}
