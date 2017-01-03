package cn.superfw.application.springsecutity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import cn.superfw.framework.CommonContants;

public class CustomLogoutHandler implements LogoutHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomLogoutHandler.class);

    public CustomLogoutHandler() {
    }

    @Override
    public void logout(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(CommonContants.UVO);
            session.invalidate();
        }
        log.info("CustomLogoutSuccessHandler#logout() is called!");
    }
}
