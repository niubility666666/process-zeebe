-- 查询process_id 部署的最新版本
select key_                                                              as processdefinitionkey,
       bpmn_process_id_                                                  as bpmnprocessid,
       version_                                                          as version,
       resource_                                                         as resource,
       to_char(to_timestamp(timestamp_ / 1000), 'yyyy-mm-dd hh24:mi:ss') as timestamp_
from zeebe_process
where bpmn_process_id_ = 'Process_0q58sgg'
order by version_ desc
limit 1;

-- 查询process_id 创建的实例
select t1.key_                                                          as processInstanceKey,
       t1.state_                                                        as state,
       t1.bpmn_process_id_                                              as bpmnProcessId,
       t1.version_                                                      as version,
       t1.process_definition_key_                                       as processDefinitionKey,
       to_char(to_timestamp(t1.start_ / 1000), 'yyyy-mm-dd hh24:mi:ss') as startTime,
       to_char(to_timestamp(t1.end_ / 1000), 'yyyy-mm-dd hh24:mi:ss')   as endTime
from zeebe_process_instance t1
         left join zeebe_process t2 on t1.process_definition_key_ = t2.key_
    and t1.version_ = t2.version_
where t1.bpmn_process_id_ = 'Process_0q58sgg';

-- 查询待办任务
select *
from zeebe_element_instance t1
         inner join zeebe_process_instance t2 on t1.process_definition_key_ = t2.process_definition_key_
    and t1.process_instance_key_ = t2.key_
         inner join zeebe_job t3 on t1.key_ = t3.element_instance_key_
where t2.bpmn_process_id_ = 'Process_0q58sgg'
  and t2.key_ = '2251799813776063'
  and lower(t1.intent_) = lower('element_activated')
  and lower(t3.state_) = lower('created');

SELECT t2.bpmn_process_id_ AS bpmnProcessId,
       t2.key_             AS processInstanceKey,
       t4.element_id       AS elementId,
       t4.element_name     AS elementName,
       t4.element_type     AS elementType,
       t3.key_             AS jobKey,
       t3.state_           AS jobState
FROM zeebe_element_instance t1
         INNER JOIN zeebe_process_instance t2 ON t1.process_definition_key_ = t2.process_definition_key_
    AND t1.process_instance_key_ = t2.key_
         INNER JOIN zeebe_job t3 ON t1.key_ = t3.element_instance_key_
         INNER JOIN process_element_t t4 ON t1.element_id_ = t4.element_id
WHERE t2.bpmn_process_id_ = 'Process_0q58sgg'
  AND t2.key_ = '2251799813790445'
  AND LOWER(t1.intent_) = LOWER('element_activated')
  AND LOWER(t3.state_) = LOWER('created')


--优化
SELECT t1.element_instance_key_ AS elementInstanceKey,
       t3.bpmn_process_id_      AS bpmnProcessId,
       t2.process_instance_key_ AS processInstanceKey,
       t4.element_id            AS elementId,
       t4.element_name          AS elementName,
       t4.element_type          AS elementType,
       t1.key_                  AS jobKey,
       t1.state_                AS jobState
FROM zeebe_job t1
         LEFT JOIN zeebe_element_instance t2 ON t1.element_instance_key_ = t2.key_
    AND t1.process_instance_key_ = t2.process_instance_key_
         LEFT JOIN zeebe_process_instance t3 ON t3.key_ = t2.process_instance_key_
    AND t3.process_definition_key_ = t2.process_definition_key_
         LEFT JOIN process_element_t t4 ON t4.element_id = t2.element_id_
    AND t4.VERSION = t3.version_
WHERE t3.bpmn_process_id_ = 'Process_0q58sgg'
  AND t2.process_instance_key_ = '2251799813790445'
  AND LOWER(t2.intent_) = LOWER('element_activated')
  AND LOWER(t1.state_) = LOWER('created');

SELECT t1.element_instance_key_                                             AS elementInstanceKey,
       t3.bpmn_process_id_                                                  AS bpmnProcessId,
       t2.process_instance_key_                                             AS processInstanceKey,
       t4.element_id                                                        AS elementId,
       t4.element_name                                                      AS elementName,
       t4.element_type                                                      AS elementType,
       t1.key_                                                              AS jobKey,
       t1.state_                                                            AS jobState,
       to_char(to_timestamp(t1.timestamp_ / 1000), 'YYYY-MM-DD HH24:MI:SS') AS startTime
FROM zeebe_job t1
         LEFT JOIN zeebe_element_instance t2 ON t1.element_instance_key_ = t2.key_
    AND t1.process_instance_key_ = t2.process_instance_key_
         LEFT JOIN zeebe_process_instance t3 ON t3.key_ = t2.process_instance_key_
    AND t3.process_definition_key_ = t2.process_definition_key_
         LEFT JOIN process_element_t t4 ON t4.element_id = t2.element_id_
    AND t4.VERSION = t3.version_
WHERE t3.bpmn_process_id_ = 'Process_0q58sgg'
  AND t2.process_instance_key_ = '2251799813790445'
  AND LOWER(t2.intent_) = LOWER('element_activated')
  AND LOWER(t1.state_) = LOWER('created');


-- 查询日志
SELECT t3.element_name,
       t1.element_id_,
       t1.bpmn_element_type_,
       t1.id,
       t1.intent_,
       to_char(to_timestamp(t1.timestamp_ / 1000), 'YYYY-MM-DD HH24:MI:SS')
FROM zeebe_element_instance t1
         LEFT JOIN zeebe_process_instance t2 ON t1.process_definition_key_ = t2.process_definition_key_
    AND t1.process_instance_key_ = t2.key_
         LEFT JOIN process_element_t t3 ON t1.element_id_ = t3.element_id
    AND t3.process_id = t2.bpmn_process_id_
    AND t2.version_ = t3."version"
WHERE t2.bpmn_process_id_ = 'Process_0q58sgg'
  AND t1.process_instance_key_ = '2251799813790445'
  AND t1.intent_ in ('ELEMENT_ACTIVATED', 'ELEMENT_COMPLETED')
  and t1.bpmn_element_type_ not in ('EXCLUSIVE_GATEWAY', 'PROCESS')
order by t1.timestamp_;
