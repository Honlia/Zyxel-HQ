package cn.superfw.application.model.m99.dto;

import java.io.Serializable;

import cn.superfw.application.domain.Account;

public class AccountViewDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public AccountViewDTO() {
		super();
		this.account = new Account();
		this.roles = "";
	}

	private Account account;
	private String roles;

	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
}
