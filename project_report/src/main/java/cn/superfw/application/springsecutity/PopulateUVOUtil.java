package cn.superfw.application.springsecutity;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import cn.superfw.application.dao.AccountDAO;
import cn.superfw.application.domain.Account;
import cn.superfw.application.web.uvo.MyUVO;
import cn.superfw.framework.spring.ApplicationContextHolder;

public class PopulateUVOUtil {

    public static MyUVO populateUVO(String username, MyUVO uvo) {

        Account account = getAccountInfo(username);
        if (account != null && uvo != null) {
            setAccountInfoToUVO(uvo, account);
        }
        return uvo;
    }

    private static Account getAccountInfo(String username) {

        Account account = null;

        Session session = null;
        try {

            AccountDAO accountDAO = ApplicationContextHolder.getBean("accountDAO", AccountDAO.class);

            session = accountDAO.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Account.class)
                    .add(Restrictions.eq("username", username));
            account = (Account) criteria.uniqueResult();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return account;
    }

    private static void setAccountInfoToUVO(MyUVO uvo, Account account) {
        uvo.setAccountId(account.getId());
        uvo.setPassword(account.getPassword());
        uvo.setUsername(account.getUsername());
        uvo.setState(account.getState());
        uvo.setNickname(account.getNickname());
        uvo.setHeadPic(account.getHeadPic());
        uvo.setPosition(account.getPosition());
        uvo.setDept(account.getDept());
        uvo.setLastLogonTime(account.getLastLogonTime());
        uvo.setLogonCount(account.getLogonCount());
    }
}
