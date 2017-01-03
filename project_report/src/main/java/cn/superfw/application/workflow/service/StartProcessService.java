package cn.superfw.application.workflow.service;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.workflow.dto.WorkflowInfo;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class StartProcessService extends BaseBLogicService<WorkflowInfo, Map<String, Object>> {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Override
    protected Map<String, Object> doService(WorkflowInfo workflowInfo) {

        Map<String, Object> map = null;
        ProcessInstance instance = null;

        String username = workflowInfo.getUsername();

    	// 查询流程实例是否存在
        instance = runtimeService.createProcessInstanceQuery()
	        .processDefinitionKey(workflowInfo.getProcessDefinitionKey())
	        .processInstanceBusinessKey(workflowInfo.getBusinessKey()).singleResult();

        // 如果不存在,则启动流程
        if (instance == null) {
        	try {
	        	identityService.setAuthenticatedUserId(username);
	            instance = runtimeService.startProcessInstanceByKey(
	                    workflowInfo.getProcessDefinitionKey(),
	                    workflowInfo.getBusinessKey(),
	                    workflowInfo.getVariables());
        	} finally {
        		identityService.setAuthenticatedUserId(null);
        	}
        }

        Task task = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).singleResult();

        map = new HashMap<String, Object>();
        map.put("instanceId", instance.getId());
        map.put("taskId", task.getId());
        map.put("instanceName", instance.getName());
        map.put("activityId", instance.getActivityId());
        map.put("processDefinitionKey", instance.getProcessDefinitionKey());
        map.put("processDefinitionName", instance.getProcessDefinitionName());
        map.put("processDefinitionVersion", instance.getProcessDefinitionVersion());

        return map;
    }

}
