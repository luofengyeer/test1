package com.cmcc.internalcontact.activity.adapter;

import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.PersonModel;

public interface ISearchListCallBack {
    /**
     * 点击机构
     */
    void clickMechanism(DepartModel departModel);

    /**
     * 点击单位
     */
    void clickCompany(DepartModel departModel);

    /**
     * 点击联系人
     */
    void clickContact(PersonModel personModel);
}
