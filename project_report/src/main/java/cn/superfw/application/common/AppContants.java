package cn.superfw.application.common;

import java.util.LinkedHashMap;
import java.util.Map;

public interface AppContants {

	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_USER = "ROLE_USER";

	public static final String USER_STATE_ACTIVE = "active";
	public static final String USER_STATE_LOCKED = "locked";

	public static final String ERROR_KEY = "error";

	/** 任务状态：待处理 */
	public static final String TASK_STATUS_TODO = "Todo";
	/** 任务状态：已完成 */
	public static final String TASK_STATUS_COMPLETED = "Completed";

	/** 流程状态：进行中 */
	public static final String FLOW_STATUS_PROCESSING = "Processing";
	/** 流程状态：已关闭 */
	public static final String FLOW_STATUS_CLOSED = "Closed";

	/** 项目状态：报备新建*/
	public static final String PROJECT_STATUS_REPORT_NEW = "A1";

	/** 项目状态：报备申请 */
	public static final String PROJECT_STATUS_REPORT_APPLY = "B1";
	/** 项目状态：报备同意 */
	public static final String PROJECT_STATUS_REPORT_AGREE = "B2";
	/** 项目状态：报备驳回 */
	public static final String PROJECT_STATUS_REPORT_DISAGREE = "B3";

	/** 项目状态：交期确认 */
	public static final String PROJECT_STATUS_DELIVERY_DATE_CONFIRM = "C1";
	/** 项目状态：交期同意 */
	public static final String PROJECT_STATUS_DELIVERY_DATE_AGREE = "C2";
	/** 项目状态：交期不同意 */
	public static final String PROJECT_STATUS_DELIVERY_DATE_DISAGREE = "C3";

	/** 项目状态：特价申请 */
	public static final String PROJECT_STATUS_SPECIAL_APPLY = "D1";
	/** 项目状态：特价业务部主管同意 */
	public static final String PROJECT_STATUS_SPECIAL_BIZM_AGREE = "D2";
	/** 项目状态：特价MDM主管同意 */
	public static final String PROJECT_STATUS_SPECIAL_MDM_AGREE = "D3";
	/** 项目状态：特价业务部主管驳回 */
	public static final String PROJECT_STATUS_SPECIAL_BIZM_DISAGREE = "D4";
	/** 项目状态：特价MDM主管驳回 */
	public static final String PROJECT_STATUS_SPECIAL_MDM_DISAGREE = "D5";
	/** 项目状态：特价总经理驳回 */
	public static final String PROJECT_STATUS_SPECIAL_GM_DISAGREE = "D6";

	/** 项目状态：执行订单 */
	public static final String PROJECT_STATUS_ORDER_EXECUTE = "E1";
	/** 项目状态：全部出货结案*/
	public static final String PROJECT_STATUS_ORDER_COMPLETED = "E2";
	/** 项目状态：取消申请，结案*/
	public static final String PROJECT_STATUS_APPLY_CANCEL = "E3";
	/** 项目状态：丢标结案*/
	public static final String PROJECT_STATUS_APPLY_LOST_BID = "E4";
	/** 项目状态：未出货结案*/
	public static final String PROJECT_STATUS_APPLY_NOT_SHIPPED = "E5";
	/** 项目状态：部分出货结案*/
	public static final String PROJECT_STATUS_APPLY_PART_SHIPPED = "E6";

	public static final Map<String, String> PROJECT_STATUS_NAMES = new LinkedHashMap<String, String>() {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		{
			put(PROJECT_STATUS_REPORT_NEW, "未报备");
			put(PROJECT_STATUS_REPORT_APPLY, "报备申请");
			put(PROJECT_STATUS_REPORT_AGREE, "已报备");
			put(PROJECT_STATUS_REPORT_DISAGREE, "报备驳回");
			put(PROJECT_STATUS_DELIVERY_DATE_CONFIRM, "交期确认");
			put(PROJECT_STATUS_DELIVERY_DATE_AGREE, "交期同意,特价申请");
			put(PROJECT_STATUS_DELIVERY_DATE_DISAGREE, "交期驳回");
			put(PROJECT_STATUS_SPECIAL_APPLY, "询问交期");
			put(PROJECT_STATUS_SPECIAL_BIZM_AGREE, "业务部主管同意");
			put(PROJECT_STATUS_SPECIAL_MDM_AGREE, "MDM主管同意");
			put(PROJECT_STATUS_SPECIAL_BIZM_DISAGREE, "业务部主管驳回");
			put(PROJECT_STATUS_SPECIAL_MDM_DISAGREE, "MDM主管驳回");
			put(PROJECT_STATUS_SPECIAL_GM_DISAGREE, "总经理驳回");
			put(PROJECT_STATUS_ORDER_EXECUTE, "执行订单");
			put(PROJECT_STATUS_ORDER_COMPLETED, "全部出货结案");
			put(PROJECT_STATUS_APPLY_CANCEL, "结案（取消申请）");
			put(PROJECT_STATUS_APPLY_LOST_BID, "丢标结案");
			put(PROJECT_STATUS_APPLY_NOT_SHIPPED, "未出货结案");
			put(PROJECT_STATUS_APPLY_PART_SHIPPED, "部分出货结案");
		}
	};

	/** 超级管理员用户名 */
	public static final String USERNAME_ADMIN = "admin";

	public static final String FLOW_DECISION_KEY = "decision";

	public static final String TASK_COMMENT = "comment";

	/** 决定：同意 */
	public static final int DECISION_AGREE = 1;
	/** 决定：报价大于或等于价格A */
	public static final int DECISION_AUTH_AGREE = 2;
	/** 决定：不同意 */
	public static final int DECISION_DISAGREE = 3;


	/** 流程Key：项目报备 */
	public static final String FLOW_PROJECT_REPORT = "project_report_flow";
	/** 流程Key：项目报备 */
	public static final String FLOW_NAME_PROJECT_REPORT = "项目报备流程";


	/** 流程Name：特价备货 */
	public static final String FLOW_SPECIAL_STOCK = "special_stock_flow";
	/** 流程Name：特价备货  */
	public static final String FLOW_NAME_SPECIAL_STOCK = "特价备货流程";



	/** 用户任务：项目报备申请 */
	public static final String TASK_PROJECT_REPORT_APPLY = "ProjectReportApply";
	/** 用户任务：丢标结案 */
	public static final String TASK_PROJECT_LOST_BID_CLOSE = "LostBidClose";

	/** 用户任务：特价备货申请 */
	public static final String TASK_SPECIAL_STOCK_APPLY = "SpecialStockApply";
	/** 用户任务：最快交期确认 */
	public static final String TASK_FASTEST_DELIVERY_DATE_CONFIRM = "FastestDeliveryDateConfirm";
	/** 用户任务：最终交期确认 */
	public static final String TASK_FINAL_DELIVERY_DATE_CONFIRM = "FinalDeliveryDateConfirm";
	/** 用户任务：业务部主管审批 */
	public static final String TASK_BUSINESS_DEPT_MANAGER_APPROVE = "BusinessDeptManagerApprove";
	/** 用户任务：MDM主管审批 */
	public static final String TASK_MDM_MANAGER_APPROVE = "MDMManagerApprove";
	/** 用户任务：总经理审批 */
	public static final String TASK_GENERAL_MANAGER_APPROVE = "GeneralManagerApprove";
	/** 用户任务：订单执行 */
	public static final String TASK_EXECUTE_ORDER = "ExecuteOrder";

	/** 职位：销售 */
	public static final String POSITION_SALES = "1";
	/** 职位：运作部库管 */
	public static final String POSITION_OPERATION_MANAGER = "2";
	/** 职位：运作部商务 */
	public static final String POSITION_OPERATION_BIZ = "21";
	/** 职位：运作部备货 */
	public static final String POSITION_OPERATION_STOCK = "22";
	/** 职位：业务部主管 */
	public static final String POSITION_BUSINESS_MANAGER = "3";
	/** 职位：MDM主管 */
	public static final String POSITION_MDM_MANAGER = "4";
	/** 职位：总经理 */
	public static final String POSITION_GENERAL_MANAGER = "5";
	/** 职位：业务部助理 */
	public static final String POSITION_BUSINESS_ASSISTANT = "6";

	public static final Map<String, String> POSITIONS_NAMES = new LinkedHashMap<String, String>() {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		{
			put(AppContants.POSITION_SALES, "销售");
			put(AppContants.POSITION_OPERATION_MANAGER, "运作部主管");
			put(AppContants.POSITION_OPERATION_BIZ, "运作部商务");
			put(AppContants.POSITION_OPERATION_STOCK, "运作部备货");
			put(AppContants.POSITION_BUSINESS_MANAGER, "业务部主管");
			put(AppContants.POSITION_MDM_MANAGER, "MDM主管");
			put(AppContants.POSITION_GENERAL_MANAGER, "总经理");
			put(AppContants.POSITION_BUSINESS_ASSISTANT, "业务部助理");
		}
	};

	/** 业务部门 */
	public static final String DEPT_GENERAL_MANAGER = "1";
	public static final String DEPT_MDM = "2";
	public static final String DEPT_BIZ_1 = "3";
	public static final String DEPT_BIZ_2 = "4";
	public static final String DEPT_OPT = "5";

	public static final Map<String, String> DEPT_NAMES = new LinkedHashMap<String, String>() {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		{
			put(DEPT_GENERAL_MANAGER, "总经理办公室");
			put(DEPT_MDM, "MDM");
			put(DEPT_BIZ_1, "渠道开发部");
			put(DEPT_BIZ_2, "业务部");
			put(DEPT_OPT, "运作部");
		}
	};

	public static final Map<String, String> DEPT_BIZ_NAMES = new LinkedHashMap<String, String>() {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		{
			put(DEPT_BIZ_1, "渠道开发部");
			put(DEPT_BIZ_2, "业务部");
		}
	};

}
