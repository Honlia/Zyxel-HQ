package cn.superfw.application.workflow.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superfw.application.common.AppContants;
import cn.superfw.application.workflow.dto.WorkflowInfo;
import cn.superfw.framework.web.BLogicService;
import cn.superfw.framework.web.BaseController;

@Controller
public class WorkflowController extends BaseController {


    private static final String PAGE_PROCESS_DEF = "SC990104";
    private static final String PAGE_PROCESS_INS = "SC990105";
    private static final String PAGE_TODO_TASK = "SC990106";
    private static final String PAGE_COMPLETED_TASK = "SC990107";

    @Autowired
    private BLogicService<String, Map<String, Object>> deployWorkflowService;

    @Autowired
    private BLogicService<Void, Void> deleteAllDeploymentService;

    @Autowired
    private BLogicService<WorkflowInfo, List<Map<String, Object>>> listTaskService;

    @Autowired
    private BLogicService<WorkflowInfo, Void> completeTaskService;

    @Autowired
    private BLogicService<WorkflowInfo, String> dispatchTaskService;

    @Autowired
    private BLogicService<Void, List<Map<String, Object>>> listProceessDefinitionService;

    @Autowired
    private BLogicService<WorkflowInfo, List<Map<String, Object>>> listProcessInstanceService;

    @Autowired
    private BLogicService<WorkflowInfo, Void> suspendProcessInstanceService;

    @Autowired
    private BLogicService<WorkflowInfo, Void> deleteProcessInstanceService;

    @Autowired
    private BLogicService<WorkflowInfo, byte[]> getProcessPicService;

    @RequestMapping(value = "/workflow/deleteAllDeployment", method = RequestMethod.GET)
    public String deleteAllDeployment() {
        deleteAllDeploymentService.execute(null);
        return "redirect:/workflow/listAllProcessDef";
    }

    @RequestMapping(value = "/workflow/deployAllProcess", method = RequestMethod.GET)
    public String deployAllProcess(ModelMap model) {
        List<Map<String, Object>> lst = new ArrayList<>();

        Map<String, Object> projectReportFlowInfo = deployWorkflowService.execute(AppContants.FLOW_PROJECT_REPORT);
        Map<String, Object> specialStockFlowInfo = deployWorkflowService.execute(AppContants.FLOW_SPECIAL_STOCK);

        lst.add(projectReportFlowInfo);
        lst.add(specialStockFlowInfo);

        model.addAttribute("processInfoList", lst);

        return "redirect:/workflow/listAllProcessDef";
    }

    @RequestMapping(value = "/workflow/listAllProcessDef", method = RequestMethod.GET)
    public String listAllProcessDef(ModelMap model) {

        List<Map<String, Object>> proceessDefinitionList = listProceessDefinitionService.execute(null);
        model.addAttribute("proceessDefinitionList", proceessDefinitionList);

        return PAGE_PROCESS_DEF;
    }

    @RequestMapping(value = "/workflow/listAllProcessIns", method = RequestMethod.GET)
    public String listAllProcessInstance(ModelMap model) {

        WorkflowInfo workflowInfo = new WorkflowInfo();
        workflowInfo.setUsername(AppContants.USERNAME_ADMIN);

        List<Map<String, Object>> processInstanceList = listProcessInstanceService.execute(workflowInfo);

        model.addAttribute("processInstanceList", processInstanceList);

        return PAGE_PROCESS_INS;
    }

    @RequestMapping(value = "/workflow/suspendProcessIns/{instanceId}", method = RequestMethod.GET)
    public String suspendProcessInstance(@PathVariable String instanceId) {

        WorkflowInfo workflowInfo = new WorkflowInfo();
        workflowInfo.setInstanceId(instanceId);
        suspendProcessInstanceService.execute(workflowInfo);

        return "redirect:/workflow/listAllProcessIns";
    }

    @RequestMapping(value = "/workflow/deleteProcessIns/{instanceId}", method = RequestMethod.GET)
    public String deleteProcessInstance(@PathVariable String instanceId) {

        WorkflowInfo workflowInfo = new WorkflowInfo();
        workflowInfo.setInstanceId(instanceId);
        deleteProcessInstanceService.execute(workflowInfo);

        return "redirect:/workflow/listAllProcessIns";
    }

    @RequestMapping(value = "/workflow/listTodoTask", method = RequestMethod.GET)
    public String listTodoTask(ModelMap model) {

        WorkflowInfo workflowInfo = new WorkflowInfo();
        workflowInfo.setTaskStatus(AppContants.TASK_STATUS_TODO);
        workflowInfo.addCandidateUserName(AppContants.USERNAME_ADMIN);

        List<Map<String, Object>> todoTaskList = listTaskService.execute(workflowInfo);
        model.addAttribute("todoTaskList", todoTaskList);

        return PAGE_TODO_TASK;
    }

    @RequestMapping(value = "/workflow/listCompletedTask", method = RequestMethod.GET)
    public String listCompletedTask(ModelMap model) {

        WorkflowInfo workflowInfo = new WorkflowInfo();
        workflowInfo.setTaskStatus(AppContants.TASK_STATUS_COMPLETED);
        workflowInfo.addCandidateUserName(AppContants.USERNAME_ADMIN);

        List<Map<String, Object>> completedTaskList = listTaskService.execute(workflowInfo);
        model.addAttribute("completedTaskList", completedTaskList);

        return PAGE_COMPLETED_TASK;
    }

    @RequestMapping(value = "/workflow/handleTask/{instanceId}/{taskId}/{businessKey}/{judge}", method = RequestMethod.GET)
    public String handleTask(ModelMap model,
            @PathVariable String instanceId,
            @PathVariable String taskId,
            @PathVariable String businessKey,
            @PathVariable String judge) {

        Integer decision = -1;
        WorkflowInfo workflowInfo = null;

        // 同意
        if ("agree".equals(judge)) {
            decision = AppContants.DECISION_AGREE;
        // 权限内同意
        } else if ("authagree".equals(judge)) {
            decision = AppContants.DECISION_AUTH_AGREE;
        // 不同意
        } else if ("disagree".equals(judge)) {
            decision = AppContants.DECISION_DISAGREE;
        }

        // 完成当前任务
        workflowInfo = new WorkflowInfo();
        workflowInfo.setTaskId(taskId);
        workflowInfo.putVariable(AppContants.FLOW_DECISION_KEY, decision);
        workflowInfo.setUsername(AppContants.USERNAME_ADMIN);
        completeTaskService.execute(workflowInfo);

        // 指派下一个任务的参与者
        workflowInfo = new WorkflowInfo();
        workflowInfo.setInstanceId(instanceId);
        dispatchTaskService.execute(workflowInfo);

        return "redirect:/workflow/listTodoTask";
    }

    @RequestMapping(value = "/workflow/getProcessPic/{processDefinitionId}", method = RequestMethod.GET)
    @ResponseBody
    public byte[] getProcessPic(ModelMap model,
            @PathVariable String processDefinitionId,
            @RequestParam(required=false) String executionId) {

        WorkflowInfo workflowInfo = new WorkflowInfo();
        workflowInfo.setProcessDefinitionId(processDefinitionId);
        workflowInfo.setExecutionId(executionId);

        byte[] processPic = getProcessPicService.execute(workflowInfo);

        return processPic;

    }

}
