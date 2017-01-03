package cn.superfw.application.web.uvo;

import java.util.Date;

import cn.superfw.framework.web.UserValueObject;

public class MyUVO extends UserValueObject {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -556991963797002803L;

    /** 用户ID. */
    private Long accountId;

    /** 用户名. */
    private String username;

    /** 密码. */
    private String password;

    /** 账户状态. */
    private String state;

    /** 上次登录时间. */
    private Date lastLogonTime;

    /** 登录次数. */
    private Long logonCount;

	/** 昵称. */
	private String nickname;

    /** 头像. */
    private String headPic;

    /** 职位 1:Sales 2:运作部库管 3：业务部主管 4：MDM主管 5：总经理 */
    private String position;

    private String dept;


	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getLastLogonTime() {
		return lastLogonTime;
	}

	public void setLastLogonTime(Date lastLogonTime) {
		this.lastLogonTime = lastLogonTime;
	}

	public Long getLogonCount() {
		return logonCount;
	}

	public void setLogonCount(Long logonCount) {
		this.logonCount = logonCount;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }


}
