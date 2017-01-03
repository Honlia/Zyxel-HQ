package cn.superfw.application.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superfw.application.common.AppContants;
import cn.superfw.application.dao.RoleDAO;
import cn.superfw.application.domain.Account;
import cn.superfw.application.domain.Resource;
import cn.superfw.application.domain.Role;
import cn.superfw.framework.web.BLogicService;
import cn.superfw.framework.web.BaseController;

@Controller
public class SystemInitController extends BaseController {

    @Autowired
    private BLogicService<Resource, Void> saveResourceService;

    @Autowired
    private BLogicService<Role, Void> saveRoleService;

    @Autowired
    private BLogicService<Account, Void> saveAccountService;

    @Autowired
    BLogicService<String, Map<String, Object>> deployWorkflowService;

    @Autowired
    private BLogicService<String, Account> getAccountService;

    @Autowired
    private RoleDAO roleDAO;


    @RequestMapping(value="/admin/sysinit")
    @ResponseBody
    public String init() {

        roleDAO.createQuery("delete from Role");
        roleDAO.createQuery("delete from Resource");
        roleDAO.createQuery("delete from Account");

        saveRoleService.execute(new Role(AppContants.ROLE_ADMIN, "管理员"));
        saveRoleService.execute(new Role(AppContants.ROLE_USER, "普通用户"));

        Role roleAdmin = roleDAO.findUniqueBy("name", AppContants.ROLE_ADMIN);
        Role roleUser = roleDAO.findUniqueBy("name", AppContants.ROLE_USER);

        saveResourceService.execute(new Resource("新建报备项目", "url", "/newReport", roleUser, roleAdmin));
        saveResourceService.execute(new Resource("项目报备查看", "url", "/searchReport", roleUser, roleAdmin));
        saveResourceService.execute(new Resource("项目报备更新", "url", "/editReport/**", roleUser, roleAdmin));
        saveResourceService.execute(new Resource("待办任务", "url", "/todoTasks", roleUser, roleAdmin));

        saveResourceService.execute(new Resource("工作流管理", "url", "/workflow/**", roleAdmin));
        saveResourceService.execute(new Resource("后台管理", "url", "/m99/**", roleAdmin));
        saveResourceService.execute(new Resource("系统初始化", "url", "/admin/**", roleAdmin));

        Account admin = getAccountService.execute(AppContants.USERNAME_ADMIN);
        if (admin == null) {

            admin = new Account();
            admin.setUsername(AppContants.USERNAME_ADMIN);
            admin.setPassword(AppContants.USERNAME_ADMIN);

            admin.addRole(roleAdmin);
            admin.addRole(roleUser);

            saveAccountService.execute(admin);
        }

        return "System Init OK!";
    }
}
