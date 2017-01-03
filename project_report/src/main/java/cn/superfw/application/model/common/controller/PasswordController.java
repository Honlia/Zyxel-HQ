package cn.superfw.application.model.common.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.superfw.application.domain.Account;
import cn.superfw.application.model.common.dto.ChangePwdCommnad;
import cn.superfw.application.web.uvo.MyUVO;
import cn.superfw.framework.CommonContants;
import cn.superfw.framework.web.BLogicService;
import cn.superfw.framework.web.BaseController;

@Controller
public class PasswordController extends BaseController {

    @Autowired
    private BLogicService<Account, Void> changePwdService;

    @RequestMapping(value="/changePwd")
    public String changePwdInit(ModelMap model, ChangePwdCommnad changePwdCommnad, HttpSession session) {

        MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
        changePwdCommnad.setId(uvo.getAccountId());
        return "13_changePassword";
    }

    @RequestMapping(value="/changePwd", params={"save"})
    public String changePwd(ModelMap model, ChangePwdCommnad changePwdCommnad, HttpSession session) {

        MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
        if (!changePwdCommnad.getPassword().equals(uvo.getPassword())) {
            model.addAttribute(CommonContants.ERR, "原密码不正确，请重新输入！");
            return "13_changePassword";
        }

        Account account = new Account();
        account.setId(changePwdCommnad.getId());
        account.setPassword(changePwdCommnad.getNewPassword());
        changePwdService.execute(account);
        uvo.setPassword(changePwdCommnad.getNewPassword());

        model.addAttribute(CommonContants.MSG, "密码已成功变更,下次登录请使用新密码！");
        return "13_changePassword";
    }

}
