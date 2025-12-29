package com.rt.engine.common.constants;

public enum ItemTypeEnum {
    /** 用户 */
    ITEM_TYPE_USER("1", "user"),
    /** 角色 */
    ITEM_TYPE_ROLE("2", "role"),
    /** 操作 */
    ITEM_TYPE_OPERATION("3", "operation"),
    /** 条件表达式 */
    ITEM_TYPE_EXPRESSION("4", "expression");

    private ItemTypeEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private String id;

    private String name;

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
}
