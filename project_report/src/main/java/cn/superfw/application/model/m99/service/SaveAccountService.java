package cn.superfw.application.model.m99.service;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.AccountDAO;
import cn.superfw.application.domain.Account;
import cn.superfw.framework.exception.BusinessException;
import cn.superfw.framework.utils.BeanUtil;
import cn.superfw.framework.utils.DateUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class SaveAccountService extends BaseBLogicService<Account, Void> {

	@Autowired
	private AccountDAO accountDAO;

	@Override
	protected Void doService(Account account) {

	    Account target = account;

        if (target.getId() != null) {
            target = accountDAO.get(target.getId());
            BeanUtil.copyPropertiesNotNull(account, target);
        } else {
            checkUsernameUnique(account);
            target.setAddTime(DateUtil.getCurrentDate());
        }

        target.setUpdTime(DateUtil.getCurrentDate());
		accountDAO.save(target);

		return null;
	}

    private void checkUsernameUnique(Account account) {
        try {
            Account checkAccount = accountDAO.findUniqueBy("username", account.getUsername());
            if (checkAccount!= null) {
                throw new BusinessException("用户名必须唯一");
            }
        } catch (HibernateException e) {
            throw new BusinessException("用户名必须唯一", e);
        }
    }

}
