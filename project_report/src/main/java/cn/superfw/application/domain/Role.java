package cn.superfw.application.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name="m_role")
public class Role extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;


	/** 角色名称 */
	private String name;
	/** 角色描述 */
	private String discription;
	/** 资源集合 */
	private Set<Resource> resourceSet;
	/** 用户集合 */
	private Set<Account> accountSet;

	public Role() {
        super();
        this.resourceSet = new HashSet<>();
        this.accountSet = new HashSet<>();
    }

	public Role(String name, String discription) {
        super();
        this.name = name;
        this.discription = discription;
        this.resourceSet = new HashSet<>();
        this.accountSet = new HashSet<>();
    }

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}

	@ManyToMany(cascade=CascadeType.ALL, mappedBy="roleSet")
	public Set<Resource> getResourceSet() {
		return resourceSet;
	}
	public void setResourceSet(Set<Resource> resourceSet) {
		this.resourceSet = resourceSet;
	}
	public void addResource(Resource resource) {
		this.resourceSet.add(resource);
	}

	@ManyToMany(cascade=CascadeType.ALL, mappedBy="roleSet")
	public Set<Account> getAccountSet() {
		return accountSet;
	}
	public void setAccountSet(Set<Account> accountSet) {
		this.accountSet = accountSet;
	}
	public void addAccount(Account account) {
		this.accountSet.add(account);
	}
}
