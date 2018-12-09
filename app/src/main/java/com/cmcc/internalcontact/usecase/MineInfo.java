package com.cmcc.internalcontact.usecase;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.cmcc.internalcontact.model.PersonBean;
import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.http.LoginResponseBean;
import com.cmcc.internalcontact.store.DepartDiskStore;
import com.cmcc.internalcontact.utils.SharePreferencesUtils;
import com.cmcc.internalcontact.utils.http.HttpManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import me.shaohui.advancedluban.Luban;
import retrofit2.Call;
import retrofit2.Response;

import static com.cmcc.internalcontact.usecase.LoginUsecase.TAG_USE_INFO;


public class MineInfo {
    public Observable<PersonBean> getMineInfo() {
        return Observable.fromCallable(new Callable<PersonBean>() {
            @Override
            public PersonBean call() throws Exception {
                String string = SharePreferencesUtils.getInstance().getString(TAG_USE_INFO, "");
                if (string == null) {
                    return null;
                }
                LoginResponseBean.UserInfo userInfo = JSON.parseObject(string, LoginResponseBean.UserInfo.class);
                if (userInfo == null) {
                    return null;
                }
                PersonBean personBean = new PersonBean();
                personBean.setAvator(userInfo.getHeadPic());
                personBean.setName(userInfo.getName());
                personBean.setPhone(userInfo.getMobile() + "\n" + userInfo.getMobile2());
                personBean.setTel(userInfo.getTel());
                personBean.setEmail(userInfo.getEmail());
                personBean.setJob(userInfo.getJob());
                DepartDiskStore departDiskStore = new DepartDiskStore();
                DepartModel departModel = departDiskStore.getDepartModeByDepartId(userInfo.getOrgId());
                if (departModel != null) {
                    personBean.setDepart(departModel);
                    DepartModel parentDepart = departDiskStore.getDepartModeByParentDepartId(departModel.getParentCode());
                    if (parentDepart != null) {
                        personBean.setMechanism(parentDepart);
                    }
                }
                return personBean;
            }
        });
    }

    public Observable<Void> updateMine(Context context) {
        return Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Call<LoginResponseBean.UserInfo> responseBeanCall = HttpManager.getInstance(context).getApi()
                        .updateAppUser();
                Response<LoginResponseBean.UserInfo> response = responseBeanCall.execute();
                LoginResponseBean.UserInfo userInfo = response.body();
                if (userInfo != null) {
                    SharePreferencesUtils.getInstance().setString(TAG_USE_INFO, JSON.toJSONString(userInfo));
                }
                return null;
            }
        });
    }

    public Observable<String> uploadHeadPic(Context context, String filePath) {
        return Observable.just(filePath).flatMap(new Function<String, Observable<File>>() {
            @Override
            public Observable<File> apply(String s) throws Exception {
                if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
                    return null;
                }
                return Luban.compress(context, new File(filePath)).putGear(Luban.CUSTOM_GEAR).asObservable();
            }
        }).flatMap(new Function<File, Observable<String>>() {
            @Override
            public Observable<String> apply(File file) throws Exception {
                byte[] bytes = file2byte(file);
                Call<HashMap<String, String>> responseBeanCall = HttpManager.getInstance(context).getApi()
                        .updateAvatar(bytes.toString());
                Response<HashMap<String, String>> response = responseBeanCall.execute();
                HashMap<String, String> result = response.body();
                return Observable.just(result.values().toArray(new String[]{})[0]);
            }
        });
    }

    public static byte[] file2byte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
