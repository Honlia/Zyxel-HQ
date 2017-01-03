package cn.superfw.application.model.m01.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.common.AppContants;
import cn.superfw.application.dao.ProjectDAO;
import cn.superfw.application.domain.Agent;
import cn.superfw.application.domain.ProductDetail;
import cn.superfw.application.domain.Project;
import cn.superfw.application.model.m01.dto.ProjectSearchCommand;
import cn.superfw.framework.utils.CalendarUtil;
import cn.superfw.framework.utils.StringUtil;
import cn.superfw.framework.web.BLogicService;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class ExportProjectExcelService extends BaseBLogicService<ProjectSearchCommand, Workbook> {

	private static final String SPACE = "";

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private BLogicService<Agent, List<Agent>> getAgentListService;

	@Override
	protected Workbook doService(ProjectSearchCommand projectSearchCommand) {

		Map<String, String> agentNames = populateAgentNamesMap();

		String[] header = {"No.","项目编号","项目名称","合作伙伴","项目地点","预计出货时间","项目描述","所属部门","申请人","申请时间","状态",
				"产品线","产品型号","数量","预计成交价","预计成交金额"/*,"需求日期","最快交期","公开报价","平台权限价","业务部主管价","MDM主管价","总经理价"*/};

		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("项目报备信息");

		// 标题行样式
		CellStyle headFillBackgroundColorStyle = wb.createCellStyle();
		headFillBackgroundColorStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		headFillBackgroundColorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// 项目信息样式
		CellStyle projectFillBackgroundColorStyle = wb.createCellStyle();
		projectFillBackgroundColorStyle.setFillForegroundColor(HSSFColor.LEMON_CHIFFON.index);
		projectFillBackgroundColorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// 产品信息样式
		CellStyle productDetailFillBackgroundColorStyle = wb.createCellStyle();
		productDetailFillBackgroundColorStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		productDetailFillBackgroundColorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// 标题行
		Row headerRow = sheet.createRow((short)0);
		int headerLength = header.length;
		for (int i = 0; i < headerLength; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(header[i]);
			cell.setCellStyle(headFillBackgroundColorStyle);
		}

		List<Project> projectList = getProjectList(projectSearchCommand);
		if (projectList == null || projectList.isEmpty()) {
			return wb;
		}

		short rowNum = 0;
		Row row = null;

		for(Project project : projectList) {
			List<ProductDetail> productDetails = project.getProductDetailList();
			if (productDetails == null || productDetails.isEmpty()) {

				rowNum += 1;
				row = sheet.createRow(rowNum);

				//No.
				row.createCell(0).setCellValue(rowNum);
				//项目编号
				row.createCell(1).setCellValue(project.getProjectNumber());
				//项目名称
				row.createCell(2).setCellValue(project.getProjectName());
				//合作伙伴
				row.createCell(3).setCellValue(agentNames.get(project.getCooperativePartner()));
				//项目地点
				row.createCell(4).setCellValue(project.getProjectPlace());
				//预计出货时间
				row.createCell(5).setCellValue(CalendarUtil.formatDate(project.getExpectedShipmentTime()));
				//项目描述
				row.createCell(6).setCellValue(project.getProjectDescription());
				//所属部门
				row.createCell(7).setCellValue(AppContants.DEPT_NAMES.get(project.getDept()));
				//申请人
				row.createCell(8).setCellValue(project.getApplyUser());
				//申请时间
				row.createCell(9).setCellValue(CalendarUtil.formatDate(project.getApplyTime()));
				//状态
				row.createCell(10).setCellValue(AppContants.PROJECT_STATUS_NAMES.get(project.getStatus()));
				//产品线
				row.createCell(11).setCellValue(SPACE);
				//产品型号
				row.createCell(12).setCellValue(SPACE);
				//数量
				row.createCell(13).setCellValue(SPACE);
				//预计成交价
				row.createCell(14).setCellValue(SPACE);
				//预计成交金额
				row.createCell(15).setCellValue(SPACE);
				/*
                //需求日期
                row.createCell(16).setCellValue(SPACE);
                //最快交期
                row.createCell(17).setCellValue(SPACE);
                //公开报价
                row.createCell(18).setCellValue(SPACE);
                //平台权限价
                row.createCell(19).setCellValue(SPACE);
                //业务部主管价
                row.createCell(20).setCellValue(SPACE);
                //MDM主管价
                row.createCell(21).setCellValue(SPACE);
                //总经理价
                row.createCell(22).setCellValue(SPACE);*/

				setCellStyle(sheet, projectFillBackgroundColorStyle, productDetailFillBackgroundColorStyle,
						headerLength, row);

				continue;
			}

			for (ProductDetail productDetail : productDetails) {

				rowNum += 1;
				row = sheet.createRow(rowNum);

				//No.
				row.createCell(0).setCellValue(rowNum);
				//项目编号
				row.createCell(1).setCellValue(project.getProjectNumber());
				//项目名称
				row.createCell(2).setCellValue(project.getProjectName());
				//合作伙伴
				row.createCell(3).setCellValue(agentNames.get(project.getCooperativePartner()));
				//项目地点
				row.createCell(4).setCellValue(project.getProjectPlace());
				//预计出货时间
				row.createCell(5).setCellValue(CalendarUtil.formatDate(project.getExpectedShipmentTime()));
				//项目描述
				row.createCell(6).setCellValue(project.getProjectDescription());
				//所属部门
				row.createCell(7).setCellValue(AppContants.DEPT_NAMES.get(project.getDept()));
				//申请人
				row.createCell(8).setCellValue(project.getApplyUser());
				//申请时间
				row.createCell(9).setCellValue(CalendarUtil.formatDate(project.getApplyTime()));
				//状态
				row.createCell(10).setCellValue(AppContants.PROJECT_STATUS_NAMES.get(project.getStatus()));
				//产品线
				row.createCell(11).setCellValue(productDetail.getProductLine());
				//产品型号
				row.createCell(12).setCellValue(productDetail.getProductModel());
				//数量
				row.createCell(13).setCellValue(productDetail.getNumber() == null ? 0 : productDetail.getNumber());
				//预计成交价
				row.createCell(14).setCellValue(productDetail.getExpectedPrice() == null ? 0 : productDetail.getExpectedPrice());
				//预计成交金额
				row.createCell(15).setCellValue(productDetail.getExpectedTotalPrice() == null ? 0 : productDetail.getExpectedTotalPrice());
				/*
                //需求日期
                row.createCell(16).setCellValue(CalendarUtil.formatDate(productDetail.getRequirementDate()));
                //最快交期
                row.createCell(17).setCellValue(CalendarUtil.formatDate(productDetail.getFastestDeliveryDate()));
                //公开报价
                row.createCell(18).setCellValue(productDetail.getOpenPrice());
                //平台权限价
                row.createCell(19).setCellValue(productDetail.getPlatformPrice());
                //业务部主管价
                row.createCell(20).setCellValue(productDetail.getBusinessDeptManagerPrice());
                //MDM主管价
                row.createCell(21).setCellValue(productDetail.getMdmManagerPrice());
                //总经理价
                row.createCell(22).setCellValue(productDetail.getGeneralManagerPrice());*/

				setCellStyle(sheet, projectFillBackgroundColorStyle, productDetailFillBackgroundColorStyle,
						headerLength, row);
			}
		}
		return wb;
	}

	private void setCellStyle(Sheet sheet, CellStyle projectFillBackgroundColorStyle,
			CellStyle productDetailFillBackgroundColorStyle, int headerLength, Row row) {
		for (int n = 0; n < headerLength; n++) {
			if (n <= 10) {
				row.getCell(n).setCellStyle(projectFillBackgroundColorStyle);
			} else {
				row.getCell(n).setCellStyle(productDetailFillBackgroundColorStyle);
			}
			sheet.autoSizeColumn(n);
		}
	}

	@SuppressWarnings("unchecked")
	private List<Project> getProjectList(ProjectSearchCommand projectSearchCommand) {
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
		if (StringUtil.isNotEmpty(projectSearchCommand.getStatus())) {
			String[] statusArrsy = StringUtils.split(projectSearchCommand.getStatus(), ",");
			criterionList1.add(Restrictions.in("status", statusArrsy));
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

		Criteria c = projectDAO.createCriteria(criterionList1.toArray(new Criterion[]{}));

		c.addOrder(Order.desc("applyTime"));
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return c.list();
	}

	public Map<String, String> populateAgentNamesMap() {
		List<Agent> agentList = getAgentListService.execute(new Agent());
		Map<String, String> agentNamesMap = new LinkedHashMap<>();
		for (Agent agent : agentList) {
			agentNamesMap.put(String.valueOf(agent.getId()), agent.getAgentName());
		}
		return agentNamesMap;
	}

}
