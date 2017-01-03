package cn.superfw.application.dao;

import org.springframework.stereotype.Repository;

import cn.superfw.application.domain.UpdateLog;
import cn.superfw.framework.orm.hibernate.HibernateDao;

@Repository("updateLogDAO")
public class UpdateLogDAO extends HibernateDao<UpdateLog, Long> {

}

