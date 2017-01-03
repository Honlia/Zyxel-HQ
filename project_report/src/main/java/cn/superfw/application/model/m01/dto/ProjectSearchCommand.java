package cn.superfw.application.model.m01.dto;

import java.io.Serializable;
import java.util.Date;

public class ProjectSearchCommand implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private String dept;
    private String projectNumber;
    private String projectName;
    private String salesName;
    private Date expectedShipmentTimeFrom;
    private Date expectedShipmentTimeTo;
    private Date applyTimeFrom;
    private Date applyTimeTo;
    private String status;
    private String productLine;
    private String productType;
    private String cooperativePartner;
    private String productModel;
    private int pageNo = 0;
    private int pageSize = 50;

	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getSalesName() {
		return salesName;
	}
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}
	public Date getExpectedShipmentTimeFrom() {
		return expectedShipmentTimeFrom;
	}
	public void setExpectedShipmentTimeFrom(Date expectedShipmentTimeFrom) {
		this.expectedShipmentTimeFrom = expectedShipmentTimeFrom;
	}
	public Date getExpectedShipmentTimeTo() {
		return expectedShipmentTimeTo;
	}
	public void setExpectedShipmentTimeTo(Date expectedShipmentTimeTo) {
		this.expectedShipmentTimeTo = expectedShipmentTimeTo;
	}
	public Date getApplyTimeFrom() {
		return applyTimeFrom;
	}
	public void setApplyTimeFrom(Date applyTimeFrom) {
		this.applyTimeFrom = applyTimeFrom;
	}
	public Date getApplyTimeTo() {
		return applyTimeTo;
	}
	public void setApplyTimeTo(Date applyTimeTo) {
		this.applyTimeTo = applyTimeTo;
	}
	public String getProductLine() {
		return productLine;
	}
	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}
	public String getCooperativePartner() {
		return cooperativePartner;
	}
	public void setCooperativePartner(String cooperativePartner) {
		this.cooperativePartner = cooperativePartner;
	}
	public String getProductModel() {
		return productModel;
	}
	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getProjectNumber() {
        return projectNumber;
    }
    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }
    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


}
