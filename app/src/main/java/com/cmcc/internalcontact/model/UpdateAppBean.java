package com.cmcc.internalcontact.model;

public class UpdateAppBean {
    /**
     * 名称
     */
    private String title;
    /**
     * 下载地址
     */
    private String downloadPath;
    /**
     * 文件大小
     */
    private long downloadSize;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public long getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    @Override
    public String toString() {
        return "UpdateAppBean{" +
                "title='" + title + '\'' +
                ", downloadPath='" + downloadPath + '\'' +
                ", downloadSize=" + downloadSize +
                '}';
    }
}
