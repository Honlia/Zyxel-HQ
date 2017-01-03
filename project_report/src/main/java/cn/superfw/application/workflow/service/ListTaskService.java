package cn.superfw.application.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.common.AppContants;
import cn.superfw.application.workflow.dto.WorkflowInfo;
import cn.superfw.framework.utils.StringUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class ListTaskService extends BaseBLogicService<WorkflowInfo, List<Map<String, Object>>> {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Override
    protected List<Map<String, Object>> doService(WorkflowInfo workflowInfo) {

        List<Map<String, Object>> lst = null;

        String taskStatus = workflowInfo.getTaskStatus();

        switch (taskStatus) {
        case AppContants.TASK_STATUS_TODO:
            lst = getTodoTaskList(workflowInfo);
            break;
        case AppContants.TASK_STATUS_COMPLETED:
            lst = getCompletedTaskList(workflowInfo);
            break;
        default:
            break;
        }

        return lst;
    }

    private List<Map<String, Object>> getCompletedTaskList(WorkflowInfo workflowInfo) {

        List<Map<String, Object>> lst = new ArrayList<>();

        Map<String, Object> map;
        List<HistoricTaskInstance> historicTaskInstanceList = null;

		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();
		if (StringUtil.isNotEmpty(workflowInfo.getUsername())) {
			query.taskAssignee(workflowInfo.getUsername());
		}

		if (StringUtil.isNotEmpty(workflowInfo.getInstanceId())) {
			query.processInstanceId(workflowInfo.getInstanceId());
		}
		query.finished();
		query.orderByTaskCreateTime().desc();
		historicTaskInstanceList = query.list();


        for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {

            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(historicTaskInstance.getProcessDefinitionId())
                    .singleResult();

            HistoricProcessInstance historicProcessInstance =
                    historyService.createHistoricProcessInstanceQuery()
	                                  .processInstanceId(historicTaskInstance.getProcessInstanceId())
	                                  .singleResult();

            HistoricVariableInstance taskCommentVariableInstance =
            		historyService.createHistoricVariableInstanceQuery()
				            		.processInstanceId(historicTaskInstance.getProcessInstanceId())
				            		.taskId(historicTaskInstance.getId())
				            		.variableName(AppContants.TASK_COMMENT)
				            		.singleResult();

            HistoricVariableInstance decisionVariableInstance =
                    historyService.createHistoricVariableInstanceQuery()
                                    .processInstanceId(historicTaskInstance.getProcessInstanceId())
                                    .taskId(historicTaskInstance.getId())
                                    .variableName(AppContants.FLOW_DECISION_KEY)
                                    .singleResult();

            String taskCommont = "没有批注";
            Integer decision = -1;

            if (taskCommentVariableInstance != null) {
            	taskCommont = String.valueOf(taskCommentVariableInstance.getValue());
            }

            if (decisionVariableInstance != null) {
                decision = (Integer)decisionVariableInstance.getValue();
            }


            map = new HashMap<String, Object>();
            map.put("executionId", historicTaskInstance.getExecutionId());
            map.put("instanceId", historicTaskInstance.getProcessInstanceId());
            map.put("processDefinitionName", processDefinition.getName());
            map.put("processDefinitionId", historicTaskInstance.getProcessDefinitionId());
            map.put("processStartTime", historicProcessInstance.getStartTime());
            map.put("deleteReason", historicProcessInstance.getDeleteReason());
            map.put("startUserId", historicProcessInstance.getStartUserId());
            map.put("taskId", historicTaskInstance.getId());
            map.put("businessKey", historicProcessInstance.getBusinessKey());
            map.put("taskName", historicTaskInstance.getName());
            map.put("taskAssignee", historicTaskInstance.getAssignee());
            map.put("taskStartTime", historicTaskInstance.getStartTime());
            map.put("taskEndTime", historicTaskInstance.getEndTime());
            map.put("processVariables", historicTaskInstance.getProcessVariables());
            map.put(AppContants.TASK_COMMENT, taskCommont);
            map.put(AppContants.FLOW_DECISION_KEY, decision);
            lst.add(map);
        }

        return lst;
    }

    private List<Map<String, Object>> getTodoTaskList(WorkflowInfo workflowInfo) {

        List<Map<String, Object>> lst = new ArrayList<>();

        Map<String, Object> map = null;
        // 查询待审批的所有流程实例
        TaskQuery taskQuery = taskService.createTaskQuery();
        if (StringUtil.isNotEmpty(workflowInfo.getUsername())) {
            taskQuery.taskCandidateOrAssigned(workflowInfo.getUsername());
        }
        if (StringUtil.isNotEmpty(workflowInfo.getBusinessKey())) {
            taskQuery.processInstanceBusinessKey(workflowInfo.getBusinessKey());
        }

        List<Task> taskList = taskQuery.orderByTaskCreateTime().desc().list();

        for (Task task : taskList) {

            ProcessInstance processInstance =
                    runtimeService.createProcessInstanceQuery()
                                  .processInstanceId(task.getProcessInstanceId())
                                  .singleResult();

            HistoricProcessInstance historicProcessInstance =
                    historyService.createHistoricProcessInstanceQuery()
                                  .processInstanceId(task.getProcessInstanceId())
                                  .singleResult();

            map = new HashMap<String, Object>();
            map.put("executionId", task.getExecutionId());
            map.put("instanceId", task.getProcessInstanceId());
            map.put("processDefinitionName", processInstance.getProcessDefinitionName());
            map.put("processDefinitionId", task.getProcessDefinitionId());
            map.put("processStartTime", historicProcessInstance.getStartTime());
            map.put("startUserId", historicProcessInstance.getStartUserId());
            map.put("taskId", task.getId());
            map.put("businessKey", processInstance.getBusinessKey());
            map.put("taskName", task.getName());
            map.put("taskStartTime", task.getCreateTime());
            map.put("processVariables", task.getProcessVariables());
            // 增加活动任务在流程图上的坐标信息
            map.putAll(getActivitiyCoordinateInfo(task.getProcessDefinitionId(), task.getExecutionId()));

            lst.add(map);
        }

        return lst;
    }

    private Map<String, Integer> getActivitiyCoordinateInfo(String processDefinitionId, String executionId) {

        ActivityImpl actImpl = null;

        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processDefinitionId);

        ExecutionEntity execution = (ExecutionEntity) runtimeService
                .createExecutionQuery()
                .executionId(executionId)
                .singleResult();// 执行实例

        String activitiId = execution.getActivityId();// 当前实例的执行到哪个节点

        List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点

        for (ActivityImpl activityImpl : activitiList) {
            String id = activityImpl.getId();
            if (id.equals(activitiId)) {// 获得执行到那个节点
                actImpl = activityImpl;
                break;
            }
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("x", actImpl.getX());
        map.put("y", actImpl.getY());
        map.put("width", actImpl.getWidth());
        map.put("height", actImpl.getHeight());

        return map;
    }

}
