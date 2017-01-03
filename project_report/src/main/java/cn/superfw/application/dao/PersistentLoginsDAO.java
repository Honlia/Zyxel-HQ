package cn.superfw.application.dao;

import org.springframework.stereotype.Repository;

import cn.superfw.application.domain.PersistentLogins;import cn.superfw.framework.orm.hibernate.HibernateDao;

@Repository
public class PersistentLoginsDAO extends HibernateDao<PersistentLogins, Long> {

}

