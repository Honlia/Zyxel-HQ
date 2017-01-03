package cn.superfw.framework.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ExceptionController {

    @RequestMapping(value = {"/403", "/forbidden"}, method = RequestMethod.GET)
    public String forbidden() {
        return "403";
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String pageNotFound() {
        return "404";
    }

    @RequestMapping(value = "/405", method = RequestMethod.GET)
    public String invalidRequest() {
        return "error";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String serverError() {
        return "error";
    }
}
