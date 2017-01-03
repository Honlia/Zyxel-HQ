package cn.superfw.application.workflow.service;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.common.AppContants;
import cn.superfw.application.common.SendMailUtil;
import cn.superfw.application.dao.AccountDAO;
import cn.superfw.application.dao.ProjectDAO;
import cn.superfw.application.domain.Account;
import cn.superfw.application.domain.Project;
import cn.superfw.application.workflow.dto.WorkflowInfo;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class DispatchTaskService extends BaseBLogicService<WorkflowInfo, String> {

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private ProjectDAO projectDAO;

    @SuppressWarnings("unchecked")
    @Override
    protected String doService(WorkflowInfo workflowInfo) {

        HistoricProcessInstance historicProcessInstance =
                historyService.createHistoricProcessInstanceQuery()
                              .processInstanceId(workflowInfo.getInstanceId())
                              .singleResult();

        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(historicProcessInstance.getProcessDefinitionId())
                .singleResult();

        String processDefName = processDefinition.getName();
        String processInsId = historicProcessInstance.getId();
        String startUsername = historicProcessInstance.getStartUserId();

        Account startAccount = (Account)accountDAO.createCriteria()
                .add(Restrictions.eq("username", startUsername)).uniqueResult();

        Project project = projectDAO.get(Long.valueOf(historicProcessInstance.getBusinessKey()));

        Task task = taskService.createTaskQuery()
                .processInstanceId(workflowInfo.getInstanceId())
                .singleResult();

        // 没有后定的任务,说明流程结束
        if (task == null) {

            HistoricActivityInstance actIns = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(workflowInfo.getInstanceId())
                    .activityId("endevent1")
                    .singleResult();
            if (actIns != null) {
                SendMailUtil.send("流程结束", processInsId, processDefName, startUsername, startAccount, project);
            }
            return null;
        }

        String taskId = task.getId();
        String taskName = task.getName();
        String taskDefinitionKey = task.getTaskDefinitionKey();

        String dept = project.getDept();

        List<Account> accountList = null;

        // 指定超级管理员是所有任务的参与者
        taskService.addCandidateUser(taskId, AppContants.USERNAME_ADMIN);

        // 根据当前任务的类型,指派参与者
        switch (taskDefinitionKey) {
        case AppContants.TASK_PROJECT_REPORT_APPLY:
            // 指派给流程发起人
            taskService.addCandidateUser(taskId, startUsername);
            SendMailUtil.send(taskName, processInsId, processDefName, startUsername, startAccount, project);
        case AppContants.TASK_PROJECT_LOST_BID_CLOSE:
            // 通知流程发起人，业务部主管
        	taskService.addCandidateUser(taskId, startUsername);
            SendMailUtil.send(taskName, processInsId, processDefName, startUsername, startAccount, project);
            accountList = accountDAO.createCriteria()
                    .add(Restrictions.eq("position", AppContants.POSITION_BUSINESS_MANAGER))
                    .add(Restrictions.eq("dept", dept))
                    .list();
            for (Account a : accountList) {
                //taskService.addCandidateUser(taskId, a.getUsername());
                SendMailUtil.send(taskName, processInsId, processDefName, startUsername, a, project);
            }
            break;
        case AppContants.TASK_SPECIAL_STOCK_APPLY:
            // 指派给流程发起人
            taskService.addCandidateUser(taskId, startUsername);
            SendMailUtil.send(taskName, processInsId, processDefName, startUsername, startAccount, project);

            // 驳回的时候
            String projectStatus = workflowInfo.getProjectStatus();
            if (AppContants.PROJECT_STATUS_SPECIAL_BIZM_DISAGREE.equals(projectStatus)) {
                // 通知运作部主管
                accountList = accountDAO.find("from Account a where a.position = ?", dept);
                for (Account a : accountList) {
                    SendMailUtil.send(taskName, processInsId, processDefName, startUsername, a, project);
                }
            } else if (AppContants.PROJECT_STATUS_SPECIAL_MDM_DISAGREE.equals(projectStatus)) {
                // 通知运作部主管 业务部主管
                // 业务部主管审批 从部门表检索业务部主管
                accountList = accountDAO.find("from Account a where a.position = ? or (a.position = ? and a.dept = ?)",
                        AppContants.POSITION_OPERATION_MANAGER,
                        AppContants.POSITION_BUSINESS_MANAGER,
                        dept);
                for (Account a : accountList) {
                    SendMailUtil.send(taskName, processInsId, processDefName, startUsername, a, project);
                }
            } else if (AppContants.PROJECT_STATUS_SPECIAL_GM_DISAGREE.equals(projectStatus)) {
                // 通知运作部主管 MDM主管 业务部主管
                accountList = accountDAO.find("from Account a where a.position in (?,?) or (a.position = ? and a.dept = ?)",
                        AppContants.POSITION_OPERATION_MANAGER,
                        AppContants.POSITION_MDM_MANAGER,
                        AppContants.POSITION_BUSINESS_MANAGER,
                        dept);
                for (Account a : accountList) {
                    SendMailUtil.send(taskName, processInsId, processDefName, startUsername, a, project);
                }
            }
            break;
        case AppContants.TASK_FASTEST_DELIVERY_DATE_CONFIRM:
            // 最快交期确认 检索运作部备货
            accountList = accountDAO.createCriteria()
                .add(Restrictions.eq("position", AppContants.POSITION_OPERATION_STOCK))
                .list();
            for (Account a : accountList) {
                taskService.addCandidateUser(taskId, a.getUsername());
                SendMailUtil.send(taskName, processInsId, processDefName, startUsername, a, project);
            }
            break;
        case AppContants.TASK_FINAL_DELIVERY_DATE_CONFIRM:
            // 指派给流程发起人
            taskService.addCandidateUser(taskId, startUsername);
            SendMailUtil.send(taskName, processInsId, processDefName, startUsername, startAccount, project);
            break;
        case AppContants.TASK_BUSINESS_DEPT_MANAGER_APPROVE:
            // 业务部主管审批 从部门表检索业务部主管
            accountList = accountDAO.createCriteria()
                .add(Restrictions.eq("position", AppContants.POSITION_BUSINESS_MANAGER))
                .add(Restrictions.eq("dept", dept))
                .list();
            for (Account a : accountList) {
                taskService.addCandidateUser(taskId, a.getUsername());
                SendMailUtil.send(taskName, processInsId, processDefName, startUsername, a, project);
            }
            break;
        case AppContants.TASK_MDM_MANAGER_APPROVE:
            // MDM主管审批 从部门表检索MDM部主管
            accountList = accountDAO.createCriteria()
                .add(Restrictions.eq("position", AppContants.POSITION_MDM_MANAGER))
                .list();
            for (Account a : accountList) {
                taskService.addCandidateUser(taskId, a.getUsername());
                SendMailUtil.send(taskName, processInsId, processDefName, startUsername, a, project);
            }
            break;
        case AppContants.TASK_GENERAL_MANAGER_APPROVE:
            // 总经理审批 从部门表检索总经理主管
            accountList = accountDAO.createCriteria()
                .add(Restrictions.eq("position", AppContants.POSITION_GENERAL_MANAGER))
                .list();
            for (Account a : accountList) {
                taskService.addCandidateUser(taskId, a.getUsername());
                SendMailUtil.send(taskName, processInsId, processDefName, startUsername, a, project);
            }
            break;
        case AppContants.TASK_EXECUTE_ORDER:
            // 执行订单 从部门表检索运作部商务
            accountList = accountDAO.find("from Account a where a.position in (?,?,?) or (a.position = ? and a.dept = ?)",
                    AppContants.POSITION_OPERATION_MANAGER,
                    AppContants.POSITION_OPERATION_STOCK,
                    AppContants.POSITION_OPERATION_BIZ,
                    AppContants.POSITION_BUSINESS_MANAGER,
                    dept);
            for (Account a : accountList) {
                if (AppContants.POSITION_OPERATION_BIZ.equals(a.getPosition())) {
                    taskService.addCandidateUser(taskId, a.getUsername());
                }
                // Mail:运作部库管,业务部主管，业务部助理
                SendMailUtil.send(taskName, processInsId, processDefName, startUsername, a, project);
            }
            // Mail:流程发起者(sales)
            SendMailUtil.send(taskName, processInsId, processDefName, startUsername, startAccount, project);
            break;
        default:
            break;
        }

        return taskId;
    }

}
