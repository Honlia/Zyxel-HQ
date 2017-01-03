package cn.superfw.application.model.m99.dto;

import java.io.Serializable;

import cn.superfw.application.domain.Resource;

public class ResourceViewDTO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private Resource resource;
    private String roles;

    public ResourceViewDTO() {
        super();
        this.resource = new Resource();
        this.roles = "";
    }

    public Resource getResource() {
        return resource;
    }
    public void setResource(Resource resource) {
        this.resource = resource;
    }
    public String getRoles() {
        return roles;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }
}
