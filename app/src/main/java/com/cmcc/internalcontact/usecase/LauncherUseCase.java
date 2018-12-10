package com.cmcc.internalcontact.usecase;

import android.content.Context;

import com.cmcc.internalcontact.model.http.GetWaitImgBean;
import com.cmcc.internalcontact.utils.ArraysUtils;
import com.cmcc.internalcontact.utils.http.HttpManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Call;

public class LauncherUseCase {
    public Observable<String> loadLauncherImg(Context context){
        return Observable.just("").map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                Call<List<GetWaitImgBean>> appWaitImg = HttpManager.getInstance(context).getApi().getAppWaitImg();
                List<GetWaitImgBean> imgBeans = appWaitImg.execute().body();
                if(ArraysUtils.isListEmpty(imgBeans)){
                    return "";
                }
                return imgBeans.get(0).getImgUrl();
            }
        });
    }
}
