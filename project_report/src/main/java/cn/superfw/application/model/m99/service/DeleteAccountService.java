package cn.superfw.application.model.m99.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.AccountDAO;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class DeleteAccountService extends BaseBLogicService<Long, Void> {

	@Autowired
	private AccountDAO accountDAO;

	@Override
	protected Void doService(Long id) {
		accountDAO.delete(id);
		return null;
	}

}
