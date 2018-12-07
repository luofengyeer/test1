package com.cmcc.internalcontact.utils;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by hkb on 2017/11/29.
 */

public class ArraysUtils {

    private static final char[] bcdLookup = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'};
    /**
     * SQL语句占位符支持最大数,超过这个需要分批执行
     */
    public static final int MAX_SQL_PARAM_COUNT = 1000;

    /**
     * bytes转换成十六进制字符串
     */
    public static String bytes2Hex(byte[] b) {
        StringBuilder s = new StringBuilder(b.length * 2);
        for (int n = 0; n < b.length; n++) {
            s.append(bcdLookup[(b[n] >>> 4) & 0x0f]);
            s.append(bcdLookup[b[n] & 0x0f]);

        }
        return s.toString();
    }


    /**
     * 十六进制字符串转换成 bytes
     */
    public static byte[] hex2Bytes(String src) throws NumberFormatException {
        byte[] bytes = new byte[src.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(src.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static <T> List<List<T>> subListByLimit(List<T> list, int limit) {
        if (isListEmpty(list)) {
            return null;
        }

        if (limit <= 0) {
            throw new IllegalArgumentException("subList limit is invalid:" + limit);
        }
        List<List<T>> wrapList = new ArrayList<>();
        int count = 0;
        while (count < list.size()) {
            wrapList.add(new ArrayList<>(list.subList(count, (count + limit > list.size() ? list.size() : count + limit))));
            count += limit;
        }
        return wrapList;
    }

    public static long[] list2Array(List<Long> list) {
        if (isListEmpty(list)) {
            return null;
        }
        long[] res = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    public static Long[] list2LongArray(List<Long> list) {
        if (isListEmpty(list)) {
            return null;
        }
        Long[] res = new Long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    public static List<Long> array2List(long[] list) {
        if (list.length < 0) {
            return null;
        }
        List<Long> res = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            res.add(list[i]);
        }
        return res;
    }

    public static <T> List<T> array2List(T[] list) {
        if (list.length < 0) {
            return null;
        }
        return Arrays.asList(list);
    }

    public static boolean isListEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }

    /**
     * 判断Map是否为空
     *
     * @param map 待判断map对象
     * @return
     * @作者 hkb
     * @since 2014年9月26日 下午2:01:21
     */
    public static boolean isMapEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断String类型的数组是否为空
     *
     * @param values
     * @return
     */
    public static <T> boolean isArrayEmpty(T[] values) {
        return values == null || values.length <= 0;
    }

    /**
     * 将集合(Long)转换成SQL语句条件字符串
     *
     * @param list Long集合
     * @return 转换成SQL语句条件字符串
     */
    public static String joinerWith(List<Long> list) {
        if (isListEmpty(list)) {
            return null;
        }
        String result = "";
        for (Long aLong : list) {
            if (aLong != null) {
                result += TextUtils.isEmpty(result) ? String.valueOf(aLong) : "," + aLong;
            }
        }
        return result;
    }

    /**
     * 将集合(Long)转换成SQL语句条件字符串
     *
     * @param list Long集合
     * @return 转换成SQL语句条件字符串
     */
    public static String joinerWith(List<String> list, String separator) {
        if (isListEmpty(list)) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (String aLong : list) {
            if (aLong != null) {
                result.append(TextUtils.isEmpty(result) ? aLong : separator + aLong);
            }
        }
        return result.toString();
    }

    /**
     * 深拷贝List
     *
     * @param src
     * @param <T>
     * @return
     */
    public static <T> List<T> deepCopy(List<T> src) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);


            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            List<T> dest = (List<T>) in.readObject();
            return dest;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
