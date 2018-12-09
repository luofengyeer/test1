package com.cmcc.internalcontact.model.http;

public class LoginResponseBean {
    private String token;//token
    private long expire;//过期时间
    private UserInfo userInfo;//用户详细信息

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfo {
        private String account;
        private String username;
        private String mobile;
        private String mobile2;
        private String tel;
        private String email;
        private String orgId;
        private String job;
        private String headPic;
        private int createBy;
        private long createTime;
        private String isDisable;
        private String password;
        private long userId;

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

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public int getCreateBy() {
            return createBy;
        }

        public void setCreateBy(int createBy) {
            this.createBy = createBy;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getIsDisable() {
            return isDisable;
        }

        public void setIsDisable(String isDisable) {
            this.isDisable = isDisable;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "account='" + account + '\'' +
                    ", username='" + username + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", mobile2='" + mobile2 + '\'' +
                    ", tel='" + tel + '\'' +
                    ", email='" + email + '\'' +
                    ", orgId='" + orgId + '\'' +
                    ", job='" + job + '\'' +
                    ", headPic='" + headPic + '\'' +
                    ", createBy=" + createBy +
                    ", createTime=" + createTime +
                    ", isDisable='" + isDisable + '\'' +
                    ", password='" + password + '\'' +
                    ", userId=" + userId +
                    '}';
        }
    }
}
