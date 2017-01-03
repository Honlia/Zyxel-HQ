package cn.superfw.application.model.m99.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.ProductDAO;
import cn.superfw.application.domain.Product;
import cn.superfw.framework.utils.StringUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GetProductListService extends BaseBLogicService<Product, List<Product>> {

    @Autowired
    private ProductDAO productDAO;

    @SuppressWarnings("unchecked")
    @Override
    public List<Product> doService(Product product) {

        // 检索条件
        List<Criterion> criterionList = new ArrayList<>();

        if (product != null) {
            // id
            if (product.getId() != null) {
                criterionList.add(Restrictions.eq("id", product.getId()));
            }
            // 产品线
            if (StringUtil.isNotEmpty(product.getProductLine())) {
                criterionList.add(Restrictions.eq("productLine", product.getProductLine()));
            }
            // 类型
            if (StringUtil.isNotEmpty(product.getProductType())) {
                criterionList.add(Restrictions.eq("productType", product.getProductType()));
            }
            // 型号
            if (StringUtil.isNotEmpty(product.getProductModel())) {
                criterionList.add(Restrictions.eq("productModel", product.getProductModel()));
            }
        }

        Criteria c = productDAO.createCriteria(criterionList.toArray(new Criterion[]{}));

        return c.list();
    }

}
