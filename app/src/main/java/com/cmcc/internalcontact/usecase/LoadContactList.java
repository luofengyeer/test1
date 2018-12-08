package com.cmcc.internalcontact.usecase;

import com.cmcc.internalcontact.db.AppDataBase;
import com.cmcc.internalcontact.model.MainInfoBean;
import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.PersonModel;
import com.cmcc.internalcontact.store.PersonDiskStore;
import com.cmcc.internalcontact.utils.ArraysUtils;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class LoadContactList {
    public void saveDepartments(List<DepartModel> departBeans) {
        departBeans = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
            DepartModel departModel1 = new DepartModel();
            departModel1.setDeptName("部门:" + i);
            departModel1.setId(i);
            departBeans.add(departModel1);
            for (int j = 1; j < 20; j++) {
                DepartModel departModel2 = new DepartModel();
                int id = new Random().nextInt();
                departModel2.setId(id);
                departModel2.setDeptName("部门:" + i + "," + j);
                departModel2.setParentCode(i);
                departBeans.add(departModel2);
                List<PersonModel> personModels = new ArrayList<>();
                for (int k = 1; k < 20; k++) {
                    PersonModel personModel = new PersonModel();
                    personModel.setUserId(new Random().nextInt());
                    personModel.setAccount("人员账户:" + k);
                    personModel.setUsername("人员:" + k);
                    personModel.setOrgId(id);
                    personModel.setJob("工作:" + k);
                    personModels.add(personModel);
                }
                savePersons(personModels);
            }
        }
        if (ArraysUtils.isListEmpty(departBeans)) {
            return;
        }
        SQLite.delete(DepartModel.class).query();

        DatabaseDefinition database = FlowManager.getDatabase(AppDataBase.class);
        database.executeTransaction(FastStoreModelTransaction.saveBuilder(FlowManager.getModelAdapter(DepartModel.class)).addAll(departBeans).build());
    }

    public void savePersons(List<PersonModel> personModels) {
        if (ArraysUtils.isListEmpty(personModels)) {
            return;
        }
//        SQLite.delete(PersonModel.class).query();

        DatabaseDefinition database = FlowManager.getDatabase(AppDataBase.class);
        database.beginTransactionAsync(FastStoreModelTransaction.saveBuilder(FlowManager.getModelAdapter(PersonModel.class)).addAll(personModels).build()).execute();
    }

    public Observable<List<MainInfoBean>> loadDepartData(long departId) {
        return Observable.just(departId).map(new Function<Long, List<MainInfoBean>>() {
            @Override
            public List<MainInfoBean> apply(Long aLong) throws Exception {
                PersonDiskStore personDiskStore = new PersonDiskStore();
                List<DepartModel> departListByParentId = personDiskStore.getDepartListByParentId(departId);
                List<MainInfoBean> resMainInfoBeans = new ArrayList<>();
                if (ArraysUtils.isListEmpty(departListByParentId)) {
                    return resMainInfoBeans;
                }
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

    public Observable<Long> getPersonsCount() {
        return Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return new PersonDiskStore().getPersonsCount();
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
                List<MainInfoBean> resMainInfoBeans = new ArrayList<>();
                for (PersonModel personModel : persons) {
                    MainInfoBean<PersonModel> mainInfoBean = new MainInfoBean<>();
                    mainInfoBean.setType(MainInfoBean.TYPE_PERSON);
                    mainInfoBean.setData(personModel);
                    mainInfoBean.setName(personModel.getUsername());
                    mainInfoBean.setDepartmentName(personModel.getJob());
                    resMainInfoBeans.add(mainInfoBean);
                }
                return resMainInfoBeans;
            }
        });
    }
}
