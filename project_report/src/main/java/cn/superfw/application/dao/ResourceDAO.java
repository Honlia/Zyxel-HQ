package cn.superfw.application.dao;

import org.springframework.stereotype.Repository;

import cn.superfw.application.domain.Resource;import cn.superfw.framework.orm.hibernate.HibernateDao;

@Repository
public class ResourceDAO extends HibernateDao<Resource, Long> {

}

