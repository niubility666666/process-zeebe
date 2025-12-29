package com.rt.engine.common.constants;

public interface EngineConstants {

    /**
     * 未发布
     */
    Integer RELEASE_ZERO = 0;
    /**
     * 已发布
     */
    Integer RELEASE_ONE = 1;
    /**
     * 前缀字符
     */
    String KEY_PREFIX = "BG-";
    /**
     * 前缀字符
     */
    String USER_ID = "user_id";
    /**
     * cookie中token的name
     */
    String COOKIE_TOKEN_NAME = "bgToken";
    /**
     * 审批变量名称
     */
    String VARIABLE_APPROVE = "approve";

    /**
     * 流程状态-完成
     */
    String INSTANCE_STATE_ACTIVE = "Active";
    /**
     * 流程状态-完成
     */
    String INSTANCE_STATE_COMPLETED = "Completed";
    /**
     * 流程状态-终止
     */
    String INSTANCE_STATE_TERMINATED = "Terminated";
    /**
     * 接口鉴权请求参数key
     */
    String INTERFACE_AUTHORIZATION = "authorization";

    /**
     * 节点激活
     */
    String ELEMENT_ACTIVATED = "ELEMENT_ACTIVATED";

    /**
     * 节点完成
     */
    String ELEMENT_COMPLETED = "ELEMENT_COMPLETED";

    /**
     * 节点终止
     */
    String ELEMENT_TERMINATED = "ELEMENT_TERMINATED";
}
