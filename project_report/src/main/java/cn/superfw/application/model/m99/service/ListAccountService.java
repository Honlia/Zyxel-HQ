package cn.superfw.application.model.m99.service;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.AccountDAO;
import cn.superfw.application.domain.Account;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class ListAccountService extends BaseBLogicService<Void, List<Account>> {

	@Autowired
	private AccountDAO accountDAO;

	@SuppressWarnings("unchecked")
    @Override
	protected List<Account> doService(Void param) {
		List<Account> accountList = accountDAO.createCriteria().addOrder(Order.asc("position")).list();
		return accountList;
	}

}
