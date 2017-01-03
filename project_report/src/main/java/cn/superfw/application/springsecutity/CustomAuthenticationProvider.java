package cn.superfw.application.springsecutity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.superfw.application.dao.AccountDAO;
import cn.superfw.application.domain.Account;
import cn.superfw.application.domain.Role;

public class CustomAuthenticationProvider extends
        AbstractUserDetailsAuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    private AccountDAO accountDAO;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        //如果想做点额外的检查,可以在这个方法里处理,校验不通时,直接抛异常即可
        log.info("CustomAuthenticationProvider#additionalAuthenticationChecks() is called!");
    }

    @Override
    protected UserDetails retrieveUser(String username,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        log.info("CustomAuthenticationProvider.retrieveUser() is called!");

        UserDetails userDetails = loadUserByUsername(username);

        String password = "";

        if (authentication.getCredentials() instanceof String) {
            password = (String) authentication.getCredentials();
        }

        if (!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("用户密码不正确");
        }

        return userDetails;
    }


    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        log.info("username: " + username);

        Session session = null;
        UserDetails userDetails = null;

        try {
            session = accountDAO.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Account.class).add(Restrictions.eq("username", username));
            Account account = (Account) criteria.uniqueResult();

            if (account == null) {
                throw new UsernameNotFoundException(username);
            }

            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

            Set<Role> roleSet = account.getRoleSet();

            for (Role role : roleSet) {
                authorities.add(new MyGrantedAuthority(role.getName()));
            }

            userDetails = new User(account.getUsername(),
                    account.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    authorities);
        } finally {
        	if (session != null) {
        		session.close();
        	}
        }

        return userDetails;
    }

}
