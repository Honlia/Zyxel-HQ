package cn.superfw.application.springsecutity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.superfw.application.dao.AccountDAO;
import cn.superfw.application.domain.Account;
import cn.superfw.application.domain.Role;

public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private SecurityResourceManager securityResourceManager;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

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
        securityResourceManager.refreshSecurityResource();
        return userDetails;
    }

}
