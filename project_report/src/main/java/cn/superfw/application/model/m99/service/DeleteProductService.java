package cn.superfw.application.model.m99.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.ProductDAO;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class DeleteProductService extends BaseBLogicService<Long, Void> {

    @Autowired
    private ProductDAO productDAO;

    @Override
    protected Void doService(Long id) {

        productDAO.delete(id);
        return null;
    }

}
