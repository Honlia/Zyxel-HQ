package cn.superfw.application.springsecutity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.superfw.application.dao.ResourceDAO;
import cn.superfw.application.domain.Resource;
import cn.superfw.application.domain.Role;

/**
 * 加载资源与权限的对应关系
 *
 * 该过滤器的主要作用就是通过spring著名的IoC生成securityMetadataSource。
 * securityMetadataSource相当于本包中自定义的MyInvocationSecurityMetadataSourceService。
 * 该MyInvocationSecurityMetadataSourceService的作用提从数据库提取权限和资源，装配到HashMap中，
 * 供Spring Security使用，用于权限校验。
 *
 * @author chenchao
 */
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static final Logger log = LoggerFactory.getLogger(MySecurityMetadataSource.class);

    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
    private RequestMatcher pathMatcher;
    private ResourceDAO resourceDAO;

    /**
     * 构造函数
     * 通过Spring启动,总用是注入SecurityResourceDao和加载资源权限元数据
     *
     * @param securityResourceDao
     */
    public MySecurityMetadataSource(ResourceDAO resourceDAO) {
        this.resourceDAO = resourceDAO;
        loadResourceDefine();
    }

    /**
     * 返回所请求资源所需要的权限
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        Iterator<String> it = resourceMap.keySet().iterator();
        while (it.hasNext()) {
            String resURL = it.next();
            pathMatcher = new AntPathRequestMatcher(resURL);
            if (pathMatcher.matches(((FilterInvocation) object).getRequest())) {
                Collection<ConfigAttribute> returnCollection = resourceMap
                        .get(resURL);
                return returnCollection;
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<ConfigAttribute>();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }


    /**
     * 加载所有资源与权限的关系
     */
    public void loadResourceDefine() {
        if (resourceMap == null) {
            resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
        } else {
            resourceMap.clear();
        }

        Session session = null;

        try {
            session = resourceDAO.getSessionFactory().openSession();
            @SuppressWarnings("unchecked")
            List<Resource> resourceList = session.createCriteria(Resource.class).list();

            Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();

            for (Resource resource : resourceList) {
                if (resourceMap.get(resource.getResString()) == null) {
                    configAttributes = new ArrayList<ConfigAttribute>();
                    resourceMap.put(resource.getResString(), configAttributes);
                }

                Set<Role> roleSet = resource.getRoleSet();
                for (Role role : roleSet) {
                    // 以权限名封装为Spring的security Object
                    ConfigAttribute configAttribute = new SecurityConfig(role.getName());
                    resourceMap.get(resource.getResString()).add(configAttribute);
                }
            }
        } finally {
        	if (session != null) {
        		session.close();
        	}
        }
        log.info("================ 权限列表 ================");
        log.info(resourceMap.toString());
        log.info("======================================");
    }
}
