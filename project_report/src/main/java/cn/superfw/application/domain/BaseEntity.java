package cn.superfw.application.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@MappedSuperclass
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public abstract class BaseEntity {

    /** ID. */
    private Long id;

    private Long version;

    /** 插入账户. */
    private String addUser;

    /** 更新账户. */
    private String updUser;

    /** 插入时间. */
    private Date addTime;

    /** 更新时间. */
    private Date updTime;

    /**
     * Set the id.
     *
     * @param id
     *            id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the id.
     *
     * @return id
     */
    @Id
    @GeneratedValue
    public Long getId() {
        return this.id;
    }

    @Version
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Set the 插入账户.
     *
     * @param addUser
     *            插入账户
     */
    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    /**
     * Get the 插入账户.
     *
     * @return 插入账户
     */
    @Column(name="add_user")
    public String getAddUser() {
        return this.addUser;
    }

    /**
     * Set the 更新账户.
     *
     * @param updUser
     *            更新账户
     */
    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }

    /**
     * Get the 更新账户.
     *
     * @return 更新账户
     */
    @Column(name="upd_user")
    public String getUpdUser() {
        return this.updUser;
    }

    /**
     * Set the 插入时间.
     *
     * @param addTime
     *            插入时间
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * Get the 插入时间.
     *
     * @return 插入时间
     */
    @Column(name="add_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getAddTime() {
        return this.addTime;
    }

    /**
     * Set the 更新时间.
     *
     * @param updTime
     *            更新时间
     */
    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    /**
     * Get the 更新时间.
     *
     * @return 更新时间
     */
    @Column(name="upd_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdTime() {
        return this.updTime;
    }

}
