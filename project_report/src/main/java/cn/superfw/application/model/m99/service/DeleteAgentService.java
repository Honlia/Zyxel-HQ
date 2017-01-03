package cn.superfw.application.model.m99.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.AgentDAO;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class DeleteAgentService extends BaseBLogicService<Long, Void> {

    @Autowired
    private AgentDAO agentDAO;

    @Override
    protected Void doService(Long id) {

    	agentDAO.delete(id);
        return null;
    }

}
