package com.cmcc.internalcontact.store;

import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.DepartModel_Table;
import com.cmcc.internalcontact.utils.Constant;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class DepartDiskStore {
    /**
     * 检索机构信息集合
     *
     * @param search 检索内容
     * @return 匹配检索内容的机构信息集合
     */
    public List<DepartModel> searchMechanismList(String search) {
        return searchDepartList(search, Constant.TYPE_MECHANISM);
    }

    /**
     * 检索单位信息集合
     *
     * @param search 检索内容
     * @return 匹配检索内容的单位信息集合
     */
    public List<DepartModel> searchCompanyList(String search) {
        return searchDepartList(search, Constant.TYPE_COMPANY);
    }

    /**
     * 检索部门信息集合
     *
     * @param search 检索内容
     * @param type   部门类型
     * @return 匹配检索内容的部门信息集合
     */
    public List<DepartModel> searchDepartList(String search, int type) {
        OperatorGroup operators = OperatorGroup
                .clause(OperatorGroup.clause().and(DepartModel_Table.deptName.like(search)).and(DepartModel_Table.deptType.is(type)))
                .or(OperatorGroup.clause().and(DepartModel_Table.shortName.like(search)).and(DepartModel_Table.deptType.is(type)))
                .or(OperatorGroup.clause().and(DepartModel_Table.deptPinyin.like(search)).and(DepartModel_Table.deptType.is(type)));
        return SQLite.select().from(DepartModel.class).where(operators).queryList();
    }

    /**
     * 根据部门id获取部门信息
     *
     * @param departId 部门id
     * @return 对应id的部门信息
     */
    public DepartModel getDepartModeByDepartId(String departId) {
        return SQLite.select().from(DepartModel.class)
                .where(DepartModel_Table.deptCode.is(departId))
                .querySingle();
    }
}
