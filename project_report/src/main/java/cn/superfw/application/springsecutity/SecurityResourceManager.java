package cn.superfw.application.springsecutity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Service;

import cn.superfw.framework.spring.ApplicationContextHolder;

/**
 * 权限信息管理器（可以刷新权限信息）
 * 每当新用户创建成功后，可以调用一次
 * @author chenchao
 *
 */
@Service
public class SecurityResourceManager {

    private static final Logger log = LoggerFactory.getLogger(SecurityResourceManager.class);

    public void refreshSecurityResource() {
        MySecurityMetadataSource mySecurityMetadataSource =
                ApplicationContextHolder.getBean("mySecurityMetadataSource", MySecurityMetadataSource.class);

        mySecurityMetadataSource.loadResourceDefine();

        FilterSecurityInterceptor filterSecurityInterceptor =
                ApplicationContextHolder.getBean("filterSecurityInterceptor", FilterSecurityInterceptor.class);

        filterSecurityInterceptor.setSecurityMetadataSource(mySecurityMetadataSource);

        log.info("权限资源信息已经成功刷新");
    }
}
