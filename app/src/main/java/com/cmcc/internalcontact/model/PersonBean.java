package com.cmcc.internalcontact.model;

import com.cmcc.internalcontact.model.db.DepartModel;

public class PersonBean {
    private long id;
    private String name;
    private String phone;
    private String avator;
    private String tel;
    private String email;
    private DepartModel mechanism;//机构
    private DepartModel depart;//单位
    private String job;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
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

    public DepartModel getMechanism() {
        return mechanism;
    }

    public void setMechanism(DepartModel mechanism) {
        this.mechanism = mechanism;
    }

    public DepartModel getDepart() {
        return depart;
    }

    public void setDepart(DepartModel depart) {
        this.depart = depart;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
