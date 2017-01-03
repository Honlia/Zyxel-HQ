package cn.superfw.application.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name="m_resource")
public class Resource extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 资源名称 */
	private String name;
	/** 资源类型 */
	private String type;
    /** 资源字符串 */
	private String resString;
	/** 角色集合 */
	private Set<Role> roleSet;

	public Resource() {
		this.roleSet = new HashSet<Role>();
	}

	public Resource(String name, String type, String resString) {
        super();
        this.name = name;
        this.type = type;
        this.resString = resString;
        this.roleSet = new HashSet<Role>();
    }

   public Resource(String name, String type, String resString, Role... roles) {
        super();
        this.name = name;
        this.type = type;
        this.resString = resString;
        this.roleSet = new HashSet<Role>();
        this.roleSet.addAll(Arrays.asList(roles));
    }

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getResString() {
		return resString;
	}
	public void setResString(String resString) {
		this.resString = resString;
	}
	@ManyToMany(cascade={CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name="m_role_resource",joinColumns={@JoinColumn(name="resource_id")},
	inverseJoinColumns={@JoinColumn(name="role_id")})
	public Set<Role> getRoleSet() {
		return roleSet;
	}
	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}
	public void addRole(Role role) {
		this.roleSet.add(role);
	}
}
