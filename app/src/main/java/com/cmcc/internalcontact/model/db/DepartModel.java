package com.cmcc.internalcontact.model.db;

import com.cmcc.internalcontact.db.AppDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDataBase.class, name = "t_dept")
public class DepartModel extends BaseModel {
    @PrimaryKey
    @Column
    private long id;//主键
    @Column
    private String deptCode;//机构单位名称
    @Column
    private long parentCode;//父级机构单位名称
    @Column
    private String deptName;//名称
    @Column
    private String shortName;//简称
    @Column
    private String deptPinyin;//拼音
    @Column
    private String updateTime;//更新时间
    @Column
    private String createTime;//创建时间
    @Column
    private String tel1;//电话
    @Column
    private String tel2;//
    @Column
    private String tel3;//
    @Column
    private String tel4;//
    @Column
    private String tel5;//
    @Column
    private String fax1;//传真
    @Column
    private String fax2;//
    @Column
    private String fax3;//
    @Column
    private String fax4;//
    @Column
    private String fax5;//
    @Column
    private String email1;//邮箱
    @Column
    private String email2;//
    @Column
    private String email3;//
    @Column
    private int type;//部门类型

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public long getParentCode() {
        return parentCode;
    }

    public void setParentCode(long parentCode) {
        this.parentCode = parentCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDeptPinyin() {
        return deptPinyin;
    }

    public void setDeptPinyin(String deptPinyin) {
        this.deptPinyin = deptPinyin;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getTel3() {
        return tel3;
    }

    public void setTel3(String tel3) {
        this.tel3 = tel3;
    }

    public String getTel4() {
        return tel4;
    }

    public void setTel4(String tel4) {
        this.tel4 = tel4;
    }

    public String getTel5() {
        return tel5;
    }

    public void setTel5(String tel5) {
        this.tel5 = tel5;
    }

    public String getFax1() {
        return fax1;
    }

    public void setFax1(String fax1) {
        this.fax1 = fax1;
    }

    public String getFax2() {
        return fax2;
    }

    public void setFax2(String fax2) {
        this.fax2 = fax2;
    }

    public String getFax3() {
        return fax3;
    }

    public void setFax3(String fax3) {
        this.fax3 = fax3;
    }

    public String getFax4() {
        return fax4;
    }

    public void setFax4(String fax4) {
        this.fax4 = fax4;
    }

    public String getFax5() {
        return fax5;
    }

    public void setFax5(String fax5) {
        this.fax5 = fax5;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
