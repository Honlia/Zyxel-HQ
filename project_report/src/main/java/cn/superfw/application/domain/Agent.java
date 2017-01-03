package cn.superfw.application.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name="m_agent")
public class Agent extends BaseEntity implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /** 代理商编号 */
    private String agentNum;
    /** 代理商名称 */
    private String agentName;
    /** 主营业务 */
    private String mainBusiness;
    /** 公司地址 */
    private String address;
    /** 签约日期 */
    private String signDate;
    /** 起始日期 */
    private String startDate;
    /** 结束日期 */
    private String endDate;
    /** 所属业务部门 */
    private String dept;
    /** 业务负责人 */
    private String businessDirector;
    /** 地理区域 */
    private String geographyArea;
    /** 公司电话*/
    private String tel;
    /** 传真*/
    private String fax;
    /** 联系人邮箱*/
    private String mailAddress;
    /** 联系人*/
    private String contactPerson;
    /** 职务*/
    private String post;
    /** 手机*/
    private String mobile;
    /**
     * @return the agentNum
     */
    public String getAgentNum() {
        return agentNum;
    }
    /**
     * @param agentNum the agentNum to set
     */
    public void setAgentNum(String agentNum) {
        this.agentNum = agentNum;
    }
    /**
     * @return the agentName
     */
    public String getAgentName() {
        return agentName;
    }
    /**
     * @param agentName the agentName to set
     */
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    /**
     * @return the mainBusiness
     */
    public String getMainBusiness() {
        return mainBusiness;
    }
    /**
     * @param mainBusiness the mainBusiness to set
     */
    public void setMainBusiness(String mainBusiness) {
        this.mainBusiness = mainBusiness;
    }
    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }
    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * @return the signDate
     */
    public String getSignDate() {
        return signDate;
    }
    /**
     * @param signDate the signDate to set
     */
    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }
    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }
    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }
    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    /**
     * @return the geographyArea
     */
    public String getGeographyArea() {
        return geographyArea;
    }
    /**
     * @param geographyArea the geographyArea to set
     */
    public void setGeographyArea(String geographyArea) {
        this.geographyArea = geographyArea;
    }
    /**
     * @return the tel
     */
    public String getTel() {
        return tel;
    }
    /**
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }
    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }
    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }
    /**
     * @return the mailAddress
     */
    public String getMailAddress() {
        return mailAddress;
    }
    /**
     * @param mailAddress the mailAddress to set
     */
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }
    /**
     * @return the contactPerson
     */
    public String getContactPerson() {
        return contactPerson;
    }
    /**
     * @param contactPerson the contactPerson to set
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    /**
     * @return the post
     */
    public String getPost() {
        return post;
    }
    /**
     * @param post the post to set
     */
    public void setPost(String post) {
        this.post = post;
    }
    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }
    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
	/**
	 * @return the dept
	 */
	public String getDept() {
		return dept;
	}
	/**
	 * @param dept the dept to set
	 */
	public void setDept(String dept) {
		this.dept = dept;
	}
	/**
	 * @return the businessDirector
	 */
	public String getBusinessDirector() {
		return businessDirector;
	}
	/**
	 * @param businessDirector the businessDirector to set
	 */
	public void setBusinessDirector(String businessDirector) {
		this.businessDirector = businessDirector;
	}

}
