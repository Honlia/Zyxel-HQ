package cn.superfw.application.model.m01.dto;

import java.io.Serializable;

import cn.superfw.application.domain.Project;
import cn.superfw.application.workflow.dto.WorkflowInfo;

public class ProjectSearchResult implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private WorkflowInfo workflowInfo;
	private Project project;

	public ProjectSearchResult(WorkflowInfo workflowInfo, Project project) {
		super();
		this.workflowInfo = workflowInfo;
		this.project = project;
	}

	public WorkflowInfo getWorkflowInfo() {
		return workflowInfo;
	}
	public void setWorkflowInfo(WorkflowInfo workflowInfo) {
		this.workflowInfo = workflowInfo;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}

}
