package cn.superfw.framework.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.superfw.framework.exception.NotLoginException;
import cn.superfw.framework.utils.RequestUtil;

public class LoginFilter extends AbstractFilter<LoginHandler> {

	private static Logger log = LoggerFactory.getLogger(LoginFilter.class);

	private static String LOGIN_PASS_KEY = "LOGIN_PASS_KEY";

	private static final String LOGIN_HANDLER_ERROR = "errors.login.handler";

	private static final Class<LoginHandler> LOGIN_HANDLER_CLASS = LoginHandler.class;

	private static final String DEFAULT_LOGIN_HANDLER_BEAN_ID = "loginHandler";

	protected static LoginHandler handler = null;

	public static LoginHandler getLoginHandler() {
		return handler;
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
        if (handler == null) {
        	handler = getHandler();
        }
	};

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// 同一个Request只做一次验证
		if (req.getAttribute(LOGIN_PASS_KEY) == null) {
			req.setAttribute(LOGIN_PASS_KEY, "true");

			if (handler.isCheckRequired(req)) {
				if (!handler.isLoginSuccessed(RequestUtil.getPathInfo(req), req)) {
					if (log.isDebugEnabled()) {
                        log.debug("isLoginSuccessed() failed.");
                    }
					throw new NotLoginException();
				}
			}
		}
		chain.doFilter(req, res);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getHandlerClass() {
		return LOGIN_HANDLER_CLASS;
	}

	@Override
	protected String getErrorCode() {
		return LOGIN_HANDLER_ERROR;
	}

	@Override
	public String getDefaultHandlerBeanId() {
		return DEFAULT_LOGIN_HANDLER_BEAN_ID;
	}

}
