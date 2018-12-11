package com.cmcc.internalcontact.usecase;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cmcc.internalcontact.activity.adapter.SearchListAdapter;
import com.cmcc.internalcontact.model.PersonBean;
import com.cmcc.internalcontact.model.UpdateAppBean;
import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.http.LoginResponseBean;
import com.cmcc.internalcontact.store.PersonDiskStore;
import com.cmcc.internalcontact.utils.AesUtils;
import com.cmcc.internalcontact.utils.ArraysUtils;
import com.cmcc.internalcontact.utils.Base64;
import com.cmcc.internalcontact.utils.SharePreferencesUtils;
import com.cmcc.internalcontact.utils.http.HttpManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import top.zibin.luban.Luban;

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
                personBean.setName(userInfo.getUsername());
                personBean.setPhone((TextUtils.isEmpty(userInfo.getMobile()) ? "" : userInfo.getMobile())
                        + (TextUtils.isEmpty(userInfo.getMobile2()) ? "" :
                        TextUtils.isEmpty(userInfo.getMobile()) ? userInfo.getMobile2() : "\n" + userInfo.getMobile2()));
                personBean.setTel(userInfo.getTel());
                personBean.setEmail(userInfo.getEmail());
                personBean.setJob(userInfo.getJob());

                List<DepartModel> departs = new PersonDiskStore().getDepartByPersonId(userInfo.getAccount(), SearchListAdapter.TYPE_COMPANY);
                String departStr = "";
                List<String> departIds = new ArrayList<>();
                if (!ArraysUtils.isListEmpty(departs)) {
                    for (int i = 0; i < departs.size(); i++) {
                        DepartModel departModel = departs.get(i);
                        departStr += departModel.getDeptName();
                        if (!TextUtils.isEmpty(departModel.getParentCode())) {
                            departIds.add(departModel.getParentCode());
                        }
                        if (i != departs.size() - 1) {
                            departStr += ",";
                        }
                    }
                }


                List<DepartModel> mechanisms = new PersonDiskStore().getDepartByCodes(departIds);
                String company = "";
                if (!ArraysUtils.isListEmpty(mechanisms)) {
                    for (int i = 0; i < mechanisms.size(); i++) {
                        DepartModel mechanism = mechanisms.get(i);
                        company += mechanism.getDeptName();
                        if (i != mechanisms.size() - 1) {
                            company += ",";
                        }
                    }
                }
                personBean.setMechanism(company);
                personBean.setDepart(departStr);
               /* DepartDiskStore departDiskStore = new DepartDiskStore();
                DepartModel departModel = departDiskStore.getDepartModeByDepartId(userInfo.getOrgId());
                if (departModel != null) {
                    personBean.setDepart(departModel);
                    DepartModel parentDepart = departDiskStore.getDepartModeByParentDepartId(departModel.getParentCode());
                    if (parentDepart != null) {
                        personBean.setMechanism(parentDepart);
                    }
                }*/
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
                if (result == null) {
                    return Observable.just("");
                }
                String item = result.values().toArray(new String[]{})[0];
                String string = SharePreferencesUtils.getInstance().getString(TAG_USE_INFO, "");
                if (TextUtils.isEmpty(string)) {
                    return Observable.just(item);
                }
                /*LoginResponseBean.UserInfo userInfo = JSON.parseObject(string, LoginResponseBean.UserInfo.class);
                if (userInfo == null) {
                    return Observable.just(item);
                }*/
              /*  LoginResponseBean.UserInfo userInfo = new LoginResponseBean.UserInfo();
                userInfo.setHeadPic(AesUtils.encrypt(item));*/
                HashMap<String, String> data = new HashMap<>();
                data.put("headPic", AesUtils.encrypt(item));
                Call<Void> responseUpdate = HttpManager.getInstance(context).getApi().updateAppUser(data);
                responseUpdate.execute();
                return Observable.just(item);
            }
        });
    }

    public Observable<Object> updateUserInfo(Context context, String key, String values) {
        return Observable.just("").map(new Function<String, Object>() {
            @Override
            public Object apply(String s) throws Exception {
                HashMap<String, String> data = new HashMap<>();
                data.put(key, AesUtils.encrypt(values));
                HttpManager.getInstance(context).getApi().updateAppUser(data).execute().body();
                return new Object();
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

    public Observable<String> downloadApk(Context context, String downloadUrl, String savePath) {
        return Observable.just(downloadUrl).flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String downloadUrl) throws Exception {
                HashMap<String, String> map = new HashMap<>();
                map.put("downloadPath", AesUtils.encrypt(downloadUrl));
                Call<ResponseBody> responseBodyCall = HttpManager.getInstance(context).getApi().downloadApp(map);
                Response<ResponseBody> response = responseBodyCall.execute();
                ResponseBody body = response.body();
                if (body == null) {
                    return Observable.just("");
                }
                long l = body.contentLength();
                Log.i("download", "size: " + l);
//                InputStream inputStream = body.byteStream();
                return Observable.just("");
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
