package cn.superfw.application.model.m99.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.AccountDAO;
import cn.superfw.framework.utils.StringUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GetBusinessDirectorService extends BaseBLogicService<String, List<String>> {

    @Autowired
    private AccountDAO accountDAO;

    @Override
    @SuppressWarnings("unchecked")
    protected List<String> doService(String dept) {

        List<String> businessDirectorList = null;

        if (StringUtil.isEmpty(dept)) {
            businessDirectorList = accountDAO.createQuery("select distinct username from Account").list();
        } else {
            businessDirectorList = accountDAO.createQuery("select distinct username from Account t where t.dept=?", dept).list();
        }

        return businessDirectorList;
    }

}
