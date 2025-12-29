package com.rt.engine;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSONObject;

import io.camunda.zeebe.model.bpmn.Bpmn;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import io.camunda.zeebe.model.bpmn.builder.EndEventBuilder;
import io.camunda.zeebe.model.bpmn.builder.ExclusiveGatewayBuilder;
import io.camunda.zeebe.model.bpmn.builder.ProcessBuilder;
import io.camunda.zeebe.model.bpmn.builder.ServiceTaskBuilder;
import io.camunda.zeebe.model.bpmn.builder.StartEventBuilder;
import io.camunda.zeebe.model.bpmn.builder.UserTaskBuilder;
import io.camunda.zeebe.model.bpmn.instance.EndEvent;
import io.camunda.zeebe.model.bpmn.instance.Process;
import io.camunda.zeebe.model.bpmn.instance.StartEvent;
import io.camunda.zeebe.model.bpmn.instance.bpmndi.BpmnDiagram;
import io.camunda.zeebe.model.bpmn.instance.bpmndi.BpmnPlane;
import io.camunda.zeebe.model.bpmn.instance.bpmndi.BpmnShape;
import io.camunda.zeebe.model.bpmn.instance.dc.Bounds;

/**
 * @author wuwanli
 * @date 2021/9/7
 */
public class BpmnModelTest {

    // @Resource
    // private ProcessInfoService processFileService;

    // @Test
    // public void parseBpmnModel() {
    // String processId = "leaveApply";
    // ProcessFile processFile = processFileService.queryProcessFileById(processId);
    // BpmnModelInstance bpmnModelInstance = Bpmn
    // .readModelFromStream(new ByteArrayInputStream(processFile.getResource().getBytes(StandardCharsets.UTF_8)));
    //
    // // 获取 Process
    // Collection<Process> processes = bpmnModelInstance.getModelElementsByType(Process.class);
    // Process process = processes.stream().findFirst().orElseThrow();
    // System.out.println(process.getId());
    // System.out.println(process.getName());
    // process.getFlowElements().iterator().forEachRemaining(o -> {
    // System.out.println(o.getId());
    // System.out.println(o.getName());
    // System.out.println(o.getElementType().getTypeName());
    // });
    // }

    /**
     * 单个userTask没有条件网关
     */
    @Test
    public void buildBpmnModel() {
        ProcessBuilder processBuilder = Bpmn.createProcess("test");
        processBuilder.getElement().setName("这是测试demo");
        StartEventBuilder startEventBuilder = processBuilder.startEvent("start");
        startEventBuilder.getElement().setName("开始");
        UserTaskBuilder taskBuilder = startEventBuilder.userTask("task1");
        taskBuilder.name("测试任务");
        EndEventBuilder eventBuilder = taskBuilder.endEvent("end");
        eventBuilder.name("结束");
        BpmnModelInstance bpmnModelInstance = processBuilder.done();
        System.out.println(Bpmn.convertToString(bpmnModelInstance));
    }

    /**
     * 单个serviceTask无条件网关
     */
    @Test
    public void buildBpmnServiceTaskModel() {
        ProcessBuilder processBuilder = Bpmn.createProcess("test");
        processBuilder.getElement().setName("这是测试demo");
        StartEventBuilder startEventBuilder = processBuilder.startEvent("start");
        startEventBuilder.getElement().setName("开始");
        ServiceTaskBuilder serviceTaskBuilder = startEventBuilder.serviceTask("task1");
        serviceTaskBuilder.name("测试serviceTask任务").zeebeJobType("aa");
        EndEventBuilder eventBuilder = serviceTaskBuilder.endEvent("end");
        eventBuilder.name("结束");
        BpmnModelInstance bpmnModelInstance = processBuilder.done();
        System.out.println(Bpmn.convertToString(bpmnModelInstance));
    }

    /**
     * 带排他网关ExclusiveGateway带task任务
     */
    @Test
    public void buildBpmnGatewayTaskModel() {
        ProcessBuilder processBuilder = Bpmn.createProcess("test");
        processBuilder.getElement().setName("这是测试demo");
        StartEventBuilder startEventBuilder = processBuilder.startEvent("start");
        startEventBuilder.getElement().setName("开始");
        ServiceTaskBuilder serviceTaskBuilder = startEventBuilder.serviceTask("task1");
        serviceTaskBuilder.name("测试GatewayTask任务").zeebeJobType("aa");
        ExclusiveGatewayBuilder exclusiveGateway = serviceTaskBuilder.exclusiveGateway("gateway");
        UserTaskBuilder userTaskBuilder2 = exclusiveGateway.condition("=a>1").userTask("task2");
        UserTaskBuilder userTaskBuilder3 = exclusiveGateway.condition("=a<=1").userTask("task3");
        EndEventBuilder end = userTaskBuilder2.endEvent("end");
        end.name("结束");
        userTaskBuilder3.connectTo("end");
        BpmnModelInstance bpmnModelInstance = processBuilder.done();
        System.out.println(Bpmn.convertToString(bpmnModelInstance));
    }

    /**
     * 带排他网关ExclusiveGateway直接结束
     */
    @Test
    public void buildBpmnGatewayNoTaskModel() {
        ProcessBuilder processBuilder = Bpmn.createProcess("test");
        processBuilder.getElement().setName("这是测试demo");
        StartEventBuilder startEventBuilder = processBuilder.startEvent("start");
        startEventBuilder.getElement().setName("开始");
        ServiceTaskBuilder serviceTaskBuilder = startEventBuilder.serviceTask("task1");
        serviceTaskBuilder.name("测试serviceTask任务").zeebeJobType("aa");
        ExclusiveGatewayBuilder exclusiveGateway = serviceTaskBuilder.exclusiveGateway("gateway");
        UserTaskBuilder userTaskBuilder2 = exclusiveGateway.condition("=a>1").userTask("task2");
        EndEventBuilder end = userTaskBuilder2.endEvent("end");
        end.name("结束");
        exclusiveGateway.connectTo("end");
        BpmnModelInstance bpmnModelInstance = processBuilder.done();

        System.out.println(Bpmn.convertToString(bpmnModelInstance));
    }

    /**
     * 移动坐标实现(暂时未实现)
     */
    @Test
    public void buildBpmnPointModel() {
        ProcessBuilder processBuilder = Bpmn.createProcess("test");
        processBuilder.getElement().setName("这是测试demo");
        StartEventBuilder startEventBuilder = processBuilder.startEvent("start");
        startEventBuilder.getElement().setName("开始");
        ServiceTaskBuilder serviceTaskBuilder = startEventBuilder.serviceTask("task1");
        serviceTaskBuilder.name("测试point任务").zeebeJobType("aa");
        ExclusiveGatewayBuilder exclusiveGateway = serviceTaskBuilder.exclusiveGateway("gateway1");
        UserTaskBuilder userTaskBuilder2 = exclusiveGateway.condition("=a>1").userTask("task2");
        EndEventBuilder end = userTaskBuilder2.endEvent("end");
        end.name("结束");
        exclusiveGateway.connectTo("end");
        BpmnModelInstance bpmnModelInstance = processBuilder.done();
        StartEvent startEvent = bpmnModelInstance.getModelElementById("start");
        EndEvent endEvent = bpmnModelInstance.getModelElementById("end");
        Process process = bpmnModelInstance.getModelElementById("test");

        // create bpmn diagram
        final BpmnDiagram bpmnDiagram = bpmnModelInstance.newInstance(BpmnDiagram.class);
        bpmnDiagram.setId("diagram");
        bpmnDiagram.setName("diagram");
        bpmnDiagram.setDocumentation("bpmn diagram element");
        bpmnDiagram.setResolution(120.0);
        // bpmnModelInstance.getDefinitions().replaceChildElement(bpmnModelInstance.getDefinitions().getBpmDiagrams().iterator().next(),
        // bpmnDiagram);
        bpmnModelInstance.getDefinitions().addChildElement(bpmnDiagram);

        // create plane for process
        final BpmnPlane processPlane = bpmnModelInstance.newInstance(BpmnPlane.class);
        processPlane.setId("plane");
        processPlane.setBpmnElement(process);
        bpmnDiagram.setBpmnPlane(processPlane);

        // create shape for start event
        final BpmnShape startEventShape = bpmnModelInstance.newInstance(BpmnShape.class);
        startEventShape.setId("startShape");
        startEventShape.setBpmnElement(startEvent);
        processPlane.getDiagramElements().add(startEventShape);

        // create bounds for start event shape
        final Bounds startEventBounds = bpmnModelInstance.newInstance(Bounds.class);
        startEventBounds.setHeight(36.0);
        startEventBounds.setWidth(36.0);
        startEventBounds.setX(632.0);
        startEventBounds.setY(312.0);
        startEventShape.setBounds(startEventBounds);

        // create shape for end event
        final BpmnShape endEventShape = bpmnModelInstance.newInstance(BpmnShape.class);
        endEventShape.setId("endShape");
        endEventShape.setBpmnElement(endEvent);
        processPlane.getDiagramElements().add(endEventShape);

        // create bounds for end event shape
        final Bounds endEventBounds = bpmnModelInstance.newInstance(Bounds.class);
        endEventBounds.setHeight(36.0);
        endEventBounds.setWidth(36.0);
        endEventBounds.setX(718.0);
        endEventBounds.setY(312.0);
        endEventShape.setBounds(endEventBounds);

        System.out.println(Bpmn.convertToString(bpmnModelInstance));
    }

    public static String json = "{\n" + "  \"name\": \"111\",\n" + "  \"workarea\": 262,\n" + "  \"describe\": \"1\",\n"
        + "  \"origin\": [\n" + "    618,\n" + "    0\n" + "  ],\n" + "  \"nodeList\": [\n" + "    {\n"
        + "      \"id\": \"nodeB2J6zFi0fuxcCH59\",\n" + "      \"width\": 120,\n" + "      \"height\": 48,\n"
        + "      \"coordinate\": [\n" + "        415,\n" + "        194\n" + "      ],\n" + "      \"meta\": {\n"
        + "        \"label\": \"结束\",\n" + "        \"name\": \"结束\",\n" + "        \"type\": 2,\n"
        + "        \"id\": \"666994a779101\"\n" + "      }\n" + "    },\n" + "    {\n"
        + "      \"id\": \"nodetjcyPMnUYt2pkjRH\",\n" + "      \"width\": 120,\n" + "      \"height\": 48,\n"
        + "      \"coordinate\": [\n" + "        -397,\n" + "        194\n" + "      ],\n" + "      \"meta\": {\n"
        + "        \"label\": \"开始\",\n" + "        \"name\": \"开始\",\n" + "        \"type\": 1,\n"
        + "        \"id\": \"322691a429059\"\n" + "      }\n" + "    },\n" + "    {\n"
        + "      \"id\": \"node3CztjsHG9ZWQjr52\",\n" + "      \"width\": 120,\n" + "      \"height\": 48,\n"
        + "      \"coordinate\": [\n" + "        -11,\n" + "        194\n" + "      ],\n" + "      \"meta\": {\n"
        + "        \"label\": \"普通节点\",\n" + "        \"name\": \"普通节点\",\n" + "        \"type\": 3,\n"
        + "        \"id\": \"754227a566608\",\n" + "        \"plugins\": []\n" + "      }\n" + "    }\n" + "  ],\n"
        + "  \"linkList\": [\n" + "    {\n" + "      \"id\": \"linkC8k9iXKaGFWqnMuf\",\n"
        + "      \"startId\": \"nodetjcyPMnUYt2pkjRH\",\n" + "      \"endId\": \"node3CztjsHG9ZWQjr52\",\n"
        + "      \"startAt\": [\n" + "        120,\n" + "        24\n" + "      ],\n" + "      \"endAt\": [\n"
        + "        0,\n" + "        24\n" + "      ]\n" + "    },\n" + "    {\n"
        + "      \"id\": \"linkHurJEcxVEdagPuTy\",\n" + "      \"startId\": \"node3CztjsHG9ZWQjr52\",\n"
        + "      \"endId\": \"nodeB2J6zFi0fuxcCH59\",\n" + "      \"startAt\": [\n" + "        120,\n" + "        24\n"
        + "      ],\n" + "      \"endAt\": [\n" + "        0,\n" + "        24\n" + "      ]\n" + "    }\n" + "  ]\n"
        + "}";

    /**
     * 不带网关
     */
    @Test
    public void parseJson() {
        JSONObject jsonObject = JSONObject.parseObject(json);
        List<Node> nodeList = JSONObject.parseArray(jsonObject.get("nodeList").toString(), Node.class);
        List<Link> linkList = JSONObject.parseArray(jsonObject.get("linkList").toString(), Link.class);
        // AtomicReference<String> startId= new AtomicReference<>();
        // AtomicReference<String> endId = new AtomicReference<>();
        // AtomicReference<String> centerId = new AtomicReference<>();
        // linkList.forEach(x -> {
        // if(linkList.stream().noneMatch(y -> StringUtils.equals(y.getEndId(), x.getStartId()))){
        // startId.set(x.getStartId());
        // centerId.set(x.getEndId());
        // }
        // if(linkList.stream().noneMatch(y -> StringUtils.equals(y.getStartId(), x.getEndId()))){
        // endId.set(x.getEndId());
        // }
        // });
        Node startNode =
            nodeList.stream().filter(x -> StringUtils.equals(x.getMeta().getType(), "1")).findFirst().orElse(null);
        Node centerNode =
            nodeList.stream().filter(x -> StringUtils.equals(x.getMeta().getType(), "3")).findFirst().orElse(null);
        Node endNode =
            nodeList.stream().filter(x -> StringUtils.equals(x.getMeta().getType(), "2")).findFirst().orElse(null);

        // 构建mode
        // jsonObject.get("name").toString(),processId不能数字开头
        ProcessBuilder processBuilder = Bpmn.createProcess(jsonObject.get("name").toString());
        processBuilder.getElement().setName(jsonObject.get("describe").toString());
        StartEventBuilder startEventBuilder = processBuilder.startEvent(startNode.getId());
        startEventBuilder.getElement().setName(startNode.getMeta().getName());
        UserTaskBuilder taskBuilder = startEventBuilder.userTask(centerNode.getId());
        taskBuilder.name(centerNode.getMeta().getName());
        EndEventBuilder eventBuilder = taskBuilder.endEvent(endNode.getId());
        eventBuilder.name(endNode.getMeta().getName());
        BpmnModelInstance bpmnModelInstance = processBuilder.done();
        System.out.println(Bpmn.convertToString(bpmnModelInstance));
    }

    public static String jsonGateWay = "{\n" + "  \"workflows_id\": 170,\n" + "  \"sector\": 0,\n"
        + "  \"name\": \"re\",\n" + "  \"workarea\": 262,\n" + "  \"describe\": \"2222\",\n" + "  \"origin\": [\n"
        + "    618,\n" + "    0\n" + "  ],\n" + "  \"nodeList\": [\n" + "    {\n"
        + "      \"id\": \"nodeB2J6zFi0fuxcCH59\",\n" + "      \"width\": 120,\n" + "      \"height\": 48,\n"
        + "      \"coordinate\": [\n" + "        415,\n" + "        194\n" + "      ],\n" + "      \"meta\": {\n"
        + "        \"meta_id\": 801,\n" + "        \"node_id\": \"nodeB2J6zFi0fuxcCH59\",\n"
        + "        \"workflows_id\": 170,\n" + "        \"name\": \"结束\",\n" + "        \"type\": 2,\n"
        + "        \"id\": \"35560a338113\",\n" + "        \"node_code\": \"WF0000017001\"\n" + "      },\n"
        + "      \"node_code\": \"WF0000017001\"\n" + "    },\n" + "    {\n"
        + "      \"id\": \"nodetjcyPMnUYt2pkjRH\",\n" + "      \"width\": 120,\n" + "      \"height\": 48,\n"
        + "      \"coordinate\": [\n" + "        -397,\n" + "        194\n" + "      ],\n" + "      \"meta\": {\n"
        + "        \"meta_id\": 802,\n" + "        \"node_id\": \"nodetjcyPMnUYt2pkjRH\",\n"
        + "        \"workflows_id\": 170,\n" + "        \"name\": \"开始\",\n" + "        \"type\": 1,\n"
        + "        \"id\": \"923438a110535\",\n" + "        \"node_code\": \"WF0000017002\"\n" + "      },\n"
        + "      \"node_code\": \"WF0000017002\"\n" + "    },\n" + "    {\n"
        + "      \"id\": \"nodemhg1ANkO7GVCjVbD\",\n" + "      \"width\": 120,\n" + "      \"height\": 48,\n"
        + "      \"coordinate\": [\n" + "        157,\n" + "        204\n" + "      ],\n" + "      \"meta\": {\n"
        + "        \"label\": \"普通节点\",\n" + "        \"name\": \"普通节点\",\n" + "        \"type\": 3,\n"
        + "        \"id\": \"210058a548019\",\n" + "        \"plugins\": []\n" + "      }\n" + "    },\n" + "    {\n"
        + "      \"id\": \"nodedlaQmPtSLkXt0Tt3\",\n" + "      \"width\": 120,\n" + "      \"height\": 48,\n"
        + "      \"coordinate\": [\n" + "        -144,\n" + "        205\n" + "      ],\n" + "      \"meta\": {\n"
        + "        \"label\": \"条件节点\",\n" + "        \"name\": \"条件节点\",\n" + "        \"type\": 4,\n"
        + "        \"id\": \"908907a334070\",\n" + "        \"plugins\": [],\n"
        + "        \"content\": \"\\u003da\\u003e3\"\n" + "      }\n" + "    }\n" + "  ],\n" + "  \"linkList\": [\n"
        + "    {\n" + "      \"id\": \"linkj4nge5r7E6cCeCH7\",\n" + "      \"startId\": \"nodetjcyPMnUYt2pkjRH\",\n"
        + "      \"endId\": \"nodedlaQmPtSLkXt0Tt3\",\n" + "      \"startAt\": [\n" + "        120,\n" + "        24\n"
        + "      ],\n" + "      \"endAt\": [\n" + "        0,\n" + "        24\n" + "      ]\n" + "    },\n" + "    {\n"
        + "      \"id\": \"linkpvs30ODQ1BEde9Cj\",\n" + "      \"startId\": \"nodedlaQmPtSLkXt0Tt3\",\n"
        + "      \"endId\": \"nodemhg1ANkO7GVCjVbD\",\n" + "      \"startAt\": [\n" + "        120,\n" + "        24\n"
        + "      ],\n" + "      \"endAt\": [\n" + "        0,\n" + "        24\n" + "      ]\n" + "    },\n" + "    {\n"
        + "      \"id\": \"link83gCTi17uJALlptn\",\n" + "      \"startId\": \"nodedlaQmPtSLkXt0Tt3\",\n"
        + "      \"endId\": \"nodeB2J6zFi0fuxcCH59\",\n" + "      \"startAt\": [\n" + "        60,\n" + "        0\n"
        + "      ],\n" + "      \"endAt\": [\n" + "        60,\n" + "        0\n" + "      ]\n" + "    },\n" + "    {\n"
        + "      \"id\": \"link1ydVxG9PpXh6kT9p\",\n" + "      \"startId\": \"nodemhg1ANkO7GVCjVbD\",\n"
        + "      \"endId\": \"nodeB2J6zFi0fuxcCH59\",\n" + "      \"startAt\": [\n" + "        120,\n" + "        24\n"
        + "      ],\n" + "      \"endAt\": [\n" + "        0,\n" + "        24\n" + "      ]\n" + "    }\n" + "  ]\n"
        + "}";

    /**
     * 带网关
     */
    @Test
    public void parseGatewayJson() {
        JSONObject jsonObject = JSONObject.parseObject(jsonGateWay);
        List<Node> nodeList = JSONObject.parseArray(jsonObject.get("nodeList").toString(), Node.class);
        List<Link> linkList = JSONObject.parseArray(jsonObject.get("linkList").toString(), Link.class);
        // AtomicReference<String> startId= new AtomicReference<>();
        // AtomicReference<String> endId = new AtomicReference<>();
        // AtomicReference<String> centerId = new AtomicReference<>();
        // linkList.forEach(x -> {
        // if(linkList.stream().noneMatch(y -> StringUtils.equals(y.getEndId(), x.getStartId()))){
        // startId.set(x.getStartId());
        // centerId.set(x.getEndId());
        // }
        // if(linkList.stream().noneMatch(y -> StringUtils.equals(y.getStartId(), x.getEndId()))){
        // endId.set(x.getEndId());
        // }
        // });
        Node startNode =
            nodeList.stream().filter(x -> StringUtils.equals(x.getMeta().getType(), "1")).findFirst().orElse(null);
        Node centerNode =
            nodeList.stream().filter(x -> StringUtils.equals(x.getMeta().getType(), "3")).findFirst().orElse(null);
        Node endNode =
            nodeList.stream().filter(x -> StringUtils.equals(x.getMeta().getType(), "2")).findFirst().orElse(null);
        Node gatewayNode =
            nodeList.stream().filter(x -> StringUtils.equals(x.getMeta().getType(), "4")).findFirst().orElse(null);

        // 构建mode
        // jsonObject.get("name").toString(),processId不能数字开头
        ProcessBuilder processBuilder = Bpmn.createProcess(jsonObject.get("name").toString());
        processBuilder.getElement().setName(jsonObject.get("describe").toString());
        StartEventBuilder startEventBuilder = processBuilder.startEvent(startNode.getId());
        startEventBuilder.getElement().setName(startNode.getMeta().getName());
        ExclusiveGatewayBuilder exclusiveGateway =
            startEventBuilder.exclusiveGateway(gatewayNode.getId()).name(gatewayNode.getMeta().getName());
        UserTaskBuilder userTaskBuilder2 = exclusiveGateway.condition(gatewayNode.getMeta().getContent())
            .userTask(centerNode.getId()).name(centerNode.getMeta().getName());
        EndEventBuilder end = userTaskBuilder2.endEvent(endNode.getId());
        end.name(endNode.getMeta().getName());
        exclusiveGateway.connectTo(endNode.getId());
        BpmnModelInstance bpmnModelInstance = processBuilder.done();
        System.out.println(Bpmn.convertToString(bpmnModelInstance));
    }
}
