package cn.superfw.framework.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.superfw.framework.exception.NoAuthException;
import cn.superfw.framework.utils.RequestUtil;

public class AuthFilter extends AbstractFilter<AuthHandler> {

	private static Logger log = LoggerFactory.getLogger(AuthFilter.class);

	private static String AUTH_PASS_KEY = "AUTH_PASS_KEY";

	private static final String AUTH_HANDLER_ERROR = "errors.auth.handler";

	private static final Class<AuthHandler> AUTH_HANDLER_CLASS = AuthHandler.class;

	private static final String DEFAULT_AUTH_HANDLER_BEAN_ID = "authHandler";

	protected static AuthHandler handler = null;

	public static AuthHandler getAuthHandler() {
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
		if (req.getAttribute(AUTH_PASS_KEY) == null) {
			req.setAttribute(AUTH_PASS_KEY, "true");

			if (handler.isCheckRequired(req)) {
				if (!handler.isAuthorized(RequestUtil.getPathInfo(req), req)) {
					if (log.isDebugEnabled()) {
                        log.debug("isAuthorized() failed.");
                    }
					throw new NoAuthException();
				}
			}
		}
		chain.doFilter(req, res);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getHandlerClass() {
		return AUTH_HANDLER_CLASS;
	}

	@Override
	protected String getErrorCode() {
		return AUTH_HANDLER_ERROR;
	}

	@Override
	public String getDefaultHandlerBeanId() {
		return DEFAULT_AUTH_HANDLER_BEAN_ID;
	}

}
