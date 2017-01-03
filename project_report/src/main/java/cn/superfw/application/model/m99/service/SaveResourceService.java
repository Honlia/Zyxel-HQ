package cn.superfw.application.model.m99.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.ResourceDAO;
import cn.superfw.application.domain.Resource;
import cn.superfw.framework.utils.BeanUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class SaveResourceService extends BaseBLogicService<Resource, Void> {

	@Autowired
	private ResourceDAO resourceDAO;

	@Override
	protected Void doService(Resource resource) {

	    Resource target = resource;

        if (target.getId() != null) {
            target = resourceDAO.get(target.getId());
            BeanUtil.copyPropertiesNotNull(resource, target);
        }

		resourceDAO.save(target);
		return null;
	}

}
