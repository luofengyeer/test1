package com.cmcc.internalcontact.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

public class Utils {
    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }
    public static SpannableStringBuilder addClickablePart(Activity context, String str) {
        SpannableString spanStr = new SpannableString("");
        final SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(spanStr);
        mSpannableStringBuilder.append(str);

        String[] titlesStr = str.split("\n");

        if (titlesStr.length > 0) {
            // 最后一个
            for (int i = 0; i < titlesStr.length; i++) {
                final String name = titlesStr[i];
                //记录当前被选择的序号，因为要在点击事件内响应，所以设置为final
                final int choiceI = i;
                final int start = str.indexOf(name) + spanStr.length();
                mSpannableStringBuilder.setSpan(new ClickableSpan() {

                    //响应点击事件
                    @Override
                    public void onClick(View widget) {
                        call(context, titlesStr[choiceI]);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //去除下划线
                        ds.setUnderlineText(false);
                    }

                }, start, start + name.length(), 0);
            }
        }
        return mSpannableStringBuilder.append("");
    }

    /**
     * 获取拨打电话意图
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.CALL_PHONE"/>}</p>
     *
     * @param phoneNumber 电话号码
     */
    public static void call(Activity activity, String phoneNumber) {
        if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(activity,"该人员没有手机号码",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * 获取跳至发送短信界面的意图
     *
     * @param phoneNumber 接收号码
     */
    public static void sendSms(Activity activity, String phoneNumber) {
        if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(activity,"该人员没有手机号码",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("smsto:" + phoneNumber));
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(sendIntent);
    }
}
