package cn.superfw.application.model.m01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.UpdateLogDAO;
import cn.superfw.application.domain.UpdateLog;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class SaveUpdateLogService extends BaseBLogicService<UpdateLog, Long> {

    @Autowired
    private UpdateLogDAO updateLogDAO;

    @Override
    protected Long doService(UpdateLog updateLog) {

        updateLogDAO.save(updateLog);
        return updateLog.getId();
    }

}
