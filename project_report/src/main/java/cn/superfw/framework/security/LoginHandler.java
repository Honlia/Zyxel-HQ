package cn.superfw.framework.security;

import javax.servlet.ServletRequest;

public interface LoginHandler {
	boolean isLoginSuccessed(String pathInfo, ServletRequest req);
	boolean isCheckRequired(ServletRequest req);
}
