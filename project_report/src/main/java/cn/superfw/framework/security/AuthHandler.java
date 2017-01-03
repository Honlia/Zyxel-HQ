package cn.superfw.framework.security;

import javax.servlet.ServletRequest;

public interface AuthHandler {
    boolean isAuthorized(String pathInfo, ServletRequest req);
    boolean isCheckRequired(ServletRequest req);
}
