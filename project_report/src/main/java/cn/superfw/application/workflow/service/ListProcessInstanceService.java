package cn.superfw.application.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import cn.superfw.framework.utils.StringUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class ListProcessInstanceService extends BaseBLogicService<WorkflowInfo, List<Map<String, Object>>> {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Override
    protected List<Map<String, Object>> doService(WorkflowInfo workflowInfo) {

        List<Map<String, Object>> lst = new ArrayList<>();

        Map<String, Object> map = null;

        List<HistoricProcessInstance> historicProcessInstanceList = null;

        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();

        if (!AppContants.USERNAME_ADMIN.equals(workflowInfo.getUsername()) && StringUtil.isNotEmpty(workflowInfo.getUsername())) {
            query.startedBy(workflowInfo.getUsername());
        }
        if (StringUtil.isNotEmpty(workflowInfo.getProcessDefinitionKey())) {
        	query.processDefinitionKey(workflowInfo.getProcessDefinitionKey());
        }
        if (StringUtil.isNotEmpty(workflowInfo.getInstanceId())) {
        	query.processInstanceId(workflowInfo.getInstanceId());
        }
        if (StringUtil.isNotEmpty(workflowInfo.getBusinessKey())) {
        	query.processInstanceBusinessKey(workflowInfo.getBusinessKey());
        }


        historicProcessInstanceList = query.orderByProcessInstanceStartTime().asc().list();

        for (HistoricProcessInstance processInstance : historicProcessInstanceList) {

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
            map.put("bizKey", processInstance.getBusinessKey());

            lst.add(map);
        }
        return lst;
    }
}
