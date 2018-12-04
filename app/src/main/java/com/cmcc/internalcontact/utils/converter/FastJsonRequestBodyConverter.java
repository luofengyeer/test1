package com.cmcc.internalcontact.utils.converter;


import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * ResponseBody转换器
 *
 * @author hjl
 * @date 2017/5/11
 */

public class FastJsonRequestBodyConverter<T> implements Converter<T, RequestBody> {

    @Override
    public RequestBody convert(@NonNull T value) throws IOException {
        return RequestBody.create(
                MediaType.parse("application/json;charset=UTF-8"),
                JsonManager.beanToJson(value));
    }
}
