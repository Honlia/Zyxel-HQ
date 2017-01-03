package cn.superfw.application.model.m99.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superfw.application.domain.Agent;
import cn.superfw.application.web.uvo.MyUVO;
import cn.superfw.framework.CommonContants;
import cn.superfw.framework.web.BLogicService;
import cn.superfw.framework.web.BaseController;

/**
 * 代理商管理控制器
 * 
 * @author zhouli
 *
 */
@Controller
public class ManageAgentController extends BaseController {

    @Autowired
    private BLogicService<Agent, Long> saveAgentService;

    @Autowired
    private BLogicService<Long, Void> deleteAgentService;

    @Autowired
    private BLogicService<Agent, List<Agent>> getAgentListService;

    @Autowired
    private BLogicService<String, List<String>> getBusinessDirectorService;

    @ModelAttribute("businessDirectorList")
    public List<String> populateBusinessDirectorList() {
        List<String> businessDirectorList = getBusinessDirectorService.execute(null);
        return businessDirectorList;
    }

    @RequestMapping(value="/changeDept", method=RequestMethod.POST)
    @ResponseBody
    public List<String> changeDept(@RequestBody Map<String, String> map) {

        List<String> businessDirectorList = getBusinessDirectorService.execute(map.get("dept"));

        return businessDirectorList;
    }

    @RequestMapping(value="/m99/initManageAgent")
    public String initManageAgent(ModelMap model, Agent agent) {

        model.addAttribute("agent", agent);

        model.addAttribute("agentList", getAgentListService.execute(agent));
        return "10_manageAgent";
    }

    @RequestMapping(value="/m99/initCreatAgent")
    public String initCreatAgent(ModelMap model) {

        model.addAttribute("agent", new Agent());
        return "11_creatAgent";
    }

    @RequestMapping(value="/m99/creatAgent")
    public String creatAgent(ModelMap model, HttpSession session, Agent agent) {

        MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
        String username = uvo.getUsername();
        agent.setAddUser(username);
        agent.setUpdUser(username);

        saveAgentService.execute(agent);

        model.addAttribute("agent", new Agent());
        model.addAttribute("agentList", getAgentListService.execute(new Agent()));

        return "10_manageAgent";
    }

    @RequestMapping(value="/m99/initModifyAgent")
    public String initModifyAgent(ModelMap model, @RequestParam(required=true) String id) {

        Agent agent = new Agent();
        agent.setId(Long.valueOf(id));
        
        model.addAttribute("agent", getAgentListService.execute(agent).get(0));
        
        return "12_modifyAgent";
    }

    @RequestMapping(value="/m99/modifyAgent")
    public String modifyAgent(ModelMap model, HttpSession session, Agent agent) {

        MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
        String username = uvo.getUsername();
        agent.setUpdUser(username);

        saveAgentService.execute(agent);

        model.addAttribute("agent", new Agent());
        model.addAttribute("agentList", getAgentListService.execute(new Agent()));
        return "10_manageAgent";
    }

    @RequestMapping(value="/m99/deleteAgent")
    public String deleteAgent(ModelMap model, @RequestParam(required=true) String id) {

        deleteAgentService.execute(Long.valueOf(id));

        model.addAttribute("agent", new Agent());
        model.addAttribute("agentList", getAgentListService.execute(new Agent()));
        return "10_manageAgent";
    }

}
