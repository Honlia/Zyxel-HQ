package cn.superfw.application.springsecutity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import cn.superfw.application.web.uvo.MyUVO;
import cn.superfw.framework.CommonContants;
import cn.superfw.framework.web.UserValueObject;

public class CustomRememberMeFilter extends RememberMeAuthenticationFilter {

    public CustomRememberMeFilter(AuthenticationManager authenticationManager,
            RememberMeServices rememberMeServices) {
        super(authenticationManager, rememberMeServices);

    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, Authentication authResult) {

        String username = null;
        Object principal = authResult.getPrincipal();
        if (principal instanceof UserDetails) {
            username =((UserDetails)principal).getUsername();
        }

        MyUVO uvo = (MyUVO)UserValueObject.createUserValueObject();
        PopulateUVOUtil.populateUVO(username, uvo);
        request.getSession().setAttribute(CommonContants.UVO, uvo);
    }

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException failed) {
        // TODO Auto-generated method stub
        super.onUnsuccessfulAuthentication(request, response, failed);
    }

}
