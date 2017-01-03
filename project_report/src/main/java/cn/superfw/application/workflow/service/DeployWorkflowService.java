package cn.superfw.application.workflow.service;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.common.AppContants;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class DeployWorkflowService extends BaseBLogicService<String, Map<String, Object>> {

    @Autowired
    private RepositoryService repositoryService;

    @Override
    protected Map<String, Object> doService(String processDefinitionKey) {

        Map<String, Object> map = null;

        Deployment deployment = null;

        if (AppContants.FLOW_PROJECT_REPORT.equals(processDefinitionKey)) {
            // 部署流程
            deployment = repositoryService.createDeployment()
                .addClasspathResource("workflow/project_report_flow.bpmn")
                .addClasspathResource("workflow/project_report_flow.png")
                .name(AppContants.FLOW_NAME_PROJECT_REPORT)
                .deploy();
        } else {
            // 部署流程
            deployment = repositoryService.createDeployment()
                .addClasspathResource("workflow/special_stock_flow.bpmn")
                .addClasspathResource("workflow/special_stock_flow.png")
                .name(AppContants.FLOW_NAME_SPECIAL_STOCK)
                .deploy();
        }
        map = new HashMap<String, Object>();
        map.put("deploymentId", deployment.getId());
        map.put("deploymentName", deployment.getName());
        map.put("deploymentTime", deployment.getDeploymentTime());
        return map;
    }

}
