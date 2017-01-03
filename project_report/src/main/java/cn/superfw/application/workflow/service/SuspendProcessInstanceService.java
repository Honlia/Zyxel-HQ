package cn.superfw.application.workflow.service;

import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.workflow.dto.WorkflowInfo;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class SuspendProcessInstanceService extends BaseBLogicService<WorkflowInfo, Void> {
    @Autowired
    private RuntimeService runtimeService;

    @Override
    protected Void doService(WorkflowInfo workflowInfo) {

        try {
            runtimeService.suspendProcessInstanceById(workflowInfo.getInstanceId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
