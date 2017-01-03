package cn.superfw.application.workflow.service;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.framework.web.BaseBLogicService;

@Service
public class DeleteAllDeploymentService extends BaseBLogicService<Void, Void> {

    @Autowired
    RepositoryService repositoryService;

    @Override
    protected Void doService(Void param) {
        List<Deployment> deployments = repositoryService.createDeploymentQuery().list();
        for (Deployment deployment : deployments) {
            repositoryService.deleteDeployment(deployment.getId(), true);
        }
        return null;
    }

}
