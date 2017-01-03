package cn.superfw.application.model.m99.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.RoleDAO;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class DeleteRoleService extends BaseBLogicService<Long, Void> {

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public Void doService(Long id) {
        roleDAO.delete(id);
        return null;
    }
}
