package cn.superfw.application.model.m99.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.ProductDAO;
import cn.superfw.framework.utils.StringUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GetProductTypeListService extends BaseBLogicService<String, List<String>> {

    @Autowired
    private ProductDAO productDAO;

    @Override
    @SuppressWarnings("unchecked")
    protected List<String> doService(String productLine) {

        List<String> productTypeList = null;

        if (StringUtil.isEmpty(productLine)) {
            productTypeList = productDAO.createQuery("select distinct productType from Product").list();
        } else {
            productTypeList = productDAO.createQuery("select distinct productType from Product t where t.productLine=?", productLine).list();
        }

        return productTypeList;
    }

}
