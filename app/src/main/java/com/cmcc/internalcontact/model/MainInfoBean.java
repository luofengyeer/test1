package com.cmcc.internalcontact.model;

public class MainInfoBean<T> {
    public static final int TYPE_PERSON = 1;
    public static final int TYPE_DEPART = 2;
    private T data;
    private int type;
    private String name;
    private String departmentName;
    private String avatar;

    public T getData() {
        return data;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
