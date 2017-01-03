package cn.superfw.application.model.m99.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.AgentDAO;
import cn.superfw.application.domain.Agent;
import cn.superfw.framework.utils.BeanUtil;
import cn.superfw.framework.utils.DateUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class SaveAgentService extends BaseBLogicService<Agent, Long> {

    @Autowired
    private AgentDAO agentDAO;

    @Override
    protected Long doService(Agent agent) {

        Agent target = agent;

        // 更新
        if (target.getId() != null) {
            target = agentDAO.get(target.getId());
            BeanUtil.copyPropertiesNotNull(agent, target);
        // 新规
        } else {
            target.setAddTime(DateUtil.getCurrentDate());
        }

        target.setUpdTime(DateUtil.getCurrentDate());
        agentDAO.save(target);

        return target.getId();
    }

}
