package cn.superfw.application.dao;

import org.springframework.stereotype.Repository;

import cn.superfw.application.domain.ProductDetail;import cn.superfw.framework.orm.hibernate.HibernateDao;

@Repository
public class ProductDetailDAO extends HibernateDao<ProductDetail, Long> {

}

