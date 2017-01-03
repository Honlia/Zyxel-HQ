package cn.superfw.application.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.superfw.framework.CommonContants;

@Controller
public class IndexController {

	@RequestMapping(value = "/")
	public String index() {
		return "01_login";
	}

	@RequestMapping(value = "/", params={"error"})
	public String loginError(ModelMap model) {
		model.addAttribute(CommonContants.ERR, "登录验证信息不正确");
		return "01_login";
	}

	@RequestMapping(value = "/admin")
	public String adminMenu() {
		return "admin_menu";
	}

	@RequestMapping(value = "/home")
	public String home() {
	    return "redirect:/todoTasks";
	}
}
