package cn.superfw.application.model.m01.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superfw.application.common.AppContants;
import cn.superfw.application.common.SendMailUtil;
import cn.superfw.application.config.SystemConfig;
import cn.superfw.application.dao.AccountDAO;
import cn.superfw.application.dao.ProjectDAO;
import cn.superfw.application.domain.Account;
import cn.superfw.application.domain.Agent;
import cn.superfw.application.domain.Project;
import cn.superfw.application.domain.UpdateLog;
import cn.superfw.application.model.m01.dto.JudgePriceCommand;
import cn.superfw.application.web.uvo.MyUVO;
import cn.superfw.application.workflow.dto.WorkflowInfo;
import cn.superfw.framework.CommonContants;
import cn.superfw.framework.utils.CalendarUtil;
import cn.superfw.framework.utils.MailUtil;
import cn.superfw.framework.utils.StringUtil;
import cn.superfw.framework.web.BLogicService;
import cn.superfw.framework.web.BaseController;

@Controller
public class SpecialStockController extends BaseController {

	private static final String MSG_MAIL_SERVER_ERR = "邮件服务器异常,请联系管理员修复后再提交。";

	@Autowired
	private BLogicService<Project, Long> saveReportService;

	@Autowired
	private BLogicService<WorkflowInfo, Map<String, Object>> startProcessService;

	@Autowired
	BLogicService<WorkflowInfo, Void> deleteProcessInstanceService;

	@Autowired
	private BLogicService<WorkflowInfo, String> dispatchTaskService;

	@Autowired
	private BLogicService<WorkflowInfo, Void> completeTaskService;

	@Autowired
	private BLogicService<JudgePriceCommand, Integer> judgePriceService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private BLogicService<Agent, List<Agent>> getAgentListService;

	@Autowired
	BLogicService<String, List<UpdateLog>> getUpdateLogListService;

	@Autowired
	private BLogicService<WorkflowInfo, List<Map<String, Object>>> listTaskService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private ProjectDAO projectDAO;

	@ModelAttribute("depts")
	public Map<String, String> populateDepts() {
		Map<String, String> depts = AppContants.DEPT_BIZ_NAMES;
		return depts;
	}

	@ModelAttribute("agentNames")
	public Map<String, String> populateAgentNamesMap(HttpSession session) {
		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();
		Agent agentSearchCommand = new Agent();
		if (AppContants.POSITION_SALES.equals(uvo.getPosition())) {
			agentSearchCommand.setBusinessDirector(username);
		}
		List<Agent> agentList = getAgentListService.execute(agentSearchCommand);
		Map<String, String> agentNamesMap = new LinkedHashMap<>();
		for (Agent agent : agentList) {
			agentNamesMap.put(String.valueOf(agent.getId()), agent.getAgentName());
		}
		return agentNamesMap;
	}


	@RequestMapping(value="/editReport/{businessKey}", params={"specialApply"}, method=RequestMethod.POST)
	public String specialApply(ModelMap model, Project project,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@PathVariable String businessKey,
			String taskComment,
			HttpSession session) {

		if ("true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value()) && !MailUtil.checkMailServer()) {
			setModelInfos(model, project, instanceId, taskId, businessKey, session);
			return "06_updateReport";
		}

		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();


		// 业务部主管驳回，MDM驳回，总经理驳回的时候不用启动新流程
		if (!AppContants.PROJECT_STATUS_SPECIAL_BIZM_DISAGREE.equals(project.getStatus()) &&
				!AppContants.PROJECT_STATUS_SPECIAL_MDM_DISAGREE.equals(project.getStatus()) &&
				!AppContants.PROJECT_STATUS_SPECIAL_GM_DISAGREE.equals(project.getStatus())) {
			// 结束报备流程
			WorkflowInfo completeTaskServiceInput = new WorkflowInfo();
			completeTaskServiceInput.setUsername(username);
			completeTaskServiceInput.setTaskId(taskId);

			completeTaskServiceInput.putVariable(AppContants.TASK_COMMENT, "结束报备流程并启动价格审批流程");
			completeTaskServiceInput.putVariable(AppContants.FLOW_DECISION_KEY, AppContants.DECISION_AGREE);
			completeTaskService.execute(completeTaskServiceInput);

			// 启动价格审批流程,并返回流程实例信息
			WorkflowInfo startProcessServiceInput = new WorkflowInfo();
			startProcessServiceInput.setProcessDefinitionKey(AppContants.FLOW_SPECIAL_STOCK);
			startProcessServiceInput.setUsername(username);
			startProcessServiceInput.setBusinessKey(businessKey);
			startProcessServiceInput.putVariable(AppContants.TASK_COMMENT, "启动特价备货流程。");
			Map<String, Object> processInstanceInfo = startProcessService.execute(startProcessServiceInput);

			instanceId = String.valueOf(processInstanceInfo.get("instanceId"));
			taskId = String.valueOf(processInstanceInfo.get("taskId"));

		}

		if (StringUtil.isEmpty(taskComment)) {
			taskComment = "特价申请提出";
		}

		handlerTask(instanceId, taskId, taskComment, username, AppContants.DECISION_AGREE, project.getStatus());

		// 业务信息保存
		project.setCurrentProcess(AppContants.FLOW_SPECIAL_STOCK);
		project.setStatus(AppContants.PROJECT_STATUS_SPECIAL_APPLY);
		saveReportService.execute(project);

		model.clear();
		return "redirect:/todoTasks";
	}

	@RequestMapping(value="/editReport/{businessKey}", params={"deliveryDateConfirm"}, method=RequestMethod.POST)
	public String deliveryDateConfirm(ModelMap model, Project project,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@PathVariable String businessKey,
			String taskComment,
			HttpSession session) {

		if ("true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value()) && !MailUtil.checkMailServer()) {
			setModelInfos(model, project, instanceId, taskId, businessKey, session);
			return "06_updateReport";
		}

		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();

		// 业务信息保存
		project.setStatus(AppContants.PROJECT_STATUS_DELIVERY_DATE_CONFIRM);
		saveReportService.execute(project);

		Integer decision = AppContants.DECISION_AGREE;

		if (StringUtil.isEmpty(taskComment)) {
			taskComment = "库存与最快交期已确认";
		}

		handlerTask(instanceId, taskId, taskComment, username, decision, project.getStatus());

		model.clear();
		return "redirect:/todoTasks";
	}

	@RequestMapping(value="/editReport/{businessKey}", params={"deliveryDateAgree"}, method=RequestMethod.POST)
	public String deliveryDateAgree(ModelMap model, Project project,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@PathVariable String businessKey,
			String taskComment,
			HttpSession session) {

		if ("true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value()) && !MailUtil.checkMailServer()) {
			setModelInfos(model, project, instanceId, taskId, businessKey, session);
			return "06_updateReport";
		}

		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();

		//Integer decision = AppContants.DECISION_AGREE;

		// 如果申请价格大于等于价格A则decision=2, 否则decision=1
		JudgePriceCommand judgePriceCommand = new JudgePriceCommand(uvo.getPosition(), project);
		Integer decision = judgePriceService.execute(judgePriceCommand);

		// 如果申请价格大于等于价格A则
		if (decision == AppContants.DECISION_AUTH_AGREE) {
			project.setStatus(AppContants.PROJECT_STATUS_ORDER_EXECUTE);
			taskComment = "申请价格大于等于价格A,执行订单";
		} else {
			project.setStatus(AppContants.PROJECT_STATUS_DELIVERY_DATE_AGREE);
		}
		// 业务信息保存
		saveReportService.execute(project);

		if (StringUtil.isEmpty(taskComment)) {
			taskComment = "最终交期同意";
		}

		handlerTask(instanceId, taskId, taskComment, username, decision, project.getStatus());

		model.clear();
		return "redirect:/todoTasks";
	}



	@RequestMapping(value="/editReport/{businessKey}", params={"deliveryDateDisagree"}, method=RequestMethod.POST)
	public String deliveryDateDisagree(ModelMap model, Project project,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@PathVariable String businessKey,
			String taskComment,
			HttpSession session) {

		if ("true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value()) && !MailUtil.checkMailServer()) {
			setModelInfos(model, project, instanceId, taskId, businessKey, session);
			return "06_updateReport";
		}

		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();

		// 业务信息保存
		project.setStatus(AppContants.PROJECT_STATUS_DELIVERY_DATE_DISAGREE);
		saveReportService.execute(project);

		Integer decision = AppContants.DECISION_DISAGREE;

		if (StringUtil.isEmpty(taskComment)) {
			taskComment = "最终交期不同意";
		}

		handlerTask(instanceId, taskId, taskComment, username, decision, project.getStatus());

		model.clear();
		return "redirect:/todoTasks";
	}

	@RequestMapping(value="/editReport/{businessKey}", params={"specialAgree"}, method=RequestMethod.POST)
	public String specialAgree(ModelMap model, Project project,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@PathVariable String businessKey,
			String taskComment,
			HttpSession session) {

		if ("true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value()) && !MailUtil.checkMailServer()) {
			setModelInfos(model, project, instanceId, taskId, businessKey, session);
			return "06_updateReport";
		}

		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();

		// 确定是同意还是权限内同意
		JudgePriceCommand judgePriceCommand = new JudgePriceCommand(uvo.getPosition(), project);
		Integer decision = judgePriceService.execute(judgePriceCommand);

		// 业务信息保存
		// 同意，提交给上一级
		if (decision == AppContants.DECISION_AGREE) {

			switch(uvo.getPosition()){
			case AppContants.POSITION_BUSINESS_MANAGER:
				project.setStatus(AppContants.PROJECT_STATUS_SPECIAL_BIZM_AGREE);
				break;
			case AppContants.POSITION_MDM_MANAGER:
				project.setStatus(AppContants.PROJECT_STATUS_SPECIAL_MDM_AGREE);
				break;
			case AppContants.POSITION_GENERAL_MANAGER:
				project.setStatus(AppContants.PROJECT_STATUS_ORDER_EXECUTE);
				taskComment = "特价同意,执行订单";
				break;
			default:
				break;
			}

			if (StringUtil.isEmpty(taskComment)) {
				taskComment = "特价同意,提交给上级审批";
			}
			// 权限内同意,执行订单
		} else {
			project.setStatus(AppContants.PROJECT_STATUS_ORDER_EXECUTE);
			if (StringUtil.isEmpty(taskComment)) {
				taskComment = "特价权限在权限范围内,执行订单";
			}
		}
		saveReportService.execute(project);

		handlerTask(instanceId, taskId, taskComment, username, decision, project.getStatus());

		model.clear();
		return "redirect:/todoTasks";
	}

	@RequestMapping(value="/editReport/{businessKey}", params={"specialDisagree"}, method=RequestMethod.POST)
	public String specialDisagree(ModelMap model, Project project,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@PathVariable String businessKey,
			String taskComment,
			HttpSession session) {

		if ("true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value()) && !MailUtil.checkMailServer()) {
			setModelInfos(model, project, instanceId, taskId, businessKey, session);
			return "06_updateReport";
		}

		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();

		// 业务信息保存
		switch(uvo.getPosition()){
		case AppContants.POSITION_BUSINESS_MANAGER:
			project.setStatus(AppContants.PROJECT_STATUS_SPECIAL_BIZM_DISAGREE);
			break;
		case AppContants.POSITION_MDM_MANAGER:
			project.setStatus(AppContants.PROJECT_STATUS_SPECIAL_MDM_DISAGREE);
			break;
		case AppContants.POSITION_GENERAL_MANAGER:
			project.setStatus(AppContants.PROJECT_STATUS_SPECIAL_GM_DISAGREE);
			break;
		default:
			break;
		}
		saveReportService.execute(project);

		// 不同意
		Integer decision = AppContants.DECISION_DISAGREE;

		if (StringUtil.isEmpty(taskComment)) {
			taskComment = "特价不同意";
		}

		handlerTask(instanceId, taskId, taskComment, username, decision, project.getStatus());

		model.clear();
		return "redirect:/todoTasks";
	}

	@RequestMapping(value="/editReport/{businessKey}", params={"orderExecute"}, method=RequestMethod.POST)
	public String orderExecute(ModelMap model, Project project,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@PathVariable String businessKey,
			String taskComment,
			HttpSession session) {

		if ("true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value()) && !MailUtil.checkMailServer()) {
			setModelInfos(model, project, instanceId, taskId, businessKey, session);
			return "06_updateReport";
		}

		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();

		// 业务信息保存
		project.setStatus(AppContants.PROJECT_STATUS_ORDER_COMPLETED);
		saveReportService.execute(project);

		// 同意
		Integer decision = AppContants.DECISION_AGREE;

		if (StringUtil.isEmpty(taskComment)) {
			taskComment = "全部出货结案";
		}

		handlerTask(instanceId, taskId, taskComment, username, decision, project.getStatus());

		model.clear();
		return "redirect:/todoTasks";
	}

	@RequestMapping(value="/editReport/{businessKey}", params={"notShipped"}, method=RequestMethod.POST)
	public String notShippedClose(ModelMap model, Project project,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@PathVariable String businessKey,
			String taskComment,
			HttpSession session) {

		if ("true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value()) && !MailUtil.checkMailServer()) {
			setModelInfos(model, project, instanceId, taskId, businessKey, session);
			return "06_updateReport";
		}

		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();

		// 业务信息保存
		project.setStatus(AppContants.PROJECT_STATUS_APPLY_NOT_SHIPPED); // 未出货结案
		saveReportService.execute(project);

		// 同意
		Integer decision = AppContants.DECISION_AGREE;

		if (StringUtil.isEmpty(taskComment)) {
			taskComment = "未出货结案";
		}

		handlerTask(instanceId, taskId, taskComment, username, decision, project.getStatus());


		// 未出货结案 通知总经理，运作部主管，运作部商务，业务部主管
		List<Account> accountList = accountDAO.find("from Account a where a.position in (?,?,?,?,?) or (a.position = ? and a.dept = ?)",
				AppContants.POSITION_GENERAL_MANAGER,
				AppContants.POSITION_BUSINESS_MANAGER,
				AppContants.POSITION_OPERATION_MANAGER,
				AppContants.POSITION_OPERATION_STOCK,
				AppContants.POSITION_OPERATION_BIZ,
				AppContants.POSITION_BUSINESS_MANAGER,
				uvo.getDept());
		for (Account a : accountList) {
			// Mail:运作部库管,业务部主管，业务部助理
			SendMailUtil.send("流程结束", instanceId, "特价备货流程", username, a, project);
		}

		model.clear();
		return "redirect:/todoTasks";
	}

	@RequestMapping(value="/editReport/{businessKey}", params={"partShipped"}, method=RequestMethod.POST)
	public String partShippedClose(ModelMap model, Project project,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@PathVariable String businessKey,
			String taskComment,
			HttpSession session) {

		if ("true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value()) && !MailUtil.checkMailServer()) {
			setModelInfos(model, project, instanceId, taskId, businessKey, session);
			return "06_updateReport";
		}

		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();

		// 业务信息保存
		project.setStatus(AppContants.PROJECT_STATUS_APPLY_PART_SHIPPED); // 未出货结案
		saveReportService.execute(project);

		// 同意
		Integer decision = AppContants.DECISION_AGREE;

		if (StringUtil.isEmpty(taskComment)) {
			taskComment = "部分出货结案";
		}

		handlerTask(instanceId, taskId, taskComment, username, decision, project.getStatus());


		// 部分出货结案 通知总经理，运作部主管，运作部商务，业务部主管
		List<Account> accountList = accountDAO.find("from Account a where a.position in (?,?,?,?,?) or (a.position = ? and a.dept = ?)",
				AppContants.POSITION_GENERAL_MANAGER,
				AppContants.POSITION_BUSINESS_MANAGER,
				AppContants.POSITION_OPERATION_MANAGER,
				AppContants.POSITION_OPERATION_STOCK,
				AppContants.POSITION_OPERATION_BIZ,
				AppContants.POSITION_BUSINESS_MANAGER,
				uvo.getDept());
		for (Account a : accountList) {
			// Mail:运作部库管,业务部主管，业务部助理
			SendMailUtil.send("流程结束", instanceId, "特价备货流程", username, a, project);
		}

		model.clear();
		return "redirect:/todoTasks";
	}


	@RequestMapping(value="/editReport/{businessKey}", params={"close"}, method=RequestMethod.POST)
	public String close(ModelMap model, Project project,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@PathVariable String businessKey,
			String taskComment,
			HttpSession session) {

		if ("true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value()) && !MailUtil.checkMailServer()) {
			setModelInfos(model, project, instanceId, taskId, businessKey, session);
			return "06_updateReport";
		}

		// 业务信息保存
		project.setStatus(AppContants.PROJECT_STATUS_APPLY_CANCEL);
		saveReportService.execute(project);

		WorkflowInfo wfInfo = new WorkflowInfo();
		wfInfo.setInstanceId(instanceId);
		wfInfo.setReasonText("取消申请，结案。");
		deleteProcessInstanceService.execute(wfInfo);

		model.clear();
		return "redirect:/todoTasks";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/notifyGPCal", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> notifyGPCal(@RequestBody Map<String, String> map) {

		Map<String, String> result = new HashMap<>();

		String instanceId = map.get("instanceId");
		String businessKey = map.get("businessKey");

		HistoricProcessInstance historicProcessInstance =
				historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(instanceId)
				.singleResult();

		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(historicProcessInstance.getProcessDefinitionId())
				.singleResult();

		String processDefName = processDefinition.getName();
		String processInsId = historicProcessInstance.getId();
		String startUsername = historicProcessInstance.getStartUserId();

		Project project = projectDAO.get(Long.valueOf(businessKey));

		List<Account> accountList = accountDAO.createCriteria()
				.add(Restrictions.eq("position", AppContants.POSITION_OPERATION_MANAGER))
				.list();
		for (Account a : accountList) {
			SendMailUtil.sendGP(processInsId, processDefName, startUsername, a, project);
		}

		result.put("result", "OK");
		return result;
	}

	@RequestMapping(value="/notifyHistory", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> notifyHistory(@RequestBody Map<String, String> map) {

		Map<String, String> result = new HashMap<>();

		String instanceId = map.get("instanceId");
		String businessKey = map.get("businessKey");

		HistoricProcessInstance historicProcessInstance =
				historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(instanceId)
				.singleResult();

		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(historicProcessInstance.getProcessDefinitionId())
				.singleResult();

		String processDefName = processDefinition.getName();
		String processInsId = historicProcessInstance.getId();
		String startUsername = historicProcessInstance.getStartUserId();

		Project project = projectDAO.get(Long.valueOf(businessKey));

		List<Account> accountList = accountDAO.find("from Account a where a.position in (?,?)",
				AppContants.POSITION_OPERATION_MANAGER, AppContants.POSITION_OPERATION_BIZ);
		for (Account a : accountList) {
			SendMailUtil.sendHis(processInsId, processDefName, startUsername, a, project);
		}

		result.put("result", "OK");
		return result;
	}

	/**
	 * 完成当前任务,并根据流程定义分配下一个任务的参与者
	 *
	 * @param instanceId 流程实例ID
	 * @param taskId 当前任务ID
	 * @param taskComment 任务批注
	 * @param username 任务办理人
	 * @param decision 流向决策：同意,权限内同意,驳回
	 * @param projectStatus 项目状态
	 */
	private void handlerTask(String instanceId, String taskId,
			String taskComment, String username, Integer decision, String projectStatus) {
		// 认领任务并完成任务
		WorkflowInfo completeTaskServiceInput = new WorkflowInfo();
		completeTaskServiceInput.setUsername(username);
		completeTaskServiceInput.setTaskId(taskId);

		completeTaskServiceInput.putVariable(AppContants.TASK_COMMENT, taskComment);
		completeTaskServiceInput.putVariable(AppContants.FLOW_DECISION_KEY, decision);
		completeTaskService.execute(completeTaskServiceInput);

		// 根据任务类型自动分配下一个任务的参与者
		WorkflowInfo dispatchTaskServiceInput = new WorkflowInfo();
		dispatchTaskServiceInput.setInstanceId(instanceId);
		dispatchTaskServiceInput.setProjectStatus(projectStatus);
		dispatchTaskService.execute(dispatchTaskServiceInput);
	}

	/**
	 * 获取历史任务列表
	 *
	 * @param username
	 * @param instanceId
	 * @param project
	 * @return
	 */
	private List<Map<String, String>> getHistoryTasks(String username, String instanceId,
			Project project) {

		WorkflowInfo listTaskServiceInput = new WorkflowInfo();
		listTaskServiceInput.setTaskStatus(AppContants.TASK_STATUS_COMPLETED);
		listTaskServiceInput.setInstanceId(instanceId);
		List<Map<String, Object>> instanceHistoricTaskList = listTaskService.execute(listTaskServiceInput);

		List<Map<String, String>> processTaskHistoryList  = new ArrayList<>();
		Map<String, String> taskHistory = null;
		String action = null;
		for (Map<String, Object> map : instanceHistoricTaskList) {
			String processDefinitionName = String.valueOf(map.get("processDefinitionName"));
			String taskName = String.valueOf(map.get("taskName"));
			String taskAssignee = String.valueOf(map.get("taskAssignee"));
			Integer decision = (Integer)map.get(AppContants.FLOW_DECISION_KEY);

			if (decision != null && decision == AppContants.DECISION_DISAGREE) {
				action = "驳回了";
			} else {
				action = "办理了";
			}

			String taskEndTime = "";
			if (map.get("taskEndTime") != null) {
				taskEndTime = CalendarUtil.formatDateTime((Date)map.get("taskEndTime"));
			}

			taskHistory = new HashMap<>();

			if ("null".equals(taskAssignee)) {
				taskHistory.put("title", "[" +
						taskEndTime + "]  [" +
						processDefinitionName + "-" + instanceId + "]被申请者取消");
				taskHistory.put("commnet", String.valueOf(map.get("deleteReason")));
			} else {
				taskHistory.put("title", "[" +
						taskEndTime + "]  [" +
						taskAssignee + "]" + action+ "[" +
						processDefinitionName + "-" + instanceId + "]中的[" +
						taskName + "]任务");
				taskHistory.put("commnet", String.valueOf(map.get(AppContants.TASK_COMMENT)));
			}
			processTaskHistoryList.add(taskHistory);
		}
		return processTaskHistoryList;
	}

	private boolean checkHasTodoTask(String instanceId, String businessKey, String username) {

		Long count = taskService.createTaskQuery()
				.processInstanceId(instanceId)
				.processInstanceBusinessKey(businessKey)
				.taskCandidateOrAssigned(username)
				.count();
		if (count != 0) {
			return true;
		}
		return false;

	}

	private void setModelInfos(ModelMap model, Project project, String instanceId, String taskId, String businessKey,
			HttpSession session) {
		model.addAttribute("instanceId", instanceId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("businessKey", businessKey);

		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();

		model.addAttribute("project", project);
		model.addAttribute("projectStatusNames", AppContants.PROJECT_STATUS_NAMES);

		List<Map<String, String>> processTaskHistoryList = getHistoryTasks(
				username, instanceId, project);
		model.addAttribute("processTaskHistoryList", processTaskHistoryList);

		// 判断是不是当前活动任务
		model.addAttribute("hasTodoTask", checkHasTodoTask(instanceId, businessKey, username));

		Agent agent = new Agent();
		agent.setId(Long.parseLong(project.getCooperativePartner()));
		List<Agent> agentList = getAgentListService.execute(agent);
		model.addAttribute("agent", agentList.get(0));

		List<UpdateLog> updateLogList = getUpdateLogListService.execute(String.valueOf(project.getId()));
		model.addAttribute("updateLogList", updateLogList);

		model.addAttribute(CommonContants.ERR, MSG_MAIL_SERVER_ERR);

	}

}
