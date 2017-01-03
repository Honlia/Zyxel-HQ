package cn.superfw.application.model.m99.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.ProductDAO;
import cn.superfw.application.domain.Product;
import cn.superfw.framework.utils.BeanUtil;
import cn.superfw.framework.utils.DateUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class SaveProductService extends BaseBLogicService<Product, Long> {

    @Autowired
    private ProductDAO productDAO;

    @Override
    protected Long doService(Product product) {

    	Product target = product;

        // 更新
        if (target.getId() != null) {
            target = productDAO.get(target.getId());
            BeanUtil.copyPropertiesNotNull(product, target);
        // 新规
        } else {
            target.setAddTime(DateUtil.getCurrentDate());
        }

        target.setUpdTime(DateUtil.getCurrentDate());
        productDAO.save(target);

        return target.getId();
    }

}
