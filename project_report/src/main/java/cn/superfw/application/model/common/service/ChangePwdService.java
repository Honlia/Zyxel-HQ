package cn.superfw.application.model.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.AccountDAO;
import cn.superfw.application.domain.Account;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class ChangePwdService extends BaseBLogicService<Account, Void> {

    @Autowired
    private AccountDAO accountDAO;

    @Override
    protected Void doService(Account param) {

        Account account = accountDAO.findUniqueBy("id", param.getId());
        account.setPassword(param.getPassword());
        accountDAO.save(account);

        return null;
    }

}
