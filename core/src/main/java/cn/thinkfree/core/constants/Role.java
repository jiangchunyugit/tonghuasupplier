package cn.thinkfree.core.constants;

/**
 * 角色信息
 *
 * @author song
 * @version 1.0
 * @date 2018/10/31 11:50
 */
public enum Role {
    /**
     * 业主
     */
    CC("CC", "业主"),

    /**
     * 设计师
     */
    CD("CD", "业主");

    public final String id;
    public final String name;

    Role(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
