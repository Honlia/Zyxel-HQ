package cn.superfw.application.model.m01.dto;

import java.io.Serializable;

import cn.superfw.application.domain.Project;

public class JudgePriceCommand implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String position;
	private Project project;

	public JudgePriceCommand(String position, Project project) {
		super();
		this.position = position;
		this.project = project;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}



}
