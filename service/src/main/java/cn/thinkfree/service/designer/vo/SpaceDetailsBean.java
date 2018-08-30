package cn.thinkfree.service.designer.vo;

import cn.thinkfree.core.model.BaseModel;

import java.util.List;

public  class SpaceDetailsBean extends BaseModel {


        private String roomTypeCode;
        private Object roomId;
        private String description;
        private List<RenderImgsBeanX> renderImgs;

        public String getRoomTypeCode() {
            return roomTypeCode;
        }

        public void setRoomTypeCode(String roomTypeCode) {
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

        public List<RenderImgsBeanX> getRenderImgs() {
            return renderImgs;
        }

        public void setRenderImgs(List<RenderImgsBeanX> renderImgs) {
            this.renderImgs = renderImgs;
        }


    }