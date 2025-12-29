package com.rt.engine.common.constants;

public enum ItemTypeOperation {

    /** 同意 */
    AGREE("1", "同意"),
    /** 拒绝 */
    REJECT("2", "拒绝"),
    /** 退回上一步 */
    FALLBACK("3", "退回上一步"),
    /** 退回指定节点 */
    FALLBACK_NODE("4", "退回指定节点");

    ItemTypeOperation(String id, String name) {
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
