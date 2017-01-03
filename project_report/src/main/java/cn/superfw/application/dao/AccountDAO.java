package cn.superfw.application.dao;

import org.springframework.stereotype.Repository;

import cn.superfw.application.domain.Account;import cn.superfw.framework.orm.hibernate.HibernateDao;

@Repository
public class AccountDAO extends HibernateDao<Account, Long> {

}

