package com.cmcc.internalcontact.model.http;

public class GetWaitImgBean {
    private long createTime;
    private String desc;
    private int id;
    private String imgUrl;
    private String isDisabled;
    private int sort;
    private String type;

    /* "createTime": 1544420893000,
                "desc": "等待页2",
                "id": 2,
                "imgUrl": "/headPic/2018-12/2af45c1c62a54766b0579a686631cf78.png",
                "isDisabled": "0",
                "sort": 2,
                "type": "1"*/
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(String isDisabled) {
        this.isDisabled = isDisabled;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
