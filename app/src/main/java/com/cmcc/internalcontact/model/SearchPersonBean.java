package com.cmcc.internalcontact.model;

import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.PersonModel;

public class SearchPersonBean {
    private PersonModel personModel;
    private DepartModel departModel;

    public PersonModel getPersonModel() {
        return personModel;
    }

    public void setPersonModel(PersonModel personModel) {
        this.personModel = personModel;
    }

    public DepartModel getDepartModel() {
        return departModel;
    }

    public void setDepartModel(DepartModel departModel) {
        this.departModel = departModel;
    }
}
