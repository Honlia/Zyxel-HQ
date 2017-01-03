package cn.superfw.application.model.m99.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.AccountDAO;
import cn.superfw.application.domain.Account;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GetAccountService extends BaseBLogicService<String, Account> {

	@Autowired
	private AccountDAO accountDAO;

	@Override
	protected Account doService(String username) {
		Account account = accountDAO.findUniqueBy("username", username);
		return account;
	}

}
