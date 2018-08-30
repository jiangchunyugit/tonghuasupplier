package cn.thinkfree.service.designer.vo;

import cn.thinkfree.core.model.BaseModel;

import java.util.List;

public   class NaviPanosBean  extends BaseModel {


        private Object roomTypeCode;
        private Object roomId;
        private String description;
        private List<RenderImgsBean> renderImgs;

        public Object getRoomTypeCode() {
            return roomTypeCode;
        }

        public void setRoomTypeCode(Object roomTypeCode) {
            this.roomTypeCode = roomTypeCode;
        }

        public Object getRoomId() {
            return roomId;
        }

        public void setRoomId(Object roomId) {
            this.roomId = roomId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<RenderImgsBean> getRenderImgs() {
            return renderImgs;
        }

        public void setRenderImgs(List<RenderImgsBean> renderImgs) {
            this.renderImgs = renderImgs;
        }

        public static class RenderImgsBean {


            private String renderId;
            private int status;
            private int selectStatus;
            private String renderType;
            private String coverUrl;
            private Object photoUrl;
            private String photo360Url;
            private Object skus;
            private Object subRenderingType;

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

            public Object getSkus() {
                return skus;
            }

            public void setSkus(Object skus) {
                this.skus = skus;
            }

            public Object getSubRenderingType() {
                return subRenderingType;
            }

            public void setSubRenderingType(Object subRenderingType) {
                this.subRenderingType = subRenderingType;
            }
        }
    }