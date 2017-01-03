package cn.superfw.application.dao;

import org.springframework.stereotype.Repository;

import cn.superfw.application.domain.Product;import cn.superfw.framework.orm.hibernate.HibernateDao;

@Repository
public class ProductDAO extends HibernateDao<Product, Long> {

}

