package cn.thinkfree.service.designer.vo;

import cn.thinkfree.core.model.BaseModel;

public  class CaseCoverBean  extends BaseModel {


        private String coverId;
        private String coverUrl;

        public String getCoverId() {
            return coverId;
        }

        public void setCoverId(String coverId) {
            this.coverId = coverId;
        }

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }
    }