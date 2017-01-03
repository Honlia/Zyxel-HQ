package cn.superfw.application.web.handler;

import javax.servlet.ServletRequest;

import cn.superfw.framework.security.AuthHandler;

public class MyAuthHandler implements AuthHandler {

	@Override
	public boolean isAuthorized(String pathInfo, ServletRequest req) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCheckRequired(ServletRequest req) {
		// TODO Auto-generated method stub
		return false;
	}

}
