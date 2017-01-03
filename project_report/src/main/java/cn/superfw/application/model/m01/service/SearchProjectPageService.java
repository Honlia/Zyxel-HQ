package cn.superfw.application.model.m01.service;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.ProjectDAO;
import cn.superfw.application.domain.Project;
import cn.superfw.application.model.m01.dto.ProjectSearchCommand;
import cn.superfw.application.model.m01.dto.ProjectSearchResult;
import cn.superfw.application.workflow.dto.WorkflowInfo;
import cn.superfw.framework.orm.Page;
import cn.superfw.framework.utils.StringUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class SearchProjectPageService extends BaseBLogicService<ProjectSearchCommand, Page<ProjectSearchResult>> {

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	@SuppressWarnings("unchecked")
	@Override
	protected Page<ProjectSearchResult> doService(ProjectSearchCommand projectSearchCommand) {

		// Project检索条件
		List<Criterion> criterionList1 = new ArrayList<>();

		if (StringUtil.isNotEmpty(projectSearchCommand.getDept())) {
			criterionList1.add(Restrictions.eq("dept", projectSearchCommand.getDept()));
		}
		if (StringUtil.isNotEmpty(projectSearchCommand.getProjectNumber())) {
			criterionList1.add(Restrictions.like("projectNumber", projectSearchCommand.getProjectNumber(), MatchMode.END));
		}
		if (StringUtil.isNotEmpty(projectSearchCommand.getProjectName())) {
			criterionList1.add(Restrictions.like("projectName", projectSearchCommand.getProjectName(), MatchMode.ANYWHERE));
		}
		if (StringUtil.isNotEmpty(projectSearchCommand.getSalesName())) {
			criterionList1.add(Restrictions.like("applyUser", projectSearchCommand.getSalesName(), MatchMode.START));
		}
		if (StringUtil.isNotEmpty(projectSearchCommand.getCooperativePartner())) {
			criterionList1.add(Restrictions.eq("cooperativePartner", projectSearchCommand.getCooperativePartner()));
		}
		if (projectSearchCommand.getExpectedShipmentTimeFrom() != null) {
			criterionList1.add(Restrictions.ge("expectedShipmentTime", projectSearchCommand.getExpectedShipmentTimeFrom()));
		}
		if (StringUtil.isNotEmpty(projectSearchCommand.getStatus())) {
			String[] statusArrsy = StringUtils.split(projectSearchCommand.getStatus(), ",");
			criterionList1.add(Restrictions.in("status", statusArrsy));
		}
		if (projectSearchCommand.getExpectedShipmentTimeTo() != null) {
			criterionList1.add(Restrictions.le("expectedShipmentTime", projectSearchCommand.getExpectedShipmentTimeTo()));
		}
		if (projectSearchCommand.getExpectedShipmentTimeFrom() != null
				&& projectSearchCommand.getExpectedShipmentTimeTo() != null) {
			criterionList1.add(Restrictions.between("expectedShipmentTime",
					projectSearchCommand.getExpectedShipmentTimeFrom(),
					projectSearchCommand.getExpectedShipmentTimeTo()));
		}
		if (projectSearchCommand.getApplyTimeFrom() != null) {
			criterionList1.add(Restrictions.ge("applyTime", projectSearchCommand.getApplyTimeFrom()));
		}
		if (projectSearchCommand.getApplyTimeTo() != null) {
			criterionList1.add(Restrictions.le("applyTime", projectSearchCommand.getApplyTimeTo()));
		}
		if (projectSearchCommand.getApplyTimeFrom() != null
				&& projectSearchCommand.getApplyTimeTo() != null) {
			criterionList1.add(Restrictions.between("applyTime",
					projectSearchCommand.getApplyTimeFrom(),
					projectSearchCommand.getApplyTimeTo()));
		}
		if (StringUtil.isNotEmpty(projectSearchCommand.getProductLine())
				|| StringUtil.isNotEmpty(projectSearchCommand.getProductType())
				|| StringUtil.isNotEmpty(projectSearchCommand.getProductModel())) {
			StringBuilder sql = new StringBuilder();
			List<String> paramList = new ArrayList<String>();
			List<Type> typeList = new ArrayList<Type>();
			sql.append("{alias}.id IN (SELECT t2.project_id FROM t_product_detail t2 WHERE {alias}.id=t2.project_id ");
			if (StringUtil.isNotEmpty(projectSearchCommand.getProductLine())) {
				sql.append("and t2.product_line=(?) ");
				paramList.add(projectSearchCommand.getProductLine());
				typeList.add(StandardBasicTypes.STRING);
			}
			if (StringUtil.isNotEmpty(projectSearchCommand.getProductType())) {
				sql.append("and t2.product_type=(?) ");
				paramList.add(projectSearchCommand.getProductType());
				typeList.add(StandardBasicTypes.STRING);
			}
			if (StringUtil.isNotEmpty(projectSearchCommand.getProductModel())) {
				sql.append("and t2.product_model=(?) ");
				paramList.add(projectSearchCommand.getProductModel());
				typeList.add(StandardBasicTypes.STRING);
			}
			sql.append(")");
			criterionList1.add(Restrictions.sqlRestriction(sql.toString(),
					paramList.toArray(new String[]{}), typeList.toArray(new Type[]{})));
		}

		// 总件数取得
		Criteria countQuery = projectDAO.createCriteria(criterionList1.toArray(new Criterion[]{}));
		countQuery.setProjection(Projections.countDistinct("projectNumber"));
		Long totalCount = (Long)countQuery.uniqueResult();

		// 分页控制信息设定
		Page<ProjectSearchResult> resultPage = new Page<ProjectSearchResult>(projectSearchCommand.getPageSize());
		resultPage.setPageNo(projectSearchCommand.getPageNo());
		resultPage.setTotalCount(totalCount);

		// 结果取得
		Criteria resultQuery = projectDAO.createCriteria(criterionList1.toArray(new Criterion[]{}));
		resultQuery.addOrder(Order.desc("applyTime"));
		resultQuery.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if(resultPage.getPageSize() > 0) {
			//hibernate的firstResult的序号从0开始
			resultQuery.setFirstResult(resultPage.getFirst() - 1);
			resultQuery.setMaxResults(resultPage.getPageSize());
		}

		List<Project> projectList = resultQuery.list();

		// 页面显示内容设定
		for (Project project : projectList) {

			// 如果项目状态是已报备,历史流程实例

			WorkflowInfo workflowInfo = new WorkflowInfo();
			// 活动的任务
			Task task = taskService.createTaskQuery()
					.processInstanceBusinessKey(String.valueOf(project.getId()))
					.active()
					.singleResult();


			// 如果是正在执行的任务,则取当前任务
			if (task != null) {
				workflowInfo.setInstanceId(task.getProcessInstanceId());
				workflowInfo.setTaskId(task.getId());
			} else {
				// 如果当前任务不存在,则取最后一个已结束的任务
				List<HistoricTaskInstance> hisTaskList = historyService.createHistoricTaskInstanceQuery()
						.processInstanceBusinessKey(String.valueOf(project.getId()))
						.orderByHistoricTaskInstanceEndTime().desc()
						.finished()
						.list();
				if (hisTaskList != null && !hisTaskList.isEmpty()) {
					HistoricTaskInstance hisTask = hisTaskList.get(0);
					workflowInfo.setInstanceId(hisTask.getProcessInstanceId());
					workflowInfo.setTaskId(hisTask.getId());
				}
			}

			resultPage.addResultItem(new ProjectSearchResult(workflowInfo, project));
		}

		return resultPage;
	}

}
