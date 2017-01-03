package cn.superfw.application.dao;

import org.springframework.stereotype.Repository;

import cn.superfw.application.domain.Agent;import cn.superfw.framework.orm.hibernate.HibernateDao;

@Repository
public class AgentDAO extends HibernateDao<Agent, Long> {

}

