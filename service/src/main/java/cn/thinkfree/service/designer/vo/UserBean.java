package cn.thinkfree.service.designer.vo;

import cn.thinkfree.core.model.BaseModel;

public  class UserBean extends BaseModel {


        private String id;
        private String name;
        private String avatarUrl;
        private int certifyStatus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public int getCertifyStatus() {
            return certifyStatus;
        }

        public void setCertifyStatus(int certifyStatus) {
            this.certifyStatus = certifyStatus;
        }
    }