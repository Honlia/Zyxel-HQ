package cn.superfw.application.workflow.service;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.workflow.dto.WorkflowInfo;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class CompleteTaskService extends BaseBLogicService<WorkflowInfo, Void> {

    @Autowired
    private TaskService taskService;

    @Override
    protected Void doService(WorkflowInfo workflowInfo) {

        String taskId = workflowInfo.getTaskId();

        // 如果任务不存在
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
        	return null;
        }

        // 认领任务
        taskService.claim(taskId, workflowInfo.getUsername());

        // 设置任务级的流程变量
        taskService.setVariablesLocal(taskId, workflowInfo.getVariables());

        // 办理任务
        taskService.complete(taskId, workflowInfo.getVariables());
        return null;
    }

}
