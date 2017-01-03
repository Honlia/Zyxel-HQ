package cn.superfw.application.model.m01.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.ProductDAO;
import cn.superfw.framework.utils.StringUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GetProductModelService extends BaseBLogicService<Map<String, String>, List<String>> {

	@Autowired
	private ProductDAO productDAO;

	@Override
	@SuppressWarnings("unchecked")
	protected List<String> doService(Map<String, String> map) {

		List<String> productModelList = null;
		String productLine = null;
        String productType = null;

		if (map != null) {
		    productLine = map.get("productLine");
	        productType = map.get("productType");
		}

		if (StringUtil.isEmpty(productLine) && StringUtil.isEmpty(productType)) {
			productModelList = productDAO.createQuery("select distinct productModel from Product").list();
		} else if (StringUtil.isEmpty(productType)) {
		    productModelList = productDAO.createQuery("select distinct productModel from Product t where t.productLine=?",
                    productLine).list();
		} else {
			productModelList = productDAO.createQuery("select distinct productModel from Product t where t.productLine=? and t.productType=?",
			        productLine, productType).list();
		}

		return productModelList;
	}

}
