package cn.superfw.application.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name="persistent_logins")
public class PersistentLogins implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String username;
	private String series;
	private String token;
	private Date lastUsed ;

	@Column(length=64, nullable=false)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	@Id
	@Column(length=64)
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}

	@Column(length=64, nullable=false)
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastUsed() {
		return lastUsed;
	}
	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}
}
