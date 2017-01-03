package cn.superfw.application.model.m01.service;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.common.AppContants;
import cn.superfw.application.dao.ProductDAO;
import cn.superfw.application.domain.Product;
import cn.superfw.application.domain.ProductDetail;
import cn.superfw.application.domain.Project;
import cn.superfw.application.model.m01.dto.JudgePriceCommand;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class JudgePriceService extends BaseBLogicService<JudgePriceCommand, Integer> {

	@Autowired
	private ProductDAO productDAO;

	@Override
	protected Integer doService(JudgePriceCommand judgePriceCommand) {


		Integer decision = AppContants.DECISION_AUTH_AGREE;

		// 通过订单编号从业务表检索订单,然后将每一件商品的单价和自己的权限价比较
		// 如果>权限价,则同意,
		// 否则,权限内同意

		//String position = judgePriceCommand.getPosition();
		Project project = judgePriceCommand.getProject();
		List<ProductDetail> productDetailList = project.getProductDetailList();
		Product product = null;

		// 所有商品中只要有一件不在权限内,则返回同意,继续向上级审批
		for (ProductDetail item : productDetailList) {
			product = productDAO.findUnique(
					Restrictions.eq("productLine", item.getProductLine()),
					Restrictions.eq("productModel", item.getProductModel()));

			/*switch (position) {
			case AppContants.POSITION_BUSINESS_MANAGER:
				if (item.getExpectedPrice() < product.getBusinessDeptManagerPrice()) {
					decision = AppContants.DECISION_AGREE;
					return decision;
				}
				break;
			case AppContants.POSITION_MDM_MANAGER:
				if (item.getExpectedPrice() < product.getMdmManagerPrice()) {
					decision = AppContants.DECISION_AGREE;
					return decision;
				}
				break;
			case AppContants.POSITION_GENERAL_MANAGER:
				decision = AppContants.DECISION_AGREE;
				return decision;
			}*/

			// 如果报价小于价格B则继续走审批流程，否则直接执行订单
			if (item.getExpectedPrice() < product.getPlatformPrice()) {
				decision = AppContants.DECISION_AGREE;
				return decision;
			} else {
				decision = AppContants.DECISION_AUTH_AGREE;
				return decision;
			}

		}

		// 所有产品价格都在权限内
		return decision;
	}

}
