package cn.superfw.application.model.m01.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.activiti.engine.TaskService;
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
import cn.superfw.application.common.UpdateLogUtil;
import cn.superfw.application.config.SystemConfig;
import cn.superfw.application.dao.AccountDAO;
import cn.superfw.application.dao.ProjectDAO;
import cn.superfw.application.domain.Account;
import cn.superfw.application.domain.Agent;
import cn.superfw.application.domain.Product;
import cn.superfw.application.domain.ProductDetail;
import cn.superfw.application.domain.Project;
import cn.superfw.application.domain.UpdateLog;
import cn.superfw.application.web.uvo.MyUVO;
import cn.superfw.application.workflow.dto.WorkflowInfo;
import cn.superfw.framework.CommonContants;
import cn.superfw.framework.utils.CalendarUtil;
import cn.superfw.framework.utils.MailUtil;
import cn.superfw.framework.utils.StringUtil;
import cn.superfw.framework.web.BLogicService;
import cn.superfw.framework.web.BaseController;

@Controller
public class ProjectReportController extends BaseController {

	private static final String MSG_MAIL_SERVER_ERR = "邮件服务器异常,请联系管理员修复后再提交。";

	private static final String MSG_PROJECT_DUPLICATE = "项目名称已经存在,请修改后再次提交。";

	@Autowired
	private BLogicService<Project, Long> saveReportService;

	@Autowired
	private BLogicService<Void, List<String>> getProductLineService;

	@Autowired
	private BLogicService<String, List<String>> getProductTypeService;

	@Autowired
	private BLogicService<Map<String, String>, List<String>> getProductModelService;

	@Autowired
	private BLogicService<Map<String, String>, Product> getProductService;

	@Autowired
	private BLogicService<WorkflowInfo, Map<String, Object>> startProcessService;

	@Autowired
	private BLogicService<WorkflowInfo, String> dispatchTaskService;

	@Autowired
	private BLogicService<WorkflowInfo, Void> completeTaskService;

	@Autowired
	private BLogicService<Long, Project> getProjectService;

	@Autowired
	private BLogicService<WorkflowInfo, List<Map<String, Object>>> listTaskService;

	@Autowired
	private BLogicService<String, String> generateProjectNumberService;

	@Autowired
	private BLogicService<Agent, List<Agent>> getAgentListService;

	@Autowired
	private BLogicService<UpdateLog, Long> saveUpdateLogService;

	@Autowired
	BLogicService<String, List<UpdateLog>> getUpdateLogListService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private AccountDAO accountDAO;


	@ModelAttribute("productLineList")
	public List<String> populateProductLineList() {
		List<String> productLineList = getProductLineService.execute(null);
		return productLineList;
	}

	@ModelAttribute("productTypeList")
	public List<String> populateProductTypeList() {
		List<String> productTypeList = getProductTypeService.execute(null);
		return productTypeList;
	}

	@ModelAttribute("productModelList")
	public List<String> populateProductModelList() {
		List<String> productLineList = getProductModelService.execute(null);
		return productLineList;
	}

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

	@RequestMapping(value="/getProduct", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getProduct(@RequestBody Map<String, String> map) {

		Product product = getProductService.execute(map);
		Map<String, Object> productMap = new HashMap<>();

		productMap.put("platformPrice", product.getPlatformPrice());
		productMap.put("openPrice", product.getOpenPrice());

		return productMap;
	}

	@RequestMapping(value="/populateProductTypeList", method=RequestMethod.POST)
	@ResponseBody
	public List<String> populateProductTypeList(@RequestBody Map<String, String> map) {
		List<String> productTypeList = getProductTypeService.execute(map.get("productLine"));
		return productTypeList;
	}

	@RequestMapping(value="/populateProductModelList", method=RequestMethod.POST)
	@ResponseBody
	public List<String> populateProductModelList(@RequestBody Map<String, String> map) {
		List<String> productModelList = getProductModelService.execute(map);
		return productModelList;
	}

	@RequestMapping(value="/newReport")
	public String initNewReport(ModelMap model, Project project, HttpSession session) {
		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		project.setDept(uvo.getDept());
		return "02_newReport";
	}

	@RequestMapping(value="/newReport", params={"apply"}, method=RequestMethod.POST)
	public String newAndApplyReport(ModelMap model, Project project, HttpSession session) {

		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();

		Long count = (Long) projectDAO.createQuery("select count(*) from Project o where o.projectName=?",
				project.getProjectName()).uniqueResult();

		if (count != null && count.intValue() != 0) {
			model.addAttribute(CommonContants.ERR, MSG_PROJECT_DUPLICATE);
			project.setDept(uvo.getDept());
			return "02_newReport";
		}

		if ("true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value()) && !MailUtil.checkMailServer()) {
			model.addAttribute(CommonContants.ERR, MSG_MAIL_SERVER_ERR);
			project.setDept(uvo.getDept());
			return "02_newReport";
		}

		// 自动生成项目编号
		String projectNumber = generateProjectNumberService.execute(uvo.getDept());
		project.setProjectNumber(projectNumber);

		saveProjectAndStartProcessAndApply(project, username);

		/*SendMailUtil.sendUpdateLog(username,
                populateAgentNamesMap(session).get(project.getCooperativePartner()),
                project,
                "新建");*/

		model.clear();

		return "redirect:/todoTasks";
	}

	@RequestMapping(value="/newReport", params={"addProductDetail"})
	public String addProductDetail(ModelMap model, Project project) {
		project.getProductDetailList().add(new ProductDetail());
		return "02_newReport";
	}

	@RequestMapping(value="/newReport", params={"removeProductDetail"})
	public String removeProductDetail(ModelMap model, Project project,
			@RequestParam Integer removeProductDetail) {

		project.getProductDetailList().remove(removeProductDetail.intValue());
		return "02_newReport";
	}

	@RequestMapping(value="/editReport/{businessKey}")
	public String editReport(ModelMap model,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@PathVariable String businessKey,
			HttpSession session) {

		model.addAttribute("instanceId", instanceId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("businessKey", businessKey);

		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();

		Project project = getProjectService.execute(Long.valueOf(businessKey));
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

		return "06_updateReport";
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


	@RequestMapping(value="/editReport/{businessKey}", params={"addProductDetail"})
	public String addProductDetail(ModelMap model, Project project,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@PathVariable String businessKey,
			HttpSession session) {

		project.getProductDetailList().add(new ProductDetail());

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

		return "06_updateReport";
	}

	@RequestMapping(value="/editReport/{businessKey}", params={"removeProductDetail"})
	public String removeProductDetail(ModelMap model, Project project,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@RequestParam Integer removeProductDetail,
			@PathVariable String businessKey,
			HttpSession session) {

		project.getProductDetailList().remove(removeProductDetail.intValue());

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

		return "06_updateReport";
	}

	@RequestMapping(value="/editReport/{businessKey}", params={"updateApply"})
	public String updateReport(ModelMap model, Project project,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@PathVariable String businessKey,
			HttpSession session) {

		model.addAttribute("instanceId", instanceId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("businessKey", businessKey);

		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();

		// 报备修改次数+1
		project.setUpdateCount(project.getUpdateCount().intValue() + 1);
		saveReportService.execute(project);
		project = getProjectService.execute(Long.valueOf(businessKey));

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

		/*SendMailUtil.sendUpdateLog(username,
                populateAgentNamesMap(session).get(project.getCooperativePartner()),
                project,
                "修改");*/

		UpdateLog updateLog = UpdateLogUtil.editUpdateLog(username,
				populateAgentNamesMap(session).get(project.getCooperativePartner()),
				project);

		saveUpdateLogService.execute(updateLog);

		List<UpdateLog> updateLogList = getUpdateLogListService.execute(String.valueOf(project.getId()));
		model.addAttribute("updateLogList", updateLogList);

		return "06_updateReport";
	}

	@RequestMapping(value="/editReport/{businessKey}", params={"reportApply"}, method=RequestMethod.POST)
	public String editAndApplyReport(ModelMap model, Project project,
			@RequestParam(required=false) String instanceId,
			@RequestParam(required=false) String taskId,
			@PathVariable String businessKey,
			HttpSession session) {

		if ("true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value()) && !MailUtil.checkMailServer()) {
			setModelInfos(model, project, instanceId, taskId, businessKey, session);
			return "02_newReport";
		}

		MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
		String username = uvo.getUsername();

		saveAndApplyProject(project, username, instanceId, taskId);

		model.clear();
		return "redirect:/todoTasks";
	}


	@RequestMapping(value="/editReport/{businessKey}", params={"reportAgree"}, method=RequestMethod.POST)
	public String agreeReport(ModelMap model, Project project,
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
		project.setStatus(AppContants.PROJECT_STATUS_REPORT_AGREE);
		saveReportService.execute(project);

		Integer decision = AppContants.DECISION_AGREE;

		if (StringUtil.isEmpty(taskComment)) {
			taskComment = "同意报备";
		}

		handlerTask(instanceId, taskId, taskComment, username, decision);

		model.clear();
		return "redirect:/todoTasks";
	}

	@RequestMapping(value="/editReport/{businessKey}", params={"reportDisagree"}, method=RequestMethod.POST)
	public String disAgreeReport(ModelMap model, Project project,
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
		project.setStatus(AppContants.PROJECT_STATUS_REPORT_DISAGREE);
		saveReportService.execute(project);

		Integer decision = AppContants.DECISION_DISAGREE;

		if (StringUtil.isEmpty(taskComment)) {
			taskComment = "报备驳回";
		}

		handlerTask(instanceId, taskId, taskComment, username, decision);

		model.clear();
		return "redirect:/todoTasks";
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value="/editReport/{businessKey}", params={"lostBid"}, method=RequestMethod.POST)
	public String lostBidClose(ModelMap model, Project project,
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
		project.setStatus(AppContants.PROJECT_STATUS_APPLY_LOST_BID);
		saveReportService.execute(project);

		Integer decision = AppContants.DECISION_AGREE;

		if (StringUtil.isEmpty(taskComment)) {
			taskComment = "丢标结案";
		}

		handlerTask(instanceId, taskId, taskComment, username, decision);

		Account startAccount = accountDAO.findUniqueBy("username", username);

		// 通知业务部主管
		List<Account> accountList = accountDAO.createCriteria()
				.add(Restrictions.eq("position", AppContants.POSITION_BUSINESS_MANAGER))
				.add(Restrictions.eq("dept", startAccount.getDept()))
				.list();
		for (Account a : accountList) {
			//taskService.addCandidateUser(taskId, a.getUsername());
			SendMailUtil.send("流程结束", "项目报备流程", instanceId, username, a, project);
		}


		model.clear();
		return "redirect:/todoTasks";
	}

	/**
	 * 保存项目信息、启动报备流程、办理当前任务、指派下一个任务的参与者
	 * @param project
	 * @param username
	 * @param instanceId
	 * @param taskId
	 * @return
	 */
	private Long saveAndApplyProject(Project project, String username, String instanceId, String taskId) {
		// 保存业务信息
		project.setCurrentProcess(AppContants.FLOW_PROJECT_REPORT);
		project.setStatus(AppContants.PROJECT_STATUS_REPORT_APPLY);
		project.setApplyUser(username);
		project.setApplyTime(CalendarUtil.getSystemTime());
		Long projectId = saveReportService.execute(project);

		// 启动流程并返回流程实例信息
		WorkflowInfo startProcessServiceInput = new WorkflowInfo();
		startProcessServiceInput.setProcessDefinitionKey(AppContants.FLOW_PROJECT_REPORT);
		startProcessServiceInput.setUsername(username);
		startProcessServiceInput.setBusinessKey(String.valueOf(projectId));

		startProcessService.execute(startProcessServiceInput);

		Integer decision = AppContants.DECISION_AGREE;
		String taskComment = "项目报备申请提出";

		handlerTask(instanceId, taskId, taskComment, username, decision);

		return projectId;
	}


	/**
	 * 保存项目信息、启动流程并办理报备申请任务
	 * @param project
	 * @param username
	 * @return
	 */
	private Long saveProjectAndStartProcessAndApply(Project project, String username) {
		// 保存业务信息
		project.setCurrentProcess(AppContants.FLOW_PROJECT_REPORT);
		project.setStatus(AppContants.PROJECT_STATUS_REPORT_AGREE);
		project.setApplyUser(username);
		project.setApplyTime(CalendarUtil.getSystemTime());
		Long projectId = saveReportService.execute(project);

		// 启动流程并返回流程实例信息
		WorkflowInfo workflowInfo = new WorkflowInfo();
		workflowInfo.setProcessDefinitionKey(AppContants.FLOW_PROJECT_REPORT);
		workflowInfo.setUsername(username);
		workflowInfo.setBusinessKey(String.valueOf(projectId));
		workflowInfo.putVariable(AppContants.TASK_COMMENT, "新建项目并提交报备。");
		Map<String, Object> processInstanceInfo = startProcessService.execute(workflowInfo);

		String instanceId = String.valueOf(processInstanceInfo.get("instanceId"));
		String taskId = String.valueOf(processInstanceInfo.get("taskId"));

		Integer decision = AppContants.DECISION_AGREE;
		String taskComment = "新建项目并提交报备";

		handlerTask(instanceId, taskId, taskComment, username, decision);

		return projectId;
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

	/**
	 * 完成当前任务,并根据流程定义分配下一个任务的参与者
	 *
	 * @param instanceId 流程实例ID
	 * @param taskId 当前任务ID
	 * @param taskComment 任务批注
	 * @param username 任务办理人
	 * @param decision 流向决策：同意,权限内同意,驳回
	 */
	private void handlerTask(String instanceId, String taskId,
			String taskComment, String username, Integer decision) {
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
		dispatchTaskService.execute(dispatchTaskServiceInput);
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
