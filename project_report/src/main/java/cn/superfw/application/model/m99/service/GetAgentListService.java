package cn.superfw.application.model.m99.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.AgentDAO;
import cn.superfw.application.domain.Agent;
import cn.superfw.framework.utils.StringUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GetAgentListService extends BaseBLogicService<Agent, List<Agent>> {

    @Autowired
    private AgentDAO agentDAO;

    @SuppressWarnings("unchecked")
    @Override
    public List<Agent> doService(Agent agent) {

        // 检索条件
        List<Criterion> criterionList = new ArrayList<>();

        if (agent != null) {
            // id
            if (agent.getId() != null) {
                criterionList.add(Restrictions.eq("id", agent.getId()));
            }
            // 代理商编号
            if (StringUtil.isNotEmpty(agent.getAgentNum())) {
                criterionList.add(Restrictions.eq("agentNum", agent.getAgentNum()));
            }
            // 代理商名称
            if (StringUtil.isNotEmpty(agent.getAgentName())) {
                criterionList.add(Restrictions.eq("agentName", agent.getAgentName()));
            }
            // 主营业务
            if (StringUtil.isNotEmpty(agent.getMainBusiness())) {
                criterionList.add(Restrictions.eq("mainBusiness", agent.getMainBusiness()));
            }
            // 地理区域
            if (StringUtil.isNotEmpty(agent.getGeographyArea())) {
                criterionList.add(Restrictions.eq("geographyArea", agent.getGeographyArea()));
            }
            // 联系人
            if (StringUtil.isNotEmpty(agent.getContactPerson())) {
                criterionList.add(Restrictions.eq("contactPerson", agent.getContactPerson()));
            }
            // 所属业务部门
            if (StringUtil.isNotEmpty(agent.getDept())) {
                criterionList.add(Restrictions.eq("dept", agent.getDept()));
            }
            // 业务负责人
            if (StringUtil.isNotEmpty(agent.getBusinessDirector())) {
                criterionList.add(Restrictions.eq("businessDirector", agent.getBusinessDirector()));
            }
        }

        Criteria c = agentDAO.createCriteria(criterionList.toArray(new Criterion[]{}));
        c.addOrder(Order.asc("agentNum"));

        return c.list();
    }

}
