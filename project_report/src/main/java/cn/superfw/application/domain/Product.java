package cn.superfw.application.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name="m_product")
public class Product extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 产品线 */
	private String productLine;
	/** 类型 */
	private String productType;
	/** 型号 */
	private String productModel;
	/** 规格 */
	private String specifications;
	/** 成本价 */
	//private Double costPrice;
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
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
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
	 * @return the specifications
	 */
	public String getSpecifications() {
		return specifications;
	}
	/**
	 * @param specifications the specifications to set
	 */
	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}
/*	public Double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}*/
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
	public Double getGeneralManagerPrice() {
		return generalManagerPrice;
	}
	public void setGeneralManagerPrice(Double generalManagerPrice) {
		this.generalManagerPrice = generalManagerPrice;
	}

}
