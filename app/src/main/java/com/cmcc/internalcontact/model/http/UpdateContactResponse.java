package com.cmcc.internalcontact.model.http;

import com.cmcc.internalcontact.model.db.PersonModel;

import java.util.List;

public class UpdateContactResponse {
    private long version;
    private List<PersonModel> data;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public List<PersonModel> getData() {
        return data;
    }

    public void setData(List<PersonModel> data) {
        this.data = data;
    }
}
