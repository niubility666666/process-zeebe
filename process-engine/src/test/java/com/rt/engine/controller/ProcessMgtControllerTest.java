package com.rt.engine.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.rt.engine.JunitBase;
import com.rt.engine.bean.dto.NodeItem;
import com.rt.engine.bean.request.SaveProcessInfoReq;
import com.rt.engine.bean.response.BaseResponse;
import com.rt.engine.common.constants.ElementType;
import com.rt.engine.common.constants.EngineConstants;

import io.camunda.zeebe.model.bpmn.Bpmn;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;

/**
 * @author wuwanli
 * @date 2021/11/1
 */
public class ProcessMgtControllerTest extends JunitBase {

    @Resource
    private ProcessMgtController processMgtController;

    @Test
    public void testSaveProcessInfo() throws FileNotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        SaveProcessInfoReq saveProcessInfoReq = new SaveProcessInfoReq();
        File file = new File("C:\\Users\\wuwanli\\Downloads\\test.bpmn");
        BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(new FileInputStream(file));
        saveProcessInfoReq.setFormId("123");
        saveProcessInfoReq.setBpmnXml(Bpmn.convertToString(bpmnModelInstance));
        List<NodeItem> nodeItemList = new ArrayList<>();
        NodeItem userTaskItem = new NodeItem();
        userTaskItem.setElementId("Activity_1vfxio6");
        userTaskItem.setElementType(ElementType.USER_TASK);
        List<String> users = new ArrayList<>();
        users.add("1");
        users.add("2");
        userTaskItem.setUsers(users);
        List<String> operations = new ArrayList<>();
        operations.add("1");
        operations.add("2");
        operations.add("3");
        userTaskItem.setOperations(operations);
        userTaskItem.setFallbackId("Activity_1rlugwj");
        nodeItemList.add(userTaskItem);

        NodeItem flowItem = new NodeItem();
        flowItem.setElementId("Flow_0de5hef");
        flowItem.setElementType(ElementType.SEQUENCE_FLOW);
        flowItem.setExpression("${QJ202020201>1 and ${QJ202020202}>1 ");
        nodeItemList.add(flowItem);
        saveProcessInfoReq.setNodeItems(nodeItemList);

        System.out.println(JSONObject.toJSONString(saveProcessInfoReq));
        BaseResponse baseResponse = processMgtController.saveProcessInfo(request, saveProcessInfoReq);
        System.out.println(JSONObject.toJSONString(baseResponse));
    }

    @Test
    public void testModifyProcessInfo() throws FileNotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        SaveProcessInfoReq saveProcessInfoReq = new SaveProcessInfoReq();
        File file = new File("C:\\Users\\wuwanli\\Downloads\\test.bpmn");
        BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(new FileInputStream(file));
        saveProcessInfoReq.setFormId("123");
        saveProcessInfoReq.setBpmnXml(Bpmn.convertToString(bpmnModelInstance));
        List<NodeItem> nodeItemList = new ArrayList<>();
        NodeItem userTaskItem = new NodeItem();
        userTaskItem.setElementId("Activity_1vfxio6");
        userTaskItem.setElementType(ElementType.USER_TASK);
        List<String> users = new ArrayList<>();
        users.add("1");
        users.add("2");
        userTaskItem.setUsers(users);
        List<String> operations = new ArrayList<>();
        operations.add("1");
        operations.add("2");
        operations.add("3");
        userTaskItem.setOperations(operations);
        userTaskItem.setFallbackId("Activity_1rlugwj");
        nodeItemList.add(userTaskItem);

        NodeItem flowItem = new NodeItem();
        flowItem.setElementId("Flow_0de5hef");
        flowItem.setElementType(ElementType.SEQUENCE_FLOW);
        flowItem.setExpression("${QJ202020201>1 and ${QJ202020202}>1 ");
        nodeItemList.add(flowItem);
        saveProcessInfoReq.setNodeItems(nodeItemList);

        System.out.println(JSONObject.toJSONString(saveProcessInfoReq));
        BaseResponse baseResponse = processMgtController.modifyProcessInfo(request, saveProcessInfoReq);
        System.out.println(JSONObject.toJSONString(baseResponse));
    }

    @Test
    public void testPublishProcess() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        String processId = "Process_0q58sgg";
        BaseResponse baseResponse = processMgtController.publishProcess(request, processId);
        System.out.println(JSONObject.toJSONString(baseResponse));
    }

    /**
     * 流程列表分页查询
     */
    @Test
    public void list() {
        // QueryProcessReq queryProcessReq = QueryProcessReq.builder().processId("132").processName("请假").build();
        // PageResponseVO pageResponseVO = processMgtController.queryProcessList(1, 10, queryProcessReq);
        // System.out.println(pageResponseVO.toString());
    }

    /**
     * 删除流程
     */
    @Test
    public void delete() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(EngineConstants.USER_ID, "张三");
        BaseResponse pageResponseVO = processMgtController.deleteProcess(request, "1,2,3");
        System.out.println(pageResponseVO.toString());
    }

    /**
     * 取消发布流程
     */
    @Test
    public void cancelPublish() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        BaseResponse pageResponseVO = processMgtController.cancelPublishProcess(request, "Process_0q58sgg");
        System.out.println(pageResponseVO.toString());
    }

    /**
     * 查询用户列表
     */
    @Test
    public void queryUserList() {
        BaseResponse pageResponseVO = processMgtController.queryUserList("putong");
        System.out.println(pageResponseVO.toString());
    }

    /**
     * 查询角色列表
     */
    @Test
    public void queryRoleList() {
        BaseResponse pageResponseVO = processMgtController.queryRoleList("管理员");
        System.out.println(pageResponseVO.toString());
    }
}
