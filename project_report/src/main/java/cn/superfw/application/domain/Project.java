package cn.superfw.application.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name="t_project")
public class Project extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;


	public Project() {
		super();
		this.productDetailList = new ArrayList<>();
	}

	/** 产品集合 */
	private List<ProductDetail> productDetailList;

	/** 项目编号 */
	private String projectNumber;
	/** 项目名称 */
	private String projectName;
	/** 合作伙伴 */
	private String cooperativePartner;
	/** 项目地点 */
	private String projectPlace;
	/** 预计出货时间 */
	private Date expectedShipmentTime;
	/** 项目描述*/
	private String projectDescription;
	/** 部门*/
	private String dept;
	/** 申请人*/
	private String applyUser;
	/** 申请时间*/
	@Temporal(TemporalType.TIMESTAMP)
	private Date applyTime;
	/** 状态*/
	private String status;
	/** 当前所在流程*/
	private String currentProcess;
	/** 报备修改次数*/
    private Integer updateCount = 0;



	/**
	 * @param productDetail the product to set
	 */
	public void addProductDetail(ProductDetail productDetail) {
		this.productDetailList.add(productDetail);
	}

	/**
	 * @return the productList
	 */
	@OneToMany(mappedBy="project")
	public List<ProductDetail> getProductDetailList() {
		return productDetailList;
	}

	/**
	 * @param productList the productList to set
	 */
	public void setProductDetailList(List<ProductDetail> productDetailList) {
		this.productDetailList = productDetailList;
	}

	/**
	 * @return the projectNumber
	 */
	public String getProjectNumber() {
		return projectNumber;
	}

	/**
	 * @param projectNumber the projectNumber to set
	 */
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the cooperativePartner
	 */
	public String getCooperativePartner() {
		return cooperativePartner;
	}

	/**
	 * @param cooperativePartner the cooperativePartner to set
	 */
	public void setCooperativePartner(String cooperativePartner) {
		this.cooperativePartner = cooperativePartner;
	}

	/**
	 * @return the projectDescription
	 */
	public String getProjectDescription() {
		return projectDescription;
	}

	/**
	 * @param projectDescription the projectDescription to set
	 */
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	/**
	 * @return the projectPlace
	 */
	public String getProjectPlace() {
		return projectPlace;
	}

	/**
	 * @param projectPlace the projectPlace to set
	 */
	public void setProjectPlace(String projectPlace) {
		this.projectPlace = projectPlace;
	}

	/**
	 * @return the expectedShipmentTime
	 */
	public Date getExpectedShipmentTime() {
		return expectedShipmentTime;
	}

	/**
	 * @param expectedShipmentTime the expectedShipmentTime to set
	 */
	public void setExpectedShipmentTime(Date expectedShipmentTime) {
		this.expectedShipmentTime = expectedShipmentTime;
	}

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentProcess() {
        return currentProcess;
    }

    public void setCurrentProcess(String currentProcess) {
        this.currentProcess = currentProcess;
    }

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	@Column(columnDefinition = "bigint default 0")
    public Integer getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(Integer updateCount) {
        this.updateCount = updateCount;
    }

}
