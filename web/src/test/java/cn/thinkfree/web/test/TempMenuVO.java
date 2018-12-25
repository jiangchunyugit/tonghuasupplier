package cn.thinkfree.web.test;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class TempMenuVO {

    /**
     * path : /designPlatform
     * component : Layout
     * redirect : /designPlatform/designerList
     * name : DesignPlatform
     * hidden : true
     * meta : {"title":"设计平台","icon":"example"}
     * children : [{"path":"designerList","hidden":true,"name":"designerList","redirect":"/designPlatform/designerList","component":"/designPlatform/designerList/index","meta":{"title":"设计师列表","icon":"example"}},{"path":"/createOrder","name":"createOrder","hidden":true,"component":"/designPlatform/designerList/createOrder","meta":{"title":"创建订单"}},{"path":"establish","name":"PlatformEstablish","component":"/designPlatform/designerList/certification/establish/index","meta":{"title":"添加设计师"}},{"path":"details","name":"PlatformDetails","component":"/designPlatform/designerList/certification/details/index","meta":{"title":"设计师详情"},"hidden":true},{"path":"dispatch","name":"PlatformDispatch","component":"/designPlatform/dispatch/index","meta":{"title":"设计派单管理"}},{"path":"orderManagement","name":"PlatformOrderManagement","component":"/designPlatform/orderManagement/index","meta":{"title":"设计订单管理"}},{"path":"designOrderDetails","hidden":true,"name":"designOrderDetails","component":"/designPlatform/orderManagement/designOrderDetails/index","meta":{"title":"设计订单详情"}},{"path":"contract","name":"PlatformContract","component":"/designPlatform/contract/index","meta":{"title":"设计合同管理"}}]
     */

    private String path;
    private String component;
    private String redirect;
    private String name;
    private boolean hidden;
    private MetaBean meta;
    private java.util.List<TempMenuVO> children;

    public TempMenuVO() {
        meta = new MetaBean();
        children = new ArrayList<>();
    }



    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public List<TempMenuVO> getChildren() {
        return children;
    }

    public void setChildren(List<TempMenuVO> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "TempMenuVO{" +
                "path='" + path + '\'' +
                ", component='" + component + '\'' +
                ", redirect='" + redirect + '\'' +
                ", name='" + name + '\'' +
                ", hidden=" + hidden +
                ", meta=" + meta +
                ", children=" + children +
                '}';
    }

    public static class MetaBean {
        /**
         * title : 设计平台
         * icon : example
         */

        private String title;
        private String icon;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        @Override
        public String toString() {
            return "MetaBean{" +
                    "title='" + title + '\'' +
                    ", icon='" + icon + '\'' +
                    '}';
        }
    }

}
