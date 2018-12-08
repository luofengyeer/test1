package com.cmcc.internalcontact.usecase;

import com.cmcc.internalcontact.model.SearchPersonBean;
import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.PersonModel;
import com.cmcc.internalcontact.store.DepartDiskStore;
import com.cmcc.internalcontact.store.PersonDiskStore;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class SearchList {

    /**
     * 根据内容搜索机构信息
     *
     * @param search 要搜索的内容
     * @return 机构信息集合
     */
    public Observable<List<DepartModel>> searchMechanism(String search) {
        return Observable.just("%" + search + "%").map(new Function<String, List<DepartModel>>() {
            @Override
            public List<DepartModel> apply(String s) throws Exception {
                DepartDiskStore departDiskStore = new DepartDiskStore();
                return departDiskStore.searchMechanismList(s);
            }
        });
    }

    /**
     * 根据内容搜索单位信息
     *
     * @param search 要搜索的内容
     * @return 单位信息集合
     */
    public Observable<List<DepartModel>> searchCompany(String search) {
        return Observable.just("%" + search + "%").map(new Function<String, List<DepartModel>>() {
            @Override
            public List<DepartModel> apply(String s) throws Exception {
                DepartDiskStore departDiskStore = new DepartDiskStore();
                return departDiskStore.searchCompanyList(s);
            }
        });
    }

    /**
     * 根据内容搜索联系人信息
     *
     * @param search 要搜索的内容
     * @return 联系人信息集合
     */
    public Observable<List<SearchPersonBean>> searchPerson(String search) {
        return Observable.just("%" + search + "%").map(new Function<String, List<SearchPersonBean>>() {
            @Override
            public List<SearchPersonBean> apply(String s) throws Exception {
                PersonDiskStore personDiskStore = new PersonDiskStore();
                DepartDiskStore departDiskStore = new DepartDiskStore();
                List<PersonModel> personModels = personDiskStore.searchPersonList(s);
                if (personModels == null) {
                    return null;
                }
                List<SearchPersonBean> result = new ArrayList<>();
                for (PersonModel personModel : personModels) {
                    if (personModel == null) {
                        continue;
                    }
                    SearchPersonBean personBean = new SearchPersonBean();
                    personBean.setPersonModel(personModel);
                    personBean.setDepartModel(departDiskStore.getDepartModeByDepartId(personModel.getOrgId()));
                    result.add(personBean);
                }
                return result;
            }
        });
    }
}
