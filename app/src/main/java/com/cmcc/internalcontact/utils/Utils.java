package com.cmcc.internalcontact.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

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
        return addClickablePart(context, str, false);
    }

    public static SpannableStringBuilder addClickablePart(Activity context, String str, boolean isUnderLine) {
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
                        if (!isUnderLine) {
                            //去除下划线
                            ds.setUnderlineText(false);
                        }
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
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(activity, "该人员没有手机号码", Toast.LENGTH_SHORT).show();
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
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(activity, "该人员没有手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("smsto:" + phoneNumber));
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(sendIntent);
    }

    /**
     * 打开文件
     *
     * @param context  上下文环境变量
     * @param filePath 文件全路径
     */
    public static void openFile(Context context, String filePath) throws Exception {

        if (TextUtils.isEmpty(filePath)) {
            Log.e("OpenFileUtil", "文件打开失败 路径为空");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        // 获取文件file的MIME类型
        String mimeType = getMIMEType(new File(filePath));
        if ("*/*".equals(mimeType)) {
            mimeType = getMimeType(filePath);
        }
        File file = new File(filePath);
        intent.setDataAndType(Uri.fromFile(file), mimeType);

        context.startActivity(intent);
    }

    public static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if ("".equals(end))
            return type;
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    public static String[][] MIME_MapTable = {
            // {后缀名，MIME类型}
            {".3gp", "video/3gpp"}, {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"}, {".avi", "video/x-msvideo"}, {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"}, {".c", "text/plain"}, {".class", "application/octet-stream"},
            {".conf", "text/plain"}, {".cpp", "text/plain"}, {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"}, {".gif", "image/gif"}, {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"}, {".h", "text/plain"}, {".htm", "text/html"}, {".html", "text/html"},
            {".jar", "application/java-archive"}, {".java", "text/plain"}, {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"}, {".js", "application/x-javascript"}, {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"}, {".m4a", "audio/mp4a-latm"}, {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"}, {".m4u", "video/vnd.mpegurl"}, {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"}, {".mp2", "audio/x-mpeg"}, {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"}, {".mpc", "application/vnd.mpohun.certificate"}, {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"}, {".mpg", "video/mpeg"}, {".mpg4", "video/mp4"}, {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"}, {".ogg", "audio/ogg"}, {".pdf", "application/pdf"},
            {".png", "image/png"}, {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"}, {".rc", "text/plain"}, {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"}, {".sh", "text/plain"}, {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"}, {".txt", "text/plain"}, {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"}, {".wmv", "audio/x-ms-wmv"}, {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"}, {".z", "application/x-compress"}, {".zip", "application/x-zip-compressed"},
            {".aac", "audio/aac"}, {".jpe", "image/jpeg"},
            {".amr", "audio/amr"}, {".mid", "audio/mid"},
            {"", "*/*"}};

}
