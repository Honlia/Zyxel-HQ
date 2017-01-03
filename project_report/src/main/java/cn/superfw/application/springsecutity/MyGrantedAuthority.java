package cn.superfw.application.springsecutity;

import org.springframework.security.core.GrantedAuthority;

public class MyGrantedAuthority implements GrantedAuthority {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4371480056144474427L;

    private String authority;

    public MyGrantedAuthority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

}
