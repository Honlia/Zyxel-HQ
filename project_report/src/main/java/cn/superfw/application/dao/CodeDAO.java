package cn.superfw.application.dao;

import org.springframework.stereotype.Repository;

import cn.superfw.application.domain.Code;import cn.superfw.framework.orm.hibernate.HibernateDao;

@Repository
public class CodeDAO extends HibernateDao<Code, Long> {

}

