package cn.superfw.application.springsecutity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class MyAccessDecisionManager implements AccessDecisionManager {

    private static final Logger log = LoggerFactory.getLogger(MyAccessDecisionManager.class);

    @Override
    public void decide(Authentication authentication, Object object,
            Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        if(configAttributes == null) {
            return;
        }
        //所请求的资源拥有的角色(一个资源对多个角色)
        Iterator<ConfigAttribute> iterator = configAttributes.iterator();
        while(iterator.hasNext()) {
            ConfigAttribute configAttribute = iterator.next();
            //访问所请求资源所需要的权限
            String needRole = configAttribute.getAttribute().trim();
            //用户所拥有的权限(角色)集合
            Set<String> authorities = new HashSet<>();
            for(GrantedAuthority ga : authentication.getAuthorities()) {
                authorities.add(ga.getAuthority().trim());
            }

            log.info("需要的角色： " + needRole);
            log.info("拥有的角色：" + authorities.toString());

            // 如果所需要的权限包含在集合中
            if (authorities.contains(needRole)) {
                log.info(needRole + " IN " + authorities.toString());
                return;
            } else {
                log.info(needRole + " IS NOT IN " + authorities.toString() + ",尝试进行下一轮匹配.");
            }

        }
        //没有权限    会跳转到login页面
        log.info("权限不足.");
        throw new InsufficientAuthenticationException("权限不足.");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
