package com.cmcc.internalcontact.usecase;


import android.content.Context;

import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.PersonModel;
import com.cmcc.internalcontact.utils.ArraysUtils;
import com.cmcc.internalcontact.utils.SharePreferencesUtils;
import com.cmcc.internalcontact.utils.http.Api;
import com.cmcc.internalcontact.utils.http.HttpManager;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

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
                long personVersion = SharePreferencesUtils.getInstance().getLong(KEY_CONTACT_VERSION, 0);
                HashMap<String, Integer> body = api.isHasNewContacts(personVersion).execute().body();
                if (ArraysUtils.isMapEmpty(body)) {
                    return null;
                }
                //更新人员
                Integer isHas = body.get("isHas");
                if (isHas == 1) {
                    List<PersonModel> personModels = api.updateContacts().execute().body();
                    if (!ArraysUtils.isListEmpty(personModels)) {
                        new LoadContactList().savePersons(personModels);
                        SharePreferencesUtils.getInstance().setLong(KEY_CONTACT_VERSION, 0);
                    }
                }
                long departVersion = SharePreferencesUtils.getInstance().getLong(KEY_CONTACT_VERSION, 0);
                //更新部门
                HashMap<String, Integer> departBody = api.isHasNewDept(departVersion).execute().body();
                if (ArraysUtils.isMapEmpty(departBody)) {
                    return null;
                }
                isHas = departBody.get("isHas");
                if (isHas == 1) {
                    List<DepartModel> personModels = api.updateDepartments().execute().body();
                    if (!ArraysUtils.isListEmpty(personModels)) {
                        new LoadContactList().saveDepartments(personModels);
                        //TODO 版本号缺失
                        SharePreferencesUtils.getInstance().setLong(KEY_DEPART_VERSION, 0);
                    }
                }
                return null;
            }
        });
    }
}
