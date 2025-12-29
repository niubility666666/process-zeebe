package com.rt.engine.common.constants;

import lombok.Getter;


public enum CodeEnum {
    /**
     * 处理成功
     */
    SUCCESS("200", "处理成功"),
    /**
     * 参数类型错误
     */
    PARAMETER_TYPE_ERROR("4000001", "参数类型错误"),
    /**
     * 必填参数为空
     */
    REQUIRED_PARAMETER_EMPTY("4000002", "必填参数为空"),
    /**
     * 参数错误
     */
    PARAMETER_ERROR("4000003", "参数错误"),
    /**
     * 未登录
     */
    NOT_LOGIN("4010001", "用户未登录"),
    /**
     * 无权限访问
     */
    NO_PERMISSION("4010003", "无权限访问"),
    /**
     * 系统异常
     */
    UNKNOWN("5000001", "系统异常"),

    /**
     * 流程删除异常
     */
    PROCESS_INFO_ALREADY_PUBLISH("5000002", "[%s] 为已发布的流程，不允许删除"),
    /**
     * 流程模板已存在
     */
    PROCESS_EXISTS("5000003", "流程模板已存在"),
    /**
     * 流程模板不存在
     */
    PROCESS_NOT_EXISTS("5000004", "流程模板不存在"),
    /**
     * 流程模板格式错误
     */
    BPMN_FORMAT_EXCEPTION("5000005", "流程模板格式错误"),
    /**
     * 流程模板名称为空
     */
    PROCESS_NAME_EMPTY("5000006", "流程模板名称为空"),
    /**
     * 结束节点不能为空
     */
    PROCESS_END_EVENT_EMPTY("5000007", "流程中【%s】节点不能为空"),
    /**
     * 流程未发布
     */
    PROCESS_NOT_RELEASE("5000008", "流程未发布"),

    /**
     * 任务不存在
     */
    JOB_NOT_EXISTS("5000009", "任务不存在"),

    /**
     * 当前解决不具有权限
     */
    AUTHORITY_NOT("5000010", "当前节点不具有【%s：%s】权限"),

    /**
     * 未找到当前权限
     */
    AUTHORITY_NOT_DIS("5000011", "未找到【%s】权限"),

    /**
     * 暂时不支持节点类型
     */
    TASK_NOT_SUPPORT("5000012", "暂时不支持【%s : %s】节点类型"),

    /**
     * 流程已完成
     */
    TASK_STATE_COMPLETED("5000013", "流程已完成, 不能取消"),

    /**
     * 未找到当前权限
     */
    TASK_STATE_TERMINATED("5000014", "流程已取消, 不能再次取消"),

    /**
     * 未找到当前权限
     */
    TASK_NOT("5000015", "请为当前节点【%s】配置人员信息和至少一个动作权限");
    /**
     * 构造函数
     * 
     * @param code
     *            响应码
     * @param desc
     *            响应描述
     */
    CodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Getter
    private final String code;

    @Getter
    private final String desc;

}