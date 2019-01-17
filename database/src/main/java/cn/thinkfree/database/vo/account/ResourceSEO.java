package cn.thinkfree.database.vo.account;

import cn.thinkfree.core.model.BaseModel;

public class ResourceSEO extends BaseModel {

    /**
     * 权限主键
     */
    private String permissionID;
    /**
     * 资源主键
     */
    private String resourceID;
    /**
     * 查询类型
     */
    private String type;
    /**
     * 适用平台
     */
    private String platform;

    public String getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(String permissionID) {
        this.permissionID = permissionID;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public static class Builder {
        private ResourceSEO condition;
        public Builder(){
            condition = new ResourceSEO();
        }
        public Builder permissionID(Integer permissionID){
            if(permissionID!= null){
                condition.setPermissionID(permissionID.toString());
            }
            return this;
        }
        public Builder platform(Integer platform){
            if(platform != null){
                condition.setPlatform(platform.toString());
            }
            return this;
        }

        public Builder type(Integer type){
            if(type != null){
                condition.setType(type.toString());
            }
            return this;
        }
        public Builder resourceID(Integer resourceID){
            if(resourceID != null){
                condition.setResourceID(resourceID.toString());
            }
            return this;
        }

        public ResourceSEO build(){
            return condition;
        }
    }

}
