package com.cmcc.internalcontact.store;

import android.text.TextUtils;
import android.util.Log;

import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.DepartModel_Table;
import com.cmcc.internalcontact.model.db.DepartPersonModel;
import com.cmcc.internalcontact.model.db.DepartPersonModel_Table;
import com.cmcc.internalcontact.model.db.PersonModel;
import com.cmcc.internalcontact.model.db.PersonModel_Table;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.util.List;

public class PersonDiskStore {
    /**
     * 根据部门查询部门
     *
     * @param departId
     * @return
     */
    public List<PersonModel> getPersonsByDepartId(String departId) {
        NameAlias deptPersonModelNameAlias = new NameAlias.Builder("DP")
                .shouldStripIdentifier(true)
                .shouldAddIdentifierToName(true).build();
        NameAlias personModelNameAlias = new NameAlias.Builder("P")
                .shouldStripIdentifier(true)
                .shouldAddIdentifierToName(true).build();
        String query = SQLite.select().from(PersonModel.class).as("P").leftOuterJoin(DepartPersonModel.class).as("DP").on(PersonModel_Table.account.withTable(personModelNameAlias)
                .eq(DepartPersonModel_Table.udAccount.withTable(deptPersonModelNameAlias))).where(DepartPersonModel_Table.udAccount.withTable(deptPersonModelNameAlias).eq(departId)).getQuery();
        Log.d("SQLHKB", "getPersonsByDepartId,query=" + query);
        return SQLite.select().from(PersonModel.class).as("P").leftOuterJoin(DepartPersonModel.class).as("DP").on(PersonModel_Table.account.withTable(personModelNameAlias)
                .eq(DepartPersonModel_Table.udAccount.withTable(deptPersonModelNameAlias))).where(DepartPersonModel_Table.udDeptCode.withTable(deptPersonModelNameAlias).eq(departId)).orderBy(PersonModel_Table.userSort, true).queryList();
    }

    /**
     * 查询人员所属部门
     *
     * @param account
     * @return
     */
    public List<DepartModel> getDepartByPersonId(String account, int departType) {
        NameAlias deptPersonModelNameAlias = new NameAlias.Builder("DP")
                .shouldStripIdentifier(true)
                .shouldAddIdentifierToName(true).build();
        NameAlias departModelNameAlias = new NameAlias.Builder("D")
                .shouldStripIdentifier(true)
                .shouldAddIdentifierToName(true).build();
        String query = SQLite.select().from(DepartModel.class).as("D").leftOuterJoin(DepartPersonModel.class)
                .as("DP").on(DepartPersonModel_Table.udDeptCode.withTable(deptPersonModelNameAlias).eq(DepartModel_Table.deptCode.withTable(departModelNameAlias)))
                .where(DepartPersonModel_Table.udAccount.withTable(deptPersonModelNameAlias).eq(account)).getQuery();
        return SQLite.select().from(DepartModel.class).as("D").leftOuterJoin(DepartPersonModel.class)
                .as("DP").on(DepartPersonModel_Table.udDeptCode.withTable(deptPersonModelNameAlias).eq(DepartModel_Table.deptCode.withTable(departModelNameAlias)))
                .where(DepartPersonModel_Table.udAccount.withTable(deptPersonModelNameAlias).eq(account)).and(DepartModel_Table.deptType.withTable(departModelNameAlias).eq(departType)).queryList();
    }

    /**
     * 查询人员所属部门
     *
     * @param account
     * @return
     */
    public DepartModel getDepartByPersonId(String account, String departCode) {
        NameAlias deptPersonModelNameAlias = new NameAlias.Builder("DP")
                .shouldStripIdentifier(true)
                .shouldAddIdentifierToName(true).build();
        NameAlias departModelNameAlias = new NameAlias.Builder("D")
                .shouldStripIdentifier(true)
                .shouldAddIdentifierToName(true).build();
        String query = SQLite.select().from(DepartModel.class).as("D").leftOuterJoin(DepartPersonModel.class)
                .as("DP").on(DepartPersonModel_Table.udDeptCode.withTable(deptPersonModelNameAlias).eq(DepartModel_Table.deptCode.withTable(departModelNameAlias)))
                .where(DepartPersonModel_Table.udAccount.withTable(deptPersonModelNameAlias).eq(account)).getQuery();
        Where<DepartModel> where = SQLite.select().from(DepartModel.class).as("D").leftOuterJoin(DepartPersonModel.class)
                .as("DP").on(DepartPersonModel_Table.udDeptCode.withTable(deptPersonModelNameAlias).eq(DepartModel_Table.deptCode.withTable(departModelNameAlias)))
                .where(DepartPersonModel_Table.udAccount.withTable(deptPersonModelNameAlias).eq(account));
        if (!TextUtils.isEmpty(departCode)) {
            where.and(DepartModel_Table.deptCode.withTable(departModelNameAlias).eq(departCode));
        }
        return where.querySingle();
    }

    /**
     * 根据父部门id获取部门
     *
     * @param parentId
     * @return
     */
    public List<DepartModel> getDepartListByParentId(String parentId) {
        return SQLite.select().from(DepartModel.class).where(DepartModel_Table.parentCode.eq(parentId)).queryList();
    }

    /**
     * 根据部门id获取父部门
     *
     * @param parentId
     * @return
     */
    public DepartModel getParentDepartByDepartId(String parentId) {
        return SQLite.select().from(DepartModel.class).where(DepartModel_Table.parentCode.eq(parentId)).querySingle();
    }

    /**
     * 获取联系人详情
     *
     * @param personId
     * @return
     */
    public PersonModel getPersonDetailById(long personId) {
        return SQLite.select().from(PersonModel.class).where(PersonModel_Table.userId.eq(personId)).querySingle();
    }

    public long getPersonsCount() {
        return SQLite.selectCountOf().from(PersonModel.class).count();
    }

    public PersonModel getPersonsByPhone(String phone) {
        if (phone == null) {
            return null;
        }
        return SQLite.select().from(PersonModel.class).where(PersonModel_Table.mobile.eq(phone)).or(PersonModel_Table.mobile2.eq(phone)).querySingle();
    }

    /**
     * 检索联系人
     *
     * @param search 检索内容
     * @return 匹配检索内容的联系人信息集合
     */
    public List<PersonModel> searchPersonList(String search) {
        return SQLite.select().from(PersonModel.class)
                .where(PersonModel_Table.username.like(search))
                .or(PersonModel_Table.namePinyin.like(search))
                .queryList();
    }

    public List<DepartModel> getDepartByCodes(List<String> departIds) {
        return SQLite.select().from(DepartModel.class).where(DepartModel_Table.deptCode.in(departIds)).queryList();
    }
}
