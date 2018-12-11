package com.cmcc.internalcontact.model.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.cmcc.internalcontact.db.AppDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDataBase.class, name = "t_person")
public class PersonModel extends BaseModel implements Parcelable {
    @PrimaryKey
    private long userId;
    @Column
    private String account;
    @Column
    private String username;
    @Column
    private String mobile;
    @Column
    private String mobile2;
    @Column
    private String tel;
    @Column
    private String email;
    @Column
    private String orgId;
    @Column
    private String headPic;
    @Column
    private String namePinyin;
    @Column
    private long updateTime;
    @Column
    private String job;
    @Column
    private long createTime;
    @Column
    private int userSort;

    public int getUserSort() {
        return userSort;
    }

    public void setUserSort(int userSort) {
        this.userSort = userSort;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userId);
        dest.writeString(this.account);
        dest.writeString(this.username);
        dest.writeString(this.mobile);
        dest.writeString(this.mobile2);
        dest.writeString(this.tel);
        dest.writeString(this.email);
        dest.writeString(this.orgId);
        dest.writeString(this.headPic);
        dest.writeString(this.namePinyin);
        dest.writeLong(this.updateTime);
        dest.writeString(this.job);
        dest.writeLong(this.createTime);
    }

    public PersonModel() {
    }

    protected PersonModel(Parcel in) {
        this.userId = in.readLong();
        this.account = in.readString();
        this.username = in.readString();
        this.mobile = in.readString();
        this.mobile2 = in.readString();
        this.tel = in.readString();
        this.email = in.readString();
        this.orgId = in.readString();
        this.headPic = in.readString();
        this.namePinyin = in.readString();
        this.updateTime = in.readLong();
        this.job = in.readString();
        this.createTime = in.readLong();
    }

    public static final Parcelable.Creator<PersonModel> CREATOR = new Parcelable.Creator<PersonModel>() {
        @Override
        public PersonModel createFromParcel(Parcel source) {
            return new PersonModel(source);
        }

        @Override
        public PersonModel[] newArray(int size) {
            return new PersonModel[size];
        }
    };
}
