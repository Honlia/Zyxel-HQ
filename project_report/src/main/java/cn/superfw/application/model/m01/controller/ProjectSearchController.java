package cn.superfw.application.model.m01.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.superfw.application.common.AppContants;
import cn.superfw.application.domain.Agent;
import cn.superfw.application.model.m01.dto.ProjectSearchCommand;
import cn.superfw.application.model.m01.dto.ProjectSearchResult;
import cn.superfw.application.web.uvo.MyUVO;
import cn.superfw.framework.CommonContants;
import cn.superfw.framework.orm.Page;
import cn.superfw.framework.utils.CalendarUtil;
import cn.superfw.framework.web.BLogicService;
import cn.superfw.framework.web.BaseController;

@Controller
public class ProjectSearchController extends BaseController {

    @Autowired
    private BLogicService<Void, List<String>> getProductLineService;

    @Autowired
    private BLogicService<String, List<String>> getProductTypeService;

    @Autowired
    private BLogicService<Map<String, String>, List<String>> getProductModelService;

	@Autowired
	private BLogicService<ProjectSearchCommand, Page<ProjectSearchResult>> searchProjectPageService;

	@Autowired
	BLogicService<ProjectSearchCommand, Workbook> exportProjectExcelService;

    @Autowired
    private BLogicService<Agent, List<Agent>> getAgentListService;

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

    @ModelAttribute("projectStatusNames")
    public Map<String, String> populateProjectStatusNames() {
    	return AppContants.PROJECT_STATUS_NAMES;
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

	@RequestMapping(value="/searchReport")
	public String searchReportInit(ModelMap model,
	        @ModelAttribute("projectSearchCommand") ProjectSearchCommand projectSearchCommand,
	        HttpSession session) {
	    MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
	    if (AppContants.POSITION_SALES.equals(uvo.getPosition())) {

	        projectSearchCommand.setSalesName(uvo.getUsername());
	        projectSearchCommand.setDept(uvo.getDept());

	    } else if (AppContants.POSITION_BUSINESS_MANAGER.equals(uvo.getPosition())
	            || AppContants.POSITION_BUSINESS_ASSISTANT.equals(uvo.getPosition())) {

	        projectSearchCommand.setDept(uvo.getDept());

	    } else if (AppContants.POSITION_MDM_MANAGER.equals(uvo.getPosition())) {
	        projectSearchCommand.setDept("");
	    }
		return "03_viewReport";
	}

	@RequestMapping(value="/searchReport", params={"search"})
	public String searchReport(ModelMap model,
	        @ModelAttribute("projectSearchCommand") ProjectSearchCommand projectSearchCommand,
	        @RequestParam(required=false) Integer pageNo,
	        HttpSession session) {

	    // 查询的时候
	    if (pageNo == null) {
	        session.setAttribute("projectSearchCommand", projectSearchCommand);
	        pageNo = 1;
	    // 翻页的时候
	    } else {
	        projectSearchCommand = (ProjectSearchCommand)session.getAttribute("projectSearchCommand");
	        model.addAttribute("projectSearchCommand", projectSearchCommand);
	    }

	    model.addAttribute("curPageNo", pageNo);
	    projectSearchCommand.setPageNo(pageNo);
		Page<ProjectSearchResult> page = searchProjectPageService.execute(projectSearchCommand);
		model.addAttribute("page", page);
		return "03_viewReport";
	}

	@RequestMapping(value="/searchReport", params={"export"})
	public void exportReport(ModelMap model,
	        @ModelAttribute("projectSearchCommand") ProjectSearchCommand projectSearchCommand,
	        HttpServletResponse response) throws IOException {

	    OutputStream os = null;

        os = response.getOutputStream();
        response.reset();

        String fileName = "项目报备信息" + CalendarUtil.formatDateTime(CalendarUtil.getSystemTime()) + ".xls";
        response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes(), "iso-8859-1"));
        response.setContentType("application/octet-stream; charset=GB2312");

        Workbook wb = exportProjectExcelService.execute(projectSearchCommand);

        wb.write(os);
        os.flush();
	}
}
