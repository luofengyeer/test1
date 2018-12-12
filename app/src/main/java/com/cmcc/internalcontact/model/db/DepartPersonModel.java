package com.cmcc.internalcontact.model.db;

import com.cmcc.internalcontact.db.AppDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDataBase.class, name = "t_depart_person")
public class DepartPersonModel extends BaseModel {
    @PrimaryKey
    @Column
    private String udId;//主键
    @Column
    private String udAccount;//帐号
    @Column
    private String udDeptCode;//机构代码
    @Column
    private long udSort;//人员排序

    public long getUdSort() {
        return udSort;
    }

    public void setUdSort(long udSort) {
        this.udSort = udSort;
    }

    public String getUdId() {
        return udId;
    }

    public void setUdId(String udId) {
        this.udId = udId;
    }

    public String getUdAccount() {
        return udAccount;
    }

    public void setUdAccount(String udAccount) {
        this.udAccount = udAccount;
    }

    public String getUdDeptCode() {
        return udDeptCode;
    }

    public void setUdDeptCode(String udDeptCode) {
        this.udDeptCode = udDeptCode;
    }
}
