package cn.superfw.application.model.common.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.superfw.application.common.AppContants;
import cn.superfw.application.domain.Project;
import cn.superfw.application.web.uvo.MyUVO;
import cn.superfw.application.workflow.dto.WorkflowInfo;
import cn.superfw.framework.CommonContants;
import cn.superfw.framework.utils.CalendarUtil;
import cn.superfw.framework.web.BLogicService;
import cn.superfw.framework.web.BaseController;

@Controller
public class HomePageController extends BaseController {

    @Autowired
    private BLogicService<WorkflowInfo, List<Map<String, Object>>> listTaskService;

    @Autowired
    private BLogicService<Long, Project> getProjectService;

    @ModelAttribute("statusNames")
    public Map<String, String> populateStatusNames() {
        Map<String, String> statusNames = AppContants.PROJECT_STATUS_NAMES;
        return statusNames;
    }

    @RequestMapping(value="/todoTasks")
    public String todoTasks(ModelMap model, HttpSession session) {

        MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);

        WorkflowInfo workflowInfo = new WorkflowInfo();
        workflowInfo.setTaskStatus(AppContants.TASK_STATUS_TODO);
        workflowInfo.setUsername(uvo.getUsername());

        List<Map<String, Object>> todoTasks = listTaskService.execute(workflowInfo);

        Project project = null;
        for (Map<String, Object> todoTask : todoTasks) {
            Long projectId = NumberUtils.toLong((String.valueOf(todoTask.get("businessKey"))));
            project = getProjectService.execute(projectId);
            todoTask.put("projectNumber", project.getProjectNumber());
            todoTask.put("projectName", project.getProjectName());
            todoTask.put("applyUser", project.getApplyUser());
            todoTask.put("applyTime", CalendarUtil.formatDateTime(project.getApplyTime()));
            todoTask.put("status", project.getStatus());
        }

        model.addAttribute("todoTasks", todoTasks);

        return "05_task";
    }

}
