package com.cmcc.internalcontact.model.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.cmcc.internalcontact.db.AppDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDataBase.class, name = "t_dept")
public class DepartModel extends BaseModel implements Parcelable {
    @PrimaryKey
    @Column
    private long id;//主键
    @Column
    private String deptCode;//机构单位名称
    @Column
    private String parentCode;//父级机构单位名称
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
    private int deptType;//部门类型
    @Column
    private int treeSort;//部门排序

    public int getTreeSort() {
        return treeSort;
    }

    public void setTreeSort(int treeSort) {
        this.treeSort = treeSort;
    }

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

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
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

    public int getDeptType() {
        return deptType;
    }

    public void setDeptType(int deptType) {
        this.deptType = deptType;
    }

    @Override
    public String toString() {
        return "DepartModel{" +
                "id=" + id +
                ", deptCode='" + deptCode + '\'' +
                ", parentCode=" + parentCode +
                ", deptName='" + deptName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", deptPinyin='" + deptPinyin + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", createTime='" + createTime + '\'' +
                ", tel1='" + tel1 + '\'' +
                ", tel2='" + tel2 + '\'' +
                ", tel3='" + tel3 + '\'' +
                ", tel4='" + tel4 + '\'' +
                ", tel5='" + tel5 + '\'' +
                ", fax1='" + fax1 + '\'' +
                ", fax2='" + fax2 + '\'' +
                ", fax3='" + fax3 + '\'' +
                ", fax4='" + fax4 + '\'' +
                ", fax5='" + fax5 + '\'' +
                ", email1='" + email1 + '\'' +
                ", email2='" + email2 + '\'' +
                ", email3='" + email3 + '\'' +
                ", deptType=" + deptType +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DepartModel) {
            return ((DepartModel) obj).getId() == getId();
        }
        return super.equals(obj);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.deptCode);
        dest.writeString(this.parentCode);
        dest.writeString(this.deptName);
        dest.writeString(this.shortName);
        dest.writeString(this.deptPinyin);
        dest.writeString(this.updateTime);
        dest.writeString(this.createTime);
        dest.writeString(this.tel1);
        dest.writeString(this.tel2);
        dest.writeString(this.tel3);
        dest.writeString(this.tel4);
        dest.writeString(this.tel5);
        dest.writeString(this.fax1);
        dest.writeString(this.fax2);
        dest.writeString(this.fax3);
        dest.writeString(this.fax4);
        dest.writeString(this.fax5);
        dest.writeString(this.email1);
        dest.writeString(this.email2);
        dest.writeString(this.email3);
        dest.writeInt(this.deptType);
    }

    public DepartModel() {
    }

    protected DepartModel(Parcel in) {
        this.id = in.readLong();
        this.deptCode = in.readString();
        this.parentCode = in.readString();
        this.deptName = in.readString();
        this.shortName = in.readString();
        this.deptPinyin = in.readString();
        this.updateTime = in.readString();
        this.createTime = in.readString();
        this.tel1 = in.readString();
        this.tel2 = in.readString();
        this.tel3 = in.readString();
        this.tel4 = in.readString();
        this.tel5 = in.readString();
        this.fax1 = in.readString();
        this.fax2 = in.readString();
        this.fax3 = in.readString();
        this.fax4 = in.readString();
        this.fax5 = in.readString();
        this.email1 = in.readString();
        this.email2 = in.readString();
        this.email3 = in.readString();
        this.deptType = in.readInt();
    }

    public static final Parcelable.Creator<DepartModel> CREATOR = new Parcelable.Creator<DepartModel>() {
        @Override
        public DepartModel createFromParcel(Parcel source) {
            return new DepartModel(source);
        }

        @Override
        public DepartModel[] newArray(int size) {
            return new DepartModel[size];
        }
    };
}
