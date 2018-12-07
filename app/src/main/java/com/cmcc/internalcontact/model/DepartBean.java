package com.cmcc.internalcontact.model;

public class DepartBean {

    private long id;
    private String name;
    private long parentDepartId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getParentDepartId() {
        return parentDepartId;
    }

    public void setParentDepartId(long parentDepartId) {
        this.parentDepartId = parentDepartId;
    }
}
