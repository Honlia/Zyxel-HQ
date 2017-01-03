package cn.superfw.application.model.m99.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superfw.framework.web.BaseController;

@Controller
public class TestController extends BaseController {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    @Autowired
    ManagementService managementService;

    @RequestMapping(value="/deploy", method=RequestMethod.GET)
    @ResponseBody
    public Map<String, String> deployProcess() {
        // 部署流程
        Deployment deployment = repositoryService.createDeployment()
            .addClasspathResource("workflow/special_stock_flow.bpmn")
            .addClasspathResource("workflow/special_stock_flow.png")
            .name("特价备货流程部署")
            .deploy();
        System.out.println(deployment.getId() + "       " + deployment.getName());

        Map<String, String> map = new HashMap<String, String>();
        map.put("deploymentId", deployment.getId());
        map.put("deploymentName", deployment.getName());
        return map;
    }

    @RequestMapping(value="/start/{username}", method=RequestMethod.GET)
    @ResponseBody
    public Map<String, String> startProcess(@PathVariable String username) {
        // 开启流程
        Map<String, Object> vars = new HashMap<>();
        vars.put("username", username);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("special_stock_flow",vars);
        System.out.println(instance.getId() + "      " + instance.getActivityId());
        Map<String, String> map = new HashMap<String, String>();
        map.put("instanceId", instance.getId());
        map.put("activityId", instance.getActivityId());
        return map;
    }

    @RequestMapping(value="/findTasks/{username}", method=RequestMethod.GET)
    @ResponseBody
    public List<Map<String, String>> findTasks(@PathVariable String username) {
        // 查找我的个人任务
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(username).list();
        List<Map<String, String>> lst = new ArrayList<>();
        Map<String, String> map = null;
        for (Task task : tasks) {
            System.out.println("id="+task.getId());
            System.out.println("name="+task.getName());
            System.out.println("assinee="+task.getAssignee());
            System.out.println("createTime="+task.getCreateTime());
            System.out.println("executionId="+task.getExecutionId());

            map = new HashMap<String, String>();
            map.put("taskId", task.getId());
            map.put("taskName", task.getName());
            map.put("assinee", task.getAssignee());
            lst.add(map);
        }
        return lst;
    }

    @RequestMapping(value="/complateTask/{taskId}/{username}/{decision}", method=RequestMethod.GET)
    @ResponseBody
    public Map<String, String> complateTask(@PathVariable String taskId, @PathVariable String username, @PathVariable Integer decision) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("username", username);
        vars.put("decision", decision);
        taskService.complete(taskId, vars);

        Map<String, String> map = new HashMap<String, String>();
        map.put("result","Task:" + taskId + "已完成");
        return map;
    }


    @RequestMapping(value="/deleteDeploy/{id}", method=RequestMethod.GET)
    @ResponseBody
    public String deleteDeploy(@PathVariable String id) {
    	repositoryService.deleteDeployment(id, true);
    	return "Cascade delete success!";
    }

    @RequestMapping(value="/testFlow1", method=RequestMethod.GET)
    @ResponseBody
    public String testFlow1() {
    	String taskId = null;
    	List<Map<String, String>> tasks = null;

    	startProcess("sales02");
    	tasks = findTasks("sales02");
    	taskId = tasks.get(0).get("taskId");
    	complateTask(taskId,"opt02",1);

    	tasks = findTasks("opt02");
    	taskId = tasks.get(0).get("taskId");
    	complateTask(taskId,"sales02",1);

    	tasks = findTasks("sales02");
    	taskId = tasks.get(0).get("taskId");
    	complateTask(taskId,"bizm02",1);

    	tasks = findTasks("bizm02");
    	taskId = tasks.get(0).get("taskId");
    	complateTask(taskId,"mdm02",1);

    	tasks = findTasks("mdm02");
    	taskId = tasks.get(0).get("taskId");
    	complateTask(taskId,"boos02",1);

    	tasks = findTasks("boos02");
    	taskId = tasks.get(0).get("taskId");
    	complateTask(taskId,"mdm02",3);


    	return "OK";
    }


    @RequestMapping(value="/testFlow2", method=RequestMethod.GET)
    @ResponseBody
    public String testFlow2() {

        // 部署流程
        /*Deployment deployment = repositoryService.createDeployment()
            .addClasspathResource("workflow/project_report_flow.bpmn")
            .addClasspathResource("workflow/project_report_flow.png")
            .name("项目报备流程部署")
            .deploy();*/

        String taskId = null;
        String instanceId = null;

        // 开启流程并用编程方式指定
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("project_report_flow");
        instanceId = instance.getId();
        Task task = taskService.createTaskQuery()
                .processInstanceId(instanceId)
                .singleResult();
        taskId = task.getId();
        taskService.addCandidateUser(taskId, "sales04");
        taskService.addCandidateUser(taskId, "sales05");

        String userId = "sales04";
        List<Task> list = taskService.createTaskQuery()//
                        .taskAssignee(userId)//指定个人任务查询
                        .list();
        for(Task task1:list ){
            System.out.println("id="+task1.getId());
            System.out.println("name="+task1.getName());
            System.out.println("assinee="+task1.getAssignee());
            System.out.println("assinee="+task1.getCreateTime());
            System.out.println("executionId="+task1.getExecutionId());
            System.out.println("sales04##################################");

        }

        userId = "sales05";
        list = taskService.createTaskQuery()//
                        .taskCandidateUser(userId)//指定组任务查询
                        .list();
        for(Task task1:list ){
            System.out.println("id="+task1.getId());
            System.out.println("name="+task1.getName());
            System.out.println("assinee="+task1.getAssignee());
            System.out.println("assinee="+task1.getCreateTime());
            System.out.println("executionId="+task1.getExecutionId());
            System.out.println("sales05##################################");

        }



        /*taskService.complete(taskId);

        task = taskService.createTaskQuery()
                .processInstanceId(instanceId)
                .singleResult();
        taskId = task.getId();
        taskService.setAssignee(taskId, "opt03");
        taskService.complete(taskId);*/


        return "OK";
    }


    @RequestMapping(value="/deploy1", method=RequestMethod.GET)
    @ResponseBody
    public String deploy1() {
    	// 部署流程
    	repositoryService.createDeployment()
                .addClasspathResource("workflow/special_stock_flow.bpmn")
                .addClasspathResource("workflow/special_stock_flow.png")
                .name("特价备货流程部署")
                .deploy();
        return "OK";
    }
    
    /**
     * 1.添加组任务的用户
     *   taskService.addCandidateUser(taskId, userId);
     * 2.删除组任务的用户
     *   taskService.deleteCandidateUser(taskId, userId);
     * 3.认领任务
     *   taskService.claim(taskId, userId)
     * 4.个人任务和组任务的区别
     *   如果是个人任务TYPE的类型表示participant（参与者）,
     *   如果是组任务TYPE的类型表示candidate（候选者）和participant（参与者）
     */
    @RequestMapping(value="/standFlow", method=RequestMethod.GET)
    @ResponseBody
    public String doStandFlow() {

    	ProcessDefinition processDefinition = null;
        String taskId = null;
        String instanceId = null;
        List<Task> taskList = null;
        Task task = null;
        
        //runtimeService.deleteProcessInstance("35101", "过期强行删除");
        /*List<ProcessInstance> instances =  runtimeService.createProcessInstanceQuery().processDefinitionKey("special_stock_flow").list();
        for (ProcessInstance instance : instances) {
        	runtimeService.deleteProcessInstance(instance.getId(), "过期强行删除");
        }*/
    
    	
    	// 查询流程定义
    	processDefinition = repositoryService.createProcessDefinitionQuery()
    			.processDefinitionKey("special_stock_flow")
    			.latestVersion()
    			.singleResult();
    	
    	if (processDefinition == null) {
    		// 部署流程
            repositoryService.createDeployment()
	            .addClasspathResource("workflow/special_stock_flow.bpmn")
	            .addClasspathResource("workflow/special_stock_flow.png")
	            .name("特价备货流程部署")
                .deploy();
    	}
    	
        // 开启流程并用编程方式指定
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("special_stock_flow");
        instanceId = instance.getId();

        // 张三填写报备申请,并提交申请
        // 没用并行网关的情况下,instanceId和任务taskId可以唯一确定一个正在执行的任务
        task = taskService.createTaskQuery()
                .processInstanceId(instanceId)
                .taskDefinitionKey("SpecialStockApply")
                .singleResult();
        taskId = task.getId();
        taskService.claim(taskId, "张三");
        taskService.complete(taskId);

        // 李四,王五是业务部主管,都有审批张三申请的权限
        // 查询待审批的所有流程实例
        taskList = taskService.createTaskQuery()
                    .taskDefinitionKey("FastestDeliveryDateConfirm")
                    .list();
        if (taskList != null && !taskList.isEmpty()) {
            for (Task item : taskList) {
                taskService.addCandidateUser(item.getId(), "李四");
                taskService.addCandidateUser(item.getId(), "王五");
            }
        }


        // 王五最近比较闲,所有任务都被他审批了
        taskList = taskService.createTaskQuery()
                //.processInstanceId(instanceId)
                .taskDefinitionKey("FastestDeliveryDateConfirm")
                .taskCandidateUser("王五")
                .list();
        if (taskList != null && !taskList.isEmpty()) {
            for (Task item : taskList) {
                taskId = item.getId();
                // 认领任务
                taskService.claim(taskId, "王五");
                // 办理任务
                taskService.complete(taskId);
            }
        }

        return "OK";
    }


    @RequestMapping(value="/findHistoryTasks/{processInstanceId}", method=RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> findHistoryTasksById(@PathVariable String processInstanceId) {

    	List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
    			.processInstanceId(processInstanceId)
    			.taskDefinitionKey("usertask1")
    			.orderByTaskDefinitionKey().asc()
    			.list();

    	List<Map<String, Object>> lst = new ArrayList<>();
        Map<String, Object> map = null;
    	for (HistoricTaskInstance historicTask : historicTasks) {
    		map = new HashMap<>();
    		map.put("taskId", historicTask.getId());
    		map.put("taskName", historicTask.getName());
    		map.put("taskDefinitionKey", historicTask.getTaskDefinitionKey());
    		map.put("assignee", historicTask.getAssignee());
    		map.put("startTime", historicTask.getStartTime());
    		map.put("endTime", historicTask.getEndTime());
    		map.put("duration", historicTask.getDurationInMillis());
    		lst.add(map);
    	}

    	return lst;
    }

}
