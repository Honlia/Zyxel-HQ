package cn.superfw.application.dao;

import org.springframework.stereotype.Repository;

import cn.superfw.application.domain.Role;import cn.superfw.framework.orm.hibernate.HibernateDao;

@Repository
public class RoleDAO extends HibernateDao<Role, Long> {

}

