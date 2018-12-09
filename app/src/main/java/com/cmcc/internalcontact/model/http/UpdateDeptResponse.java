package com.cmcc.internalcontact.model.http;

import com.cmcc.internalcontact.model.db.DepartModel;

import java.util.List;

public class UpdateDeptResponse {
    private long version;
    private List<DepartModel> data;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public List<DepartModel> getData() {
        return data;
    }

    public void setData(List<DepartModel> data) {
        this.data = data;
    }
}
