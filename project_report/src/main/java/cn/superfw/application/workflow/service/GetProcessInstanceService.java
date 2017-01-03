package cn.superfw.application.workflow.service;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.common.AppContants;
import cn.superfw.application.workflow.dto.WorkflowInfo;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GetProcessInstanceService extends BaseBLogicService<WorkflowInfo, Map<String, Object>> {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Override
    protected Map<String, Object> doService(WorkflowInfo workflowInfo) {

        Map<String, Object> map = null;

        HistoricProcessInstance processInstance = null;

        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
    	query.processDefinitionKey(workflowInfo.getProcessDefinitionKey());
    	query.processInstanceBusinessKey(workflowInfo.getBusinessKey());
    	processInstance = query.singleResult();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processInstance.getProcessDefinitionId())
                .singleResult();

        map = new HashMap<String, Object>();
        map.put("instanceId",processInstance.getId());
        map.put("processDefinitionName", processDefinition.getName());
        map.put("processDefinitionId", processInstance.getProcessDefinitionId());
        map.put("processDefinitionVersion", processDefinition.getVersion());
        map.put("startUserId", processInstance.getStartUserId());
        map.put("startTime", processInstance.getStartTime());
        map.put("endTime", processInstance.getEndTime());
        map.put("status", processInstance.getEndTime() == null ?
                AppContants.FLOW_STATUS_PROCESSING : AppContants.FLOW_STATUS_CLOSED);
        map.put("processVariables", processInstance.getProcessVariables());

        return map;
    }
}
