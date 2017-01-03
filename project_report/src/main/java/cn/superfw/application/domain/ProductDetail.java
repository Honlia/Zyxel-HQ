package cn.superfw.application.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name="t_product_detail")
public class ProductDetail extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 项目*/
	private Project project;

	/** 产品线 */
	private String productLine;

	/** 类型 */
    private String productType;

	/** 产品型号 */
	private String productModel;

	/** 数量 */
	private Integer number;

	/** 预计成交价*/
	private Double expectedPrice;

	/** 预计成交金额*/
	private Double expectedTotalPrice;

	/** 公开报价 */
	private Double openPrice;

	/** 平台权限价 */
	private Double platformPrice;

	/** 业务部主管价 */
	private Double businessDeptManagerPrice;

	/** MDM主管价 */
	private Double mdmManagerPrice;

	/** 总经理价 */
	private Double generalManagerPrice;

	/** 需求日期 */
	private Date requirementDate;

	/** 最快交期 */
	private Date fastestDeliveryDate;

	/** 说明 */
	private String explains;

	public ProductDetail() {
		super();
	}

	/**
	 * @return the project
	 */
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch=FetchType.LAZY)
    @JoinColumn(name="project_id")
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return the productLine
	 */
	public String getProductLine() {
		return productLine;
	}

	/**
	 * @param productLine the productLine to set
	 */
	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}

	/**
	 * @return the productModel
	 */
	public String getProductModel() {
		return productModel;
	}

	/**
	 * @param productModel the productModel to set
	 */
	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * @return the requirementDate
	 */
	public Date getRequirementDate() {
		return requirementDate;
	}

	/**
	 * @param requirementDate the requirementDate to set
	 */
	public void setRequirementDate(Date requirementDate) {
		this.requirementDate = requirementDate;
	}

	/**
	 * @return the explains
	 */
	public String getExplains() {
		return explains;
	}

	/**
	 * @param explains the explains to set
	 */
	public void setExplains(String explains) {
		this.explains = explains;
	}

	/**
	 * @return the expectedPrice
	 */
	public Double getExpectedPrice() {
		return expectedPrice;
	}

	/**
	 * @param expectedPrice the expectedPrice to set
	 */
	public void setExpectedPrice(Double expectedPrice) {
		this.expectedPrice = expectedPrice;
	}

	public Double getExpectedTotalPrice() {
		return expectedTotalPrice;
	}

	public void setExpectedTotalPrice(Double expectedTotalPrice) {
		this.expectedTotalPrice = expectedTotalPrice;
	}

	public Date getFastestDeliveryDate() {
		return fastestDeliveryDate;
	}

	public void setFastestDeliveryDate(Date fastestDeliveryDate) {
		this.fastestDeliveryDate = fastestDeliveryDate;
	}

	public Double getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(Double openPrice) {
		this.openPrice = openPrice;
	}

	public Double getPlatformPrice() {
		return platformPrice;
	}

	public void setPlatformPrice(Double platformPrice) {
		this.platformPrice = platformPrice;
	}

	public Double getBusinessDeptManagerPrice() {
		return businessDeptManagerPrice;
	}

	public void setBusinessDeptManagerPrice(Double businessDeptManagerPrice) {
		this.businessDeptManagerPrice = businessDeptManagerPrice;
	}

	public Double getMdmManagerPrice() {
		return mdmManagerPrice;
	}

	public void setMdmManagerPrice(Double mdmManagerPrice) {
		this.mdmManagerPrice = mdmManagerPrice;
	}

	public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Double getGeneralManagerPrice() {
		return generalManagerPrice;
	}

	public void setGeneralManagerPrice(Double generalManagerPrice) {
		this.generalManagerPrice = generalManagerPrice;
	}



}
