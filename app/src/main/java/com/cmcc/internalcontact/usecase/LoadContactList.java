package com.cmcc.internalcontact.usecase;

import com.cmcc.internalcontact.model.MainInfoBean;
import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.PersonModel;
import com.cmcc.internalcontact.store.PersonDiskStore;
import com.cmcc.internalcontact.utils.ArraysUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class LoadContactList {
    public Observable<List<MainInfoBean>> loadDepartData(long departId) {
        return Observable.just(departId).map(new Function<Long, List<MainInfoBean>>() {
            @Override
            public List<MainInfoBean> apply(Long aLong) throws Exception {
                PersonDiskStore personDiskStore = new PersonDiskStore();
                List<DepartModel> departListByParentId = personDiskStore.getDepartListByParentId(departId);
                if (ArraysUtils.isListEmpty(departListByParentId)) {
                    return null;
                }
                List<MainInfoBean> resMainInfoBeans = new ArrayList<>();
                for (DepartModel departModel : departListByParentId) {
                    MainInfoBean<DepartModel> mainInfoBean = new MainInfoBean<>();
                    mainInfoBean.setType(MainInfoBean.TYPE_DEPART);
                    mainInfoBean.setData(departModel);
                    mainInfoBean.setName(departModel.getDeptName());
                    resMainInfoBeans.add(mainInfoBean);
                }
                return resMainInfoBeans;
            }
        });
    }

    public Observable<List<MainInfoBean>> loadPersons(long departId) {
        return Observable.just(departId).map(new Function<Long, List<MainInfoBean>>() {
            @Override
            public List<MainInfoBean> apply(Long aLong) throws Exception {
                PersonDiskStore personDiskStore = new PersonDiskStore();
                List<PersonModel> persons = personDiskStore.getPersonsByDepartId(aLong);
                if (ArraysUtils.isListEmpty(persons)) {
                    return null;
                }
                List<MainInfoBean<PersonModel>> resMainInfoBeans = new ArrayList<>();
                for (PersonModel personModel : persons) {
                    MainInfoBean<PersonModel> mainInfoBean = new MainInfoBean<>();
                    mainInfoBean.setType(MainInfoBean.TYPE_PERSON);
                    mainInfoBean.setData(personModel);
                    mainInfoBean.setName(personModel.getUsername());
//                    mainInfoBean.setDepartmentName();
                    resMainInfoBeans.add(mainInfoBean);
                }
                return null;
            }
        });
    }
}
