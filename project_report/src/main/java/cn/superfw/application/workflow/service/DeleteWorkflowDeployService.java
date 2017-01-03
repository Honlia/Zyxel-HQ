package cn.superfw.application.workflow.service;

import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.workflow.dto.WorkflowInfo;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class DeleteWorkflowDeployService extends BaseBLogicService<WorkflowInfo, Void> {

    @Autowired
    private RepositoryService repositoryService;

    @Override
    protected Void doService(WorkflowInfo workflowInfo) {

        try {
            repositoryService.deleteDeployment(workflowInfo.getDeploymentId(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
