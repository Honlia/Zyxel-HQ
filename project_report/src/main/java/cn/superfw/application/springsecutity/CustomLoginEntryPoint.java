package cn.superfw.application.springsecutity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class CustomLoginEntryPoint extends LoginUrlAuthenticationEntryPoint {

	public CustomLoginEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}

	/**
	 * Performs the redirect (or forward) to the login form URL.
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		// 如果是MyAccessDecisionManager抛出的异常,则返回403
		if (authException != null && authException instanceof InsufficientAuthenticationException) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN,
					authException.getMessage());
		} else {
			super.commence(request, response, authException);
		}

	}

}
