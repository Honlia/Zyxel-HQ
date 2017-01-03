package cn.superfw.application.springsecutity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cn.superfw.framework.utils.StringUtil;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request).trim();
        String password = obtainPassword(request);

        if (StringUtil.isEmpty(username)) {
            throw new BadCredentialsException("用户名不能为空");
        }
        if (StringUtil.isEmpty(password)) {
            throw new BadCredentialsException("密码不能为空");
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);

        setDetails(request, authRequest);

        Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);

        return authentication;
    }

}
