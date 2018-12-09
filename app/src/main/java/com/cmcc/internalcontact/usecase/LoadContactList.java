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
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class LoadContactList {
    public void saveDepartments(List<DepartModel> departBeans) {
     /*   departBeans = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            DepartModel departModel1 = new DepartModel();
            departModel1.setDeptName("机构:" + i);
            departModel1.setId(i);
            departModel1.setDeptType(Constant.TYPE_MECHANISM);
            departBeans.add(departModel1);
            for (int j = 1; j < 3; j++) {
                DepartModel departModel2 = new DepartModel();
                int id = new Random().nextInt();
                departModel2.setId(id);
                departModel2.setDeptName("单位:" + i + "," + j);
                departModel2.setParentCode(i);
                departModel2.setDeptType(Constant.TYPE_COMPANY);
                departBeans.add(departModel2);
                List<PersonModel> personModels = new ArrayList<>();
                for (int k = 1; k < 3; k++) {
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
        }*/
        if (ArraysUtils.isListEmpty(departBeans)) {
            return;
        }
        SQLite.delete(DepartModel.class).query();

        DatabaseDefinition database = FlowManager.getDatabase(AppDataBase.class);
        database.executeTransaction(FastStoreModelTransaction.saveBuilder(FlowManager.getModelAdapter(DepartModel.class)).addAll(departBeans).build());
//        for (int i = 0; i < 5; i++) {
//            DepartModel departModel = new DepartModel();
//            departModel.setDeptType(Constant.TYPE_MECHANISM);
//            departModel.setCreateTime(1000 + i + "");
//            departModel.setDeptName("测试机构" + i);
//            departModel.setId(i);
//            departModel.save();
//        }
//        for (int i = 0; i < 5; i++) {
//            DepartModel departModel = new DepartModel();
//            departModel.setDeptType(Constant.TYPE_COMPANY);
//            departModel.setCreateTime(2000 + i + "");
//            departModel.setDeptName("测试单位" + i);
//            departModel.setId(2000 + i);
//            departModel.setParentCode(i+"");
//            departModel.save();
//        }
//        for (int i = 0; i < 5; i++) {
//            PersonModel personModel = new PersonModel();
//            personModel.setUserId(i);
//            personModel.setAccount("测试联系人:" + i);
//            personModel.setUsername("测试联系人:" + i);
//            personModel.setOrgId(2000 + i+"");
//            personModel.setJob("测试联系人工作:" + i);
//            personModel.setHeadPic("https://gss1.bdstatic.com/9vo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=09cb6e95de2a283457ab3e593adca28f/c8177f3e6709c93d195d707f953df8dcd000548e.jpg");
//            personModel.save();
//        }
    }

    public void savePersons(List<PersonModel> personModels) {
        if (ArraysUtils.isListEmpty(personModels)) {
            return;
        }
        SQLite.delete(PersonModel.class).query();

        DatabaseDefinition database = FlowManager.getDatabase(AppDataBase.class);
        database.beginTransactionAsync(FastStoreModelTransaction.saveBuilder(FlowManager.getModelAdapter(PersonModel.class)).addAll(personModels).build()).execute();
    }

    public Observable<List<MainInfoBean>> loadDepartData(String departId) {
        return Observable.just(departId).map(new Function<String, List<MainInfoBean>>() {
            @Override
            public List<MainInfoBean> apply(String aLong) throws Exception {
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

    public Observable<List<MainInfoBean>> loadPersons(String departId) {
        return Observable.just(departId).map(new Function<String, List<MainInfoBean>>() {
            @Override
            public List<MainInfoBean> apply(String aLong) throws Exception {
                PersonDiskStore personDiskStore = new PersonDiskStore();
                List<PersonModel> persons = personDiskStore.getPersonsByDepartId(aLong);
                List<MainInfoBean> resMainInfoBeans = new ArrayList<>();
                if (ArraysUtils.isListEmpty(persons)) {
                    return resMainInfoBeans;
                }
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
