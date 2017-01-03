package cn.superfw.application.model.m01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.ProjectDAO;
import cn.superfw.application.domain.Project;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GetProjectService extends BaseBLogicService<Long, Project> {

    @Autowired
    private ProjectDAO projectDAO;

    @Override
    protected Project doService(Long id) {

        Project project = projectDAO.get(id);
        return project;
    }

}
