package cn.superfw.application.model.m99.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.RoleDAO;
import cn.superfw.application.domain.Role;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class ListRoleService extends BaseBLogicService<Void, List<Role>> {

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public List<Role> doService(Void param) {
        List<Role> roleList = roleDAO.getAll();
        return roleList;
    }

}
