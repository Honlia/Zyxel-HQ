package cn.superfw.application.model.m01.service;

import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.ProductDAO;
import cn.superfw.application.domain.Product;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GetProductService extends BaseBLogicService<Map<String, String>, Product> {

	@Autowired
    private ProductDAO productDAO;

	@Override
	protected Product doService(Map<String, String> param) {
		String productLine = param.get("productLine");
		String productModel = param.get("productModel");

		Product product = productDAO.findUnique(
                Restrictions.eq("productLine", productLine),
                Restrictions.eq("productModel", productModel));

		return product;
	}

}
