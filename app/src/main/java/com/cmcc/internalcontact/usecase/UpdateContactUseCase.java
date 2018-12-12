package com.cmcc.internalcontact.usecase;


import android.content.Context;
import android.util.Log;

import com.cmcc.internalcontact.model.db.DepartPersonModel;
import com.cmcc.internalcontact.model.http.UpdateContactResponse;
import com.cmcc.internalcontact.model.http.UpdateDeptResponse;
import com.cmcc.internalcontact.utils.AesUtils;
import com.cmcc.internalcontact.utils.ArraysUtils;
import com.cmcc.internalcontact.utils.Constant;
import com.cmcc.internalcontact.utils.SharePreferencesUtils;
import com.cmcc.internalcontact.utils.http.Api;
import com.cmcc.internalcontact.utils.http.HttpManager;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import retrofit2.Call;

public class UpdateContactUseCase {
    public static final String KEY_CONTACT_VERSION = "key_contact_version";
    public static final String KEY_DEPART_VERSION = "key_depart_version";
    private Api api;

    public UpdateContactUseCase(Context context) {
        api = HttpManager.getInstance(context).getApi();
    }

    public Observable updateContact() {
        return Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                download(Constant.YELLOW_PAGE_URL, Constant.BASE_AVATRE_URL + "app/huangye.html");

                long personVersion = SharePreferencesUtils.getInstance().getLong(KEY_CONTACT_VERSION, 0);
                HashMap<String, String> requestData = new HashMap<>();
                requestData.put("telsVersion", AesUtils.encrypt(String.valueOf(personVersion)));
                HashMap<String, Integer> body = api.isHasNewContacts(requestData).execute().body();
                if (ArraysUtils.isMapEmpty(body)) {
                    return body;
                }
                //更新人员
                Integer isHas = body.get("isHas");
                if (isHas == 1) {
                    UpdateContactResponse contactResponse = api.updateContacts().execute().body();
                    if (contactResponse != null && !ArraysUtils.isListEmpty(contactResponse.getData())) {
                        new LoadContactList().savePersons(contactResponse.getData());
                        SharePreferencesUtils.getInstance().setLong(KEY_CONTACT_VERSION, contactResponse.getVersion());
                    }
                }
                long departVersion = SharePreferencesUtils.getInstance().getLong(KEY_DEPART_VERSION, 0);
                HashMap<String, String> requestDeptData = new HashMap<>();
                requestDeptData.put("deptsVersion", AesUtils.encrypt(String.valueOf(departVersion)));
                //更新部门
                HashMap<String, Integer> departBody = api.isHasNewDept(requestDeptData).execute().body();
                if (ArraysUtils.isMapEmpty(departBody)) {
                    return body;
                }
                isHas = departBody.get("isHas");
                if (isHas == 1) {
                    UpdateDeptResponse updateDeptResponse = api.updateDepartments().execute().body();
                    if (updateDeptResponse != null && !ArraysUtils.isListEmpty(updateDeptResponse.getData())) {
                        new LoadContactList().saveDepartments(updateDeptResponse.getData());
                        SharePreferencesUtils.getInstance().setLong(KEY_DEPART_VERSION, updateDeptResponse.getVersion());
                    }
//                    download(Constant.YELLOW_PAGE_URL, Constant.BASE_AVATRE_URL + "app/huangye.html");
                }
                Call<List<DepartPersonModel>> userDeptAll = api.getUserDeptAll();
                List<DepartPersonModel> body1 = userDeptAll.execute().body();
                if (!ArraysUtils.isListEmpty(body1)) {
                    new LoadContactList().saveDepartPersons(body1);
                }
                return body;
            }
        });
    }

    private void download(String name, String downloadUrl) {
        FileDownloader.getImpl().create(downloadUrl)
                .setPath(name)
                .setForceReDownload(true)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.d("download", "completed");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.e("download", "error", e);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                })
                .start();
    }
}
