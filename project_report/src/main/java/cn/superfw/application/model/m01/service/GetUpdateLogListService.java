package cn.superfw.application.model.m01.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.UpdateLogDAO;
import cn.superfw.application.domain.UpdateLog;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GetUpdateLogListService extends BaseBLogicService<String, List<UpdateLog>> {

    @Autowired
    private UpdateLogDAO updateLogDAO;

	@Override
	protected List<UpdateLog> doService(String projectId) {

		List<UpdateLog> updLogList =
                updateLogDAO.find("from UpdateLog o where projectId=? order by o.updTime desc", projectId);

        return updLogList;
	}

}
