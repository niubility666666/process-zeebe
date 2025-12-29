drop table if exists process_info_t;
drop table if exists process_info_his;
drop table if exists process_node_item_t;
drop table if exists process_element_t;
drop table if exists sys_operation_log;
drop table if exists user_role;
drop table if exists form_t;
drop table if exists form_column_t;

drop sequence if exists process_info_t_id_seq;
drop sequence if exists process_node_item_t_id_seq;

create sequence public.process_info_t_id_seq;
create sequence public.process_node_item_t_id_seq;

create table process_info_t
(
    process_id   varchar(64) not null default nextval('process_info_t_id_seq'::regclass),
    process_name varchar(64),
    form_id      varchar(64),
    bpmn_xml     text,
    is_released  int                  default 0,
    version      int,
    is_deleted   int                  default 0,
    created_by   varchar(32),
    created_time timestamp,
    updated_by   varchar(32),
    updated_time timestamp,
    primary key (process_id)
);

comment on table process_info_t is '流程信息表';
comment on column process_info_t.process_id is '流程id';
comment on column process_info_t.process_name is '流程名称';
comment on column process_info_t.form_id is '表单id';
comment on column process_info_t.bpmn_xml is '流程定义内容';
comment on column process_info_t.is_released is '0：未发布 1：已发布';
comment on column process_info_t.version is '版本';
comment on column process_info_t.is_deleted is '0：未删除 1：已删除';
comment on column process_info_t.created_by is '创建人';
comment on column process_info_t.created_time is '创建时间';
comment on column process_info_t.updated_by is '更新人';
comment on column process_info_t.updated_time is '更新时间';

create table process_node_item_t
(
    item_id      bigint not null default nextval('process_node_item_t_id_seq'::regclass),
    process_id   varchar(64),
    is_released  int             default 0,
    version      int,
    is_deleted   int             default 0,
    element_id   varchar(64),
    element_type varchar(64),
    item_name    varchar(64),
    item_type    varchar(64),
    item_value   text,
    ext1         varchar(64),
    ext2         varchar(64),
    ext3         varchar(64),
    primary key (item_id)
);

comment on table process_node_item_t is '流程节点属性表';
comment on column process_node_item_t.item_id is '属性id';
comment on column process_node_item_t.process_id is '流程id';
comment on column process_node_item_t.version is '版本';
comment on column process_node_item_t.is_released is '0：未发布 1：已发布';
comment on column process_node_item_t.is_deleted is '是否删除';
comment on column process_node_item_t.element_id is '节点id';
comment on column process_node_item_t.element_type is '节点类型';
comment on column process_node_item_t.item_name is '枚举描述';
comment on column process_node_item_t.item_type is '枚举值';
comment on column process_node_item_t.item_value is '属性值';
comment on column process_node_item_t.ext1 is '扩展字段1';
comment on column process_node_item_t.ext2 is '扩展字段2';
comment on column process_node_item_t.ext3 is '扩展字段3';

create table process_info_his
(
    process_id    varchar(64) not null,
    process_name  varchar(64),
    form_id       varchar(64),
    bpmn_xml      text,
    version       int         not null,
    deployment_id varchar(32),
    created_by    varchar(32),
    created_time  timestamp,
    primary key (process_id, version)
);

comment on table process_info_his is '流程信息历史表';
comment on column process_info_his.process_id is '流程id';
comment on column process_info_his.process_name is '流程名称';
comment on column process_info_his.form_id is '表单id';
comment on column process_info_his.bpmn_xml is '流程定义内容';
comment on column process_info_his.version is '版本';
comment on column process_info_his.deployment_id is '部署id';
comment on column process_info_his.created_by is '创建人';
comment on column process_info_his.created_time is '创建时间';

create table process_element_t
(
    process_id   varchar(64) not null,
    version      int         not null,
    element_id   varchar(64) not null,
    element_name varchar(255),
    element_type varchar(64),
    primary key (process_id, version, element_id)
);
comment on table process_element_t is '流程节点信息表';
comment on column process_element_t.process_id is '流程id';
comment on column process_element_t.version is '版本';
comment on column process_element_t.element_id is '节点id';
comment on column process_element_t.element_name is '节点名称';
comment on column process_element_t.element_type is '节点类型';

create table process_variable_t
(
    job_key              bigint not null,
    process_id           varchar(64),
    process_instance_key bigint,
    element_id           varchar(64),
    element_instance_key bigint,
    element_name         varchar(255),
    element_start_time   timestamp,
    element_end_time     timestamp,
    variable_name        varchar(64),
    variable_value       text,
    created_by           varchar(32),
    created_time         timestamp,
    primary key (job_key)
);

comment on column process_variable_t.job_key is '任务id';
comment on column process_variable_t.process_id is '流程id';
comment on column process_variable_t.process_instance_key is '流程实例id';
comment on column process_variable_t.element_id is '节点id';
comment on column process_variable_t.element_instance_key is '节点实例id';
comment on column process_variable_t.element_name is '节点名称';
comment on column process_variable_t.element_start_time is '节点开始时间';
comment on column process_variable_t.element_end_time is '节点结束时间';
comment on column process_variable_t.variable_name is '变量名称';
comment on column process_variable_t.variable_value is '变量值';
comment on column process_variable_t.created_by is '创建人';
comment on column process_variable_t.created_time is '创建时间';


create table sys_operation_log
(
    record_id   serial not null
        constraint sys_operation_log_pkey primary key,
    record_type varchar(255),
    record_desc text,
    record_time timestamp,
    operator    varchar(255)
);
comment on column sys_operation_log.record_id is '序号';
comment on column sys_operation_log.record_type is '操作类型';
comment on column sys_operation_log.record_desc is '操作描述';
comment on column sys_operation_log.record_time is '操作时间';
comment on column sys_operation_log.operator is '操作人';

CREATE TABLE user_role
(
    user_id   varchar(255),
    user_name varchar(255),
    role_id   varchar(255),
    role_name varchar(255)
);
CREATE TABLE form_t
(
    form_id   varchar(255) NOT NULL,
    form_name varchar(255),
    version   varchar(255),
    CONSTRAINT form_t_pkey PRIMARY KEY (form_id)
);
CREATE TABLE form_column_t
(
    form_id     varchar(255),
    column_name varchar(255),
    column_desc varchar(255)
);

INSERT INTO user_role (user_id, user_name, role_id, role_name)
VALUES ('zhangsan', '张三', '0001', '普通用户');
INSERT INTO user_role (user_id, user_name, role_id, role_name)
VALUES ('lisi', '李四', '0002', '信息中心管理员');
INSERT INTO user_role (user_id, user_name, role_id, role_name)
VALUES ('wangwu', '王五', '0002', '信息中心管理员');
INSERT INTO user_role (user_id, user_name, role_id, role_name)
VALUES ('zhaoliu', '赵六', '0003', '组织管理员');
INSERT INTO user_role (user_id, user_name, role_id, role_name)
VALUES ('sunqi', '孙七', '0004', '超级管理员');

INSERT INTO form_column_t (form_id, column_name, column_desc)
VALUES ('0001', 'name', '申请人姓名');
INSERT INTO form_column_t (form_id, column_name, column_desc)
VALUES ('0001', 'time', '申请时间');
INSERT INTO form_column_t (form_id, column_name, column_desc)
VALUES ('0001', 'open_level', '开放程度');
INSERT INTO form_column_t (form_id, column_name, column_desc)
VALUES ('0002', 'name', '申请人姓名');
INSERT INTO form_column_t (form_id, column_name, column_desc)
VALUES ('0002', 'role', '申请角色');

INSERT INTO form_t (form_id, form_name, version)
VALUES ('0001', '服务申请', '01');
INSERT INTO form_t (form_id, form_name, version)
VALUES ('0002', '权限申请', '01');


CREATE TABLE "public"."serviceInfo"
(
    "service_id"            varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "service_name"          varchar(32) COLLATE "pg_catalog"."default",
    "request_type"          varchar(32) COLLATE "pg_catalog"."default",
    "service_describe"      varchar(255) COLLATE "pg_catalog"."default",
    "source_in"             int4,
    "authentication_method" varchar(255) COLLATE "pg_catalog"."default",
    "interface_describe"    varchar(255) COLLATE "pg_catalog"."default",
    "interface_addr"        varchar(255) COLLATE "pg_catalog"."default",
    "request_header"        varchar(255) COLLATE "pg_catalog"."default",
    "request_param"         varchar(255) COLLATE "pg_catalog"."default",
    "request_body"          varchar(255) COLLATE "pg_catalog"."default",
    "response_body"         varchar(255) COLLATE "pg_catalog"."default",
    "request_example"       varchar(255) COLLATE "pg_catalog"."default",
    "response_example"      varchar(255) COLLATE "pg_catalog"."default",
    "delete_flag"           int4 DEFAULT 0,
    "created_by"            varchar(255) COLLATE "pg_catalog"."default",
    "created_time"          date,
    "updated_by"            varchar(255) COLLATE "pg_catalog"."default",
    "updated_time"          date,
    CONSTRAINT "services_pkey" PRIMARY KEY ("service_id")
)
;

ALTER TABLE "public"."serviceInfo"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."serviceInfo"."service_id" IS '服务id';

COMMENT ON COLUMN "public"."serviceInfo"."service_name" IS '服务名称';

COMMENT ON COLUMN "public"."serviceInfo"."request_type" IS '请求类型';

COMMENT ON COLUMN "public"."serviceInfo"."service_describe" IS '服务描述';

COMMENT ON COLUMN "public"."serviceInfo"."source_in" IS '资源来源';

COMMENT ON COLUMN "public"."serviceInfo"."authentication_method" IS '认证方式';

COMMENT ON COLUMN "public"."serviceInfo"."interface_describe" IS '接口描述';

COMMENT ON COLUMN "public"."serviceInfo"."interface_addr" IS '接口地址';

COMMENT ON COLUMN "public"."serviceInfo"."request_header" IS '请求头';

COMMENT ON COLUMN "public"."serviceInfo"."request_param" IS '请求参数';

COMMENT ON COLUMN "public"."serviceInfo"."request_body" IS '请求体';

COMMENT ON COLUMN "public"."serviceInfo"."response_body" IS '返回参数';

COMMENT ON COLUMN "public"."serviceInfo"."request_example" IS '请求示例';

COMMENT ON COLUMN "public"."serviceInfo"."response_example" IS '返回示例';

COMMENT ON COLUMN "public"."serviceInfo"."delete_flag" IS '删除状态';

COMMENT ON COLUMN "public"."serviceInfo"."created_by" IS '创建人';

COMMENT ON COLUMN "public"."serviceInfo"."created_time" IS '创建时间';

COMMENT ON COLUMN "public"."serviceInfo"."updated_by" IS '修改人';

COMMENT ON COLUMN "public"."serviceInfo"."updated_time" IS '修改时间';

COMMENT ON TABLE "public"."serviceInfo" IS '服务表';

CREATE TABLE "public"."process_param_variable"
(
    "param_id"         varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "param_name"       varchar(255) COLLATE "pg_catalog"."default",
    "process_id"       varchar(255) COLLATE "pg_catalog"."default",
    "parent_param_id"  varchar(255) COLLATE "pg_catalog"."default",
    "service_id"       varchar(255) COLLATE "pg_catalog"."default",
    "element_id"       varchar(255) COLLATE "pg_catalog"."default",
    "element_name"     varchar(255) COLLATE "pg_catalog"."default",
    "is_node"          int4,
    "is_param"         int4,
    "param_type"       varchar(255) COLLATE "pg_catalog"."default",
    "is_must"          int4,
    "value_region"     int4,
    "value_or_example" varchar(255) COLLATE "pg_catalog"."default",
    "instructions"     varchar(255) COLLATE "pg_catalog"."default",
    "created_by"       varchar(255) COLLATE "pg_catalog"."default",
    "created_time"     varchar(255) COLLATE "pg_catalog"."default",
    "updated_by"       varchar(255) COLLATE "pg_catalog"."default",
    "updated_time"     varchar(255) COLLATE "pg_catalog"."default",
    CONSTRAINT "process_param_variable_pkey" PRIMARY KEY ("param_id")
)
;

ALTER TABLE "public"."process_param_variable"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."process_param_variable"."param_id" IS '参数id';

COMMENT ON COLUMN "public"."process_param_variable"."param_name" IS '参数名称';

COMMENT ON COLUMN "public"."process_param_variable"."process_id" IS '流程id';

COMMENT ON COLUMN "public"."process_param_variable"."parent_param_id" IS 'json父级参数id';

COMMENT ON COLUMN "public"."process_param_variable"."service_id" IS '服务id';

COMMENT ON COLUMN "public"."process_param_variable"."element_id" IS '节点id';

COMMENT ON COLUMN "public"."process_param_variable"."element_name" IS '节点name';

COMMENT ON COLUMN "public"."process_param_variable"."is_node" IS '是否是节点(0流程 1节点)';

COMMENT ON COLUMN "public"."process_param_variable"."is_param" IS '是否是参数(0变量 1参数)';

COMMENT ON COLUMN "public"."process_param_variable"."param_type" IS '参数类型';

COMMENT ON COLUMN "public"."process_param_variable"."is_must" IS '是否必填(0否 1是)';

COMMENT ON COLUMN "public"."process_param_variable"."value_region" IS '值域(0全局变量 1局部变量 2 输入参数 3节点输出 4自定义)';

COMMENT ON COLUMN "public"."process_param_variable"."value_or_example" IS '值或者示例';

COMMENT ON COLUMN "public"."process_param_variable"."instructions" IS '说明';

COMMENT ON COLUMN "public"."process_param_variable"."created_by" IS '创建人';

COMMENT ON COLUMN "public"."process_param_variable"."created_time" IS '创建时间';

COMMENT ON COLUMN "public"."process_param_variable"."updated_by" IS '修改人';

COMMENT ON COLUMN "public"."process_param_variable"."updated_time" IS '修改时间';

COMMENT ON TABLE "public"."process_param_variable" IS '流程参数和变量设置';

CREATE TABLE "public"."process_exception_handle"
(
    "exception_handle_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "process_id"          varchar(255) COLLATE "pg_catalog"."default",
    "element_id"          varchar(255) COLLATE "pg_catalog"."default",
    "timeout_num"         int4,
    "timeout_unit"        varchar(32) COLLATE "pg_catalog"."default",
    "retry_num"           int4,
    "created_by"          varchar(255) COLLATE "pg_catalog"."default",
    "created_time"        timestamp(6),
    "updated_by"          varchar(255) COLLATE "pg_catalog"."default",
    "updated_time"        timestamp(6),
    CONSTRAINT "process_exception_handle_pkey" PRIMARY KEY ("exception_handle_id")
)
;

ALTER TABLE "public"."process_exception_handle"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."process_exception_handle"."exception_handle_id" IS '主键id';

COMMENT ON COLUMN "public"."process_exception_handle"."process_id" IS '流程id';

COMMENT ON COLUMN "public"."process_exception_handle"."element_id" IS '节点id';

COMMENT ON COLUMN "public"."process_exception_handle"."timeout_num" IS '超时时长';

COMMENT ON COLUMN "public"."process_exception_handle"."timeout_unit" IS '超时单位(0秒1分)';

COMMENT ON COLUMN "public"."process_exception_handle"."retry_num" IS '重试次数';

COMMENT ON COLUMN "public"."process_exception_handle"."created_by" IS '创建人';

COMMENT ON COLUMN "public"."process_exception_handle"."created_time" IS '创建时间';

COMMENT ON COLUMN "public"."process_exception_handle"."updated_by" IS '修改人';

COMMENT ON COLUMN "public"."process_exception_handle"."updated_time" IS '修改时间';

COMMENT ON TABLE "public"."process_exception_handle" IS '流程节点异常处理';
