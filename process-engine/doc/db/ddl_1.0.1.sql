drop table if exists service_t;
create table service_t
(
    service_id    varchar(64),
    service_name  varchar(255),
    http_method   varchar(32),
    service_url   varchar(255),
    service_desc  text,
    request_param text,
    request_body  text,
    response      text,
    auth_type     varchar(32),
    auth_value    varchar(255),
    source        varchar(32),
    delete_flag   int2 default 0,
    created_by    varchar(32),
    created_time  timestamp,
    updated_by    varchar(32),
    updated_time  timestamp,
    constraint service_pkey primary key (service_id)
);
comment on column service_t.service_id is '服务id';
comment on column service_t.service_name is '服务名称';
comment on column service_t.http_method is '请求类型';
comment on column service_t.service_url is '接口地址';
comment on column service_t.service_desc is '服务描述';
comment on column service_t.request_param is '请求参数';
comment on column service_t.request_body is '请求体';
comment on column service_t.response is '返回参数';
comment on column service_t.auth_type is '认证方式';
comment on column service_t.auth_value is '授权值';
comment on column service_t.source is '来源';
comment on column service_t.delete_flag is '删除状态';
comment on column service_t.created_by is '创建人';
comment on column service_t.created_time is '创建时间';
comment on column service_t.updated_by is '修改人';
comment on column service_t.updated_time is '修改时间';
comment on table service_t is '服务表';