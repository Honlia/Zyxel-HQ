package cn.superfw.application.model.m01.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.ProductDAO;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GetProductLineService extends BaseBLogicService<Void, List<String>> {

	@Autowired
	private ProductDAO productDAO;

	@Override
	protected List<String> doService(Void param) {
		@SuppressWarnings("unchecked")
		List<String> productLineList = productDAO.createQuery("select distinct productLine from Product").list();
		return productLineList;
	}

}
