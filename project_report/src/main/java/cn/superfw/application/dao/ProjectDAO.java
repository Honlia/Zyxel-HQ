package cn.superfw.application.dao;

import org.springframework.stereotype.Repository;

import cn.superfw.application.domain.Project;import cn.superfw.framework.orm.hibernate.HibernateDao;

@Repository
public class ProjectDAO extends HibernateDao<Project, Long> {

}

