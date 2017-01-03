package cn.superfw.application.model.m99.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.ResourceDAO;
import cn.superfw.application.domain.Resource;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class ListResourceService extends BaseBLogicService<Void, List<Resource>> {

	@Autowired
	private ResourceDAO resourceDAO;

	@Override
	protected List<Resource> doService(Void param) {
		List<Resource> resourceList = resourceDAO.getAll();
		return resourceList;
	}

}
