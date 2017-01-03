package cn.superfw.application.web.handler;

import javax.servlet.ServletRequest;

import cn.superfw.framework.security.LoginHandler;

public class MyLoginHandler implements LoginHandler {

	@Override
	public boolean isLoginSuccessed(String pathInfo, ServletRequest req) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCheckRequired(ServletRequest req) {
		// TODO Auto-generated method stub
		return false;
	}

}
