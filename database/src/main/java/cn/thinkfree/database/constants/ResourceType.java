package cn.thinkfree.database.constants;

/**
 * 资源类型
 */
public enum ResourceType {
    /**
     * 根
     */
    ROOT(0),
    /**
     * 菜单
     */
    MENU(1),
    /**
     * 功能
     */
    FUNCTION(2);

    public final Integer code;
    ResourceType(Integer code) {
        this.code = code;
    }
}
