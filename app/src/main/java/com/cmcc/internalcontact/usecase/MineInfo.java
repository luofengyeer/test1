package com.cmcc.internalcontact.usecase;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cmcc.internalcontact.model.PersonBean;
import com.cmcc.internalcontact.model.UpdateAppBean;
import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.http.LoginResponseBean;
import com.cmcc.internalcontact.store.DepartDiskStore;
import com.cmcc.internalcontact.utils.AesUtils;
import com.cmcc.internalcontact.utils.Base64;
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
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.Response;
import top.zibin.luban.Luban;

import static com.cmcc.internalcontact.usecase.LoginUsecase.TAG_USE_INFO;
import static com.cmcc.internalcontact.utils.Constant.BASE_URL;


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

    public Observable<LoginResponseBean.UserInfo> updateMine(Context context) {
        return Observable.just("").flatMap(new Function<String, ObservableSource<LoginResponseBean.UserInfo>>() {
            @Override
            public ObservableSource<LoginResponseBean.UserInfo> apply(String s) throws Exception {
                Call<LoginResponseBean.UserInfo> responseBeanCall = HttpManager.getInstance(context).getApi()
                        .updateAppUser();
                Response<LoginResponseBean.UserInfo> response = responseBeanCall.execute();
                return Observable.just(response.body());
            }
        });
    }

    public Observable<String> uploadHeadPic(Context context, String filePath) {
        return Observable.just(filePath).map(new Function<String, File>() {
            @Override
            public File apply(String s) throws Exception {
                if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
                    return null;
                }
                return Luban.with(context).load(s).get(s);
            }
        }).flatMap(new Function<File, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(File file) throws Exception {
                byte[] bytes = file2byte(file);
                String encode = Base64.encode(bytes);
                HashMap<String, String> map = new HashMap<>();
                map.put("headPic", encode);
                map.put("suffix", getFormatName(filePath));
                Call<HashMap<String, String>> responseBeanCall = HttpManager.getInstance(context).getApi()
                        .updateAvatar(map);
                Response<HashMap<String, String>> response = responseBeanCall.execute();
                HashMap<String, String> result = response.body();
                String item = result.values().toArray(new String[]{})[0];
                String path = BASE_URL + item.replaceAll("\\\\", "/");
                Log.v("upload", "path: " + path);
                path = path.replace("..png", ".png");
                return Observable.just(path);
            }
        });
    }

    public Observable<UpdateAppBean> checkUpdate(Context context, String versionCode) {
        return Observable.just(versionCode).map(new Function<String, UpdateAppBean>() {
            @Override
            public UpdateAppBean apply(String s) throws Exception {
                HashMap<String, String> map = new HashMap<>();
                map.put("version", AesUtils.encrypt(versionCode));
                map.put("appType", AesUtils.encrypt(String.valueOf(1)));
                Call<UpdateAppBean> responseBeanCall = HttpManager.getInstance(context).getApi()
                        .updateApp(map);
                Response<UpdateAppBean> response = responseBeanCall.execute();
                return response.body();
            }
        });
    }

    /**
     * 获取文件格式名
     */
    public static String getFormatName(String fileName) {
        fileName = fileName.trim();
        String s[] = fileName.split("\\.");
        if (s.length >= 2) {
            return "." + s[s.length - 1];
        }
        return "";
    }

    private static byte[] file2byte(File file) {
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
