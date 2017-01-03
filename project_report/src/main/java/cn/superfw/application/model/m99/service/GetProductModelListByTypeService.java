package cn.superfw.application.model.m99.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.ProductDAO;
import cn.superfw.framework.utils.StringUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GetProductModelListByTypeService extends BaseBLogicService<String, List<String>> {

	@Autowired
	private ProductDAO productDAO;

	@Override
	@SuppressWarnings("unchecked")
	protected List<String> doService(String productType) {

		List<String> productModelList = null;

		if (StringUtil.isEmpty(productType)) {
			productModelList = productDAO.createQuery("select distinct productModel from Product").list();
		} else {
			productModelList = productDAO.createQuery("select distinct productModel from Product t where t.productType=?", productType).list();
		}

		return productModelList;
	}

}
