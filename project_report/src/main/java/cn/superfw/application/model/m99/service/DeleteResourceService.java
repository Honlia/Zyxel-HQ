package cn.superfw.application.model.m99.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.ResourceDAO;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class DeleteResourceService extends BaseBLogicService<Long, Void> {

	@Autowired
	private ResourceDAO resourceDAO;

	@Override
	protected Void doService(Long id) {
		resourceDAO.delete(id);
		return null;
	}

}
