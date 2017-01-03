package cn.superfw.framework.activiti;

import java.util.Date;

import org.activiti.engine.impl.cfg.IdGenerator;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.history.DefaultHistoryManager;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;

public class ProcessHistoryManager extends DefaultHistoryManager {
    @Override
    public void recordProcessInstanceStart(ExecutionEntity processInstance) {
        if(isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
          HistoricProcessInstanceEntity historicProcessInstance = new HistoricProcessInstanceEntity(processInstance);
          getDbSqlSession().insert(historicProcessInstance);

          IdGenerator idGenerator = Context.getProcessEngineConfiguration().getIdGenerator();

          String processDefinitionId = processInstance.getProcessDefinitionId();
          String processInstanceId = processInstance.getProcessInstanceId();
          String executionId = processInstance.getId();

          HistoricActivityInstanceEntity historicActivityInstance = new HistoricActivityInstanceEntity();
          historicActivityInstance.setId(idGenerator.getNextId());
          historicActivityInstance.setProcessDefinitionId(processDefinitionId);
          historicActivityInstance.setProcessInstanceId(processInstanceId);
          historicActivityInstance.setExecutionId(executionId);
          historicActivityInstance.setActivityId(processInstance.getActivityId());
          historicActivityInstance.setActivityName((String) processInstance.getActivity().getProperty("name"));
          historicActivityInstance.setActivityType((String) processInstance.getActivity().getProperty("type"));
          Date now = Context.getProcessEngineConfiguration().getClock().getCurrentTime();
          historicActivityInstance.setStartTime(now);

          if (processInstance.getTenantId() != null) {
              historicActivityInstance.setTenantId(processInstance.getTenantId());
          }
          //通过Authentication.getAuthenticatedUserId()获取当前线程绑定的用户ID。
          historicActivityInstance.setAssignee(Authentication.getAuthenticatedUserId());

          getDbSqlSession().insert(historicActivityInstance);
        }
    }
}
