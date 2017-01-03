package cn.superfw.application.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.framework.web.BaseBLogicService;

@Service
public class ListProceessDefinitionService extends BaseBLogicService<Void, List<Map<String, Object>>> {

    @Autowired
    private RepositoryService repositoryService;

    @Override
    protected List<Map<String, Object>> doService(Void param) {

        List<Map<String, Object>> lst = new ArrayList<>();

        Map<String, Object> map = null;

        List<ProcessDefinition> processDefinitionList = repositoryService
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionKey().asc()
                .list();
        for (ProcessDefinition processDefinition : processDefinitionList) {

            map = new HashMap<String, Object>();
            map.put("processDefinitionId", processDefinition.getId());
            map.put("processDefinitionKey", processDefinition.getKey());
            map.put("processDefinitionName", processDefinition.getName());
            map.put("processDefinitionVersion", processDefinition.getVersion());
            map.put("status", processDefinition.isSuspended() ? "Suspended" : "Activite");
            lst.add(map);
        }

        return lst;
    }
}
