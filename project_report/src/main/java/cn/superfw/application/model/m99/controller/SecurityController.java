package cn.superfw.application.model.m99.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.superfw.application.common.AppContants;
import cn.superfw.application.domain.Account;
import cn.superfw.application.domain.Resource;
import cn.superfw.application.domain.Role;
import cn.superfw.application.model.m99.dto.AccountViewDTO;
import cn.superfw.application.model.m99.dto.ResourceViewDTO;
import cn.superfw.application.springsecutity.SecurityResourceManager;
import cn.superfw.framework.utils.StringUtil;
import cn.superfw.framework.web.BLogicService;
import cn.superfw.framework.web.BaseController;

@Controller
public class SecurityController extends BaseController {

	/** 角色管理服务 */
    @Autowired
    private BLogicService<Void, List<Role>> listRoleService;
    @Autowired
    private BLogicService<Role, Void> saveRoleService;
    @Autowired
    private BLogicService<Long, Void> deleteRoleService;
    @Autowired
    private BLogicService<Long, Role> getRoleService;

	/** 资源管理服务 */
    @Autowired
    private BLogicService<Void, List<Resource>> listResourceService;
    @Autowired
    private BLogicService<Resource, Void> saveResourceService;
    @Autowired
    private BLogicService<Long, Void> deleteResourceService;

	/** 账户管理服务 */
    @Autowired
    private BLogicService<Void, List<Account>> listAccountService;
    @Autowired
    private BLogicService<Account, Void> saveAccountService;
    @Autowired
    private BLogicService<Long, Void> deleteAccountService;

    @Autowired
    private SecurityResourceManager securityResourceManager;

    /** 角色管理服务 */
    @RequestMapping(value="/m99/SC990101", method=RequestMethod.GET)
    public String displayRole(ModelMap model) {

        List<Role> roleList = listRoleService.execute(null);

        model.addAttribute("roleList", roleList);
        model.addAttribute("role", new Role());

        return "SC990101";
    }

    @RequestMapping(value="/m99/saveRole", method=RequestMethod.POST)
    public String saveRole(@ModelAttribute Role role) {
        saveRoleService.execute(role);
        return "redirect:/m99/SC990101";
    }

    @RequestMapping(value="/m99/deleteRole/{id}", method=RequestMethod.GET)
    public String deleteRole(@PathVariable Long id) {
        deleteRoleService.execute(id);
        return "redirect:/m99/SC990101";
    }




    /** 资源管理服务 */
    @RequestMapping(value="/m99/SC990102", method=RequestMethod.GET)
    public String displayResource(ModelMap model) {

        List<Resource> resourceList = listResourceService.execute(null);
        List<Role> roleList = listRoleService.execute(null);

        model.addAttribute("resourceList", resourceList);
        model.addAttribute("roleList", roleList);
        model.addAttribute("resourceViewDTO", new ResourceViewDTO());

        return "SC990102";
    }

    @RequestMapping(value="/m99/saveResource", method=RequestMethod.POST)
    public String saveResource(@ModelAttribute ResourceViewDTO resourceViewDTO) {

    	Resource resource = resourceViewDTO.getResource();

    	String rolesStr = resourceViewDTO.getRoles();
        String[] roleIds = StringUtil.split(rolesStr, ",");

        Role role = null;
        for (String roleId : roleIds) {
        	if (StringUtil.isNotEmpty(roleId)) {
	        	role = getRoleService.execute(Long.parseLong(roleId));
	        	resource.addRole(role);
        	}
        }
        saveResourceService.execute(resource);

        securityResourceManager.refreshSecurityResource();

        return "redirect:/m99/SC990102";
    }

    @RequestMapping(value="/m99/deleteResource/{id}", method=RequestMethod.GET)
    public String deleteResource(@PathVariable Long id) {
        deleteResourceService.execute(id);

        securityResourceManager.refreshSecurityResource();

        return "redirect:/m99/SC990102";
    }



    /** 账户管理服务 */
    @RequestMapping(value="/m99/SC990103", method=RequestMethod.GET)
    public String displayAccount(ModelMap model) {

        List<Account> accountList = listAccountService.execute(null);
        List<Role> roleList = listRoleService.execute(null);

        model.addAttribute("accountList", accountList);
        model.addAttribute("roleList", roleList);
        model.addAttribute("positions", AppContants.POSITIONS_NAMES);
        model.addAttribute("depts", AppContants.DEPT_NAMES);
        model.addAttribute("accountViewDTO", new AccountViewDTO());

        return "SC990103";
    }

    @RequestMapping(value="/m99/saveAccount", method=RequestMethod.POST)
    public String saveAccount(@ModelAttribute AccountViewDTO accountViewDTO) {

    	Account account = accountViewDTO.getAccount();

        String rolesStr = accountViewDTO.getRoles();
        String[] roleIds = StringUtil.split(rolesStr, ",");

        Role role = null;
        for (String roleId : roleIds) {
        	if (StringUtil.isNotEmpty(roleId)) {
        		role = getRoleService.execute(Long.parseLong(roleId));
            	account.addRole(role);
        	}
        }

        saveAccountService.execute(account);

        securityResourceManager.refreshSecurityResource();

        return "redirect:/m99/SC990103";
    }

    @RequestMapping(value="/m99/deleteAccount/{id}", method=RequestMethod.GET)
    public String deleteAccount(@PathVariable Long id) {
        deleteAccountService.execute(id);

        securityResourceManager.refreshSecurityResource();

        return "redirect:/m99/SC990103";
    }

}
