package cn.thinkfree.service.designer.vo;

import cn.thinkfree.core.model.BaseModel;

import java.util.List;

public  class RenderImgsBeanX extends BaseModel {

            private String renderId;
            private int status;
            private int selectStatus;
            private String renderType;
            private String coverUrl;
            private Object photoUrl;
            private String photo360Url;
            private String subRenderingType;
            private List<?> skus;

            public String getRenderId() {
                return renderId;
            }

            public void setRenderId(String renderId) {
                this.renderId = renderId;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getSelectStatus() {
                return selectStatus;
            }

            public void setSelectStatus(int selectStatus) {
                this.selectStatus = selectStatus;
            }

            public String getRenderType() {
                return renderType;
            }

            public void setRenderType(String renderType) {
                this.renderType = renderType;
            }

            public String getCoverUrl() {
                return coverUrl;
            }

            public void setCoverUrl(String coverUrl) {
                this.coverUrl = coverUrl;
            }

            public Object getPhotoUrl() {
                return photoUrl;
            }

            public void setPhotoUrl(Object photoUrl) {
                this.photoUrl = photoUrl;
            }

            public String getPhoto360Url() {
                return photo360Url;
            }

            public void setPhoto360Url(String photo360Url) {
                this.photo360Url = photo360Url;
            }

            public String getSubRenderingType() {
                return subRenderingType;
            }

            public void setSubRenderingType(String subRenderingType) {
                this.subRenderingType = subRenderingType;
            }

            public List<?> getSkus() {
                return skus;
            }

            public void setSkus(List<?> skus) {
                this.skus = skus;
            }
        }