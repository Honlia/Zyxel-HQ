package cn.superfw.application.model.m99.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.RoleDAO;
import cn.superfw.application.domain.Role;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GetRoleService extends BaseBLogicService<Long, Role> {

	@Autowired
	private RoleDAO roleDAO;

	@Override
	protected Role doService(Long id) {
		Role role = roleDAO.get(id);
		return role;
	}

}
