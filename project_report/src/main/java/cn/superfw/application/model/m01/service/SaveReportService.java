package cn.superfw.application.model.m01.service;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.ProductDAO;
import cn.superfw.application.dao.ProductDetailDAO;
import cn.superfw.application.dao.ProjectDAO;
import cn.superfw.application.domain.Product;
import cn.superfw.application.domain.ProductDetail;
import cn.superfw.application.domain.Project;
import cn.superfw.framework.utils.BeanUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class SaveReportService extends BaseBLogicService<Project, Long> {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private ProductDetailDAO productDetailDAO;



    @Override
    protected Long doService(Project project) {
        Product product = null;

        // 小技巧:通过hibernate来进行插入操作的时候，不管是一对多、一对一还是多对多，都只需要记住一点，
        // 在哪个实体类声明了外键，就由哪个类来维护关系，在保存数据时，总是先保存的是没有维护关联关系的那一方的数据，
        // 后保存维护了关联关系的那一方的数据

        // 新建
        if (project.getId() == null) {
            // Project 没有维护关系，所以先保存
            projectDAO.save(project);

            for (ProductDetail item : project.getProductDetailList()) {

                product = productDAO.findUnique(
                        Restrictions.eq("productLine", item.getProductLine()),
                        Restrictions.eq("productModel", item.getProductModel()));

                // 建立Project和ProjuctDetail的关联关系,关系由ProjuctDetail中的外键project_id来维护
                item.setProject(project);

                // 各级审批参考用权限价复制
                item.setBusinessDeptManagerPrice(product.getBusinessDeptManagerPrice());
                item.setMdmManagerPrice(product.getMdmManagerPrice());
                item.setGeneralManagerPrice(product.getGeneralManagerPrice());

                productDetailDAO.save(item);
            }
        // 更新
        } else {
            Project target = projectDAO.findUnique(Restrictions.idEq(project.getId()));

            // 清空商品列表
            for (ProductDetail delObj : target.getProductDetailList()) {
                productDetailDAO.delete(delObj.getId());
            }

            BeanUtil.copyPropertiesNotNull(project, target);
            projectDAO.save(target);

            // 重新插入商品列表
            for (ProductDetail item : project.getProductDetailList()) {
                // 建立Project和ProjuctDetail的关联关系,关系由ProjuctDetail中的外键project_id来维护
                item.setProject(target);

                product = productDAO.findUnique(
                        Restrictions.eq("productLine", item.getProductLine()),
                        Restrictions.eq("productModel", item.getProductModel()));

                // 各级审批参考用权限价复制
                item.setBusinessDeptManagerPrice(product.getBusinessDeptManagerPrice());
                item.setMdmManagerPrice(product.getMdmManagerPrice());
                item.setGeneralManagerPrice(product.getGeneralManagerPrice());
                productDetailDAO.save(item);
            }
        }

        return project.getId();
    }

}
