package cn.superfw.application.model.m99.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.RoleDAO;
import cn.superfw.application.domain.Role;
import cn.superfw.framework.utils.BeanUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class SaveRoleService extends BaseBLogicService<Role, Void> {

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public Void doService(Role role) {

        Role target = role;

        if (target.getId() != null) {
            target = roleDAO.get(target.getId());
            BeanUtil.copyPropertiesNotNull(role, target);
        }

        roleDAO.save(target);
        return null;
    }
}
