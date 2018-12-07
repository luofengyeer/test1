package com.cmcc.internalcontact.store;

import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.DepartModel_Table;
import com.cmcc.internalcontact.model.db.PersonModel;
import com.cmcc.internalcontact.model.db.PersonModel_Table;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class PersonDiskStore {
    /**
     * 根据部门查询部门
     *
     * @param departId
     * @return
     */
    public List<PersonModel> getPersonsByDepartId(long departId) {
        return SQLite.select().from(PersonModel.class).where(PersonModel_Table.orgId.eq(departId)).queryList();
    }

    /**
     * 查询人员所属部门
     *
     * @param personId
     * @return
     */
    public DepartModel getDepartByPersonId(long personId) {
        NameAlias deptPersonModelNameAlias = new NameAlias.Builder("DP")
                .shouldStripIdentifier(true)
                .shouldAddIdentifierToName(true).build();
        NameAlias departModelNameAlias = new NameAlias.Builder("D")
                .shouldStripIdentifier(true)
                .shouldAddIdentifierToName(true).build();
        return SQLite.select().from(DepartModel.class).as("D").leftOuterJoin(PersonModel.class)
                .as("P").on(PersonModel_Table.orgId.withTable(deptPersonModelNameAlias).eq(DepartModel_Table.id.withTable(departModelNameAlias)))
                .where(PersonModel_Table.userId.withTable(deptPersonModelNameAlias).eq(personId)).querySingle();
    }

    /**
     * 根据父部门id获取部门
     *
     * @param parentId
     * @return
     */
    public List<DepartModel> getDepartListByParentId(long parentId) {
        return SQLite.select().from(DepartModel.class).where(DepartModel_Table.parentCode.eq(parentId)).queryList();
    }

    /**
     * 根据部门id获取父部门
     *
     * @param parentId
     * @return
     */
    public DepartModel getParentDepartByDepartId(long parentId) {
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
}
