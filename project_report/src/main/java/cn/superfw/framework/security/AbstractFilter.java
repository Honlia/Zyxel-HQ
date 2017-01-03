package cn.superfw.framework.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.superfw.framework.exception.SystemException;

public abstract class AbstractFilter<E> implements Filter {

	private static Logger log = LoggerFactory.getLogger(AbstractFilter.class);

	protected FilterConfig config = null;

	@Override
	public void init(FilterConfig config) throws ServletException {
        if (log.isDebugEnabled()) {
            log.debug("setConfig() called.");
        }
        this.config = config;
	}

    /**
     * 获取被注入的控制器实现类
     *
     * @return E 控制器实现类
     */
    @SuppressWarnings("unchecked")
    protected E getHandler() {

        if (log.isDebugEnabled()) {
            log.debug("setHandler() called.");
        }

        WebApplicationContext wac
            = WebApplicationContextUtils
                .getWebApplicationContext(config.getServletContext());


        String handlerId = config.getInitParameter("handler");
        if (handlerId == null || "".equals(handlerId)) {
            if (log.isDebugEnabled()) {
                log.debug("init parameter 'handler' isn't defined or "
                          + "empty");
            }
            handlerId = getDefaultHandlerBeanId();
        }


        if (log.isDebugEnabled()) {
            log.debug("handler bean id = \"" + handlerId + "\"");
        }

        E handler = null;
        try {
            handler = (E) wac.getBean(handlerId, getHandlerClass());
        } catch (NoSuchBeanDefinitionException e) {
            log.error("not found " + handlerId + ". "
                      + "handler bean not defined in Beans definition file.",
                      e);
            throw new SystemException(e, getErrorCode());
        } catch (BeanNotOfRequiredTypeException e) {
            log.error("handler not implemented "
                      + getHandlerClass().toString() + ".",
                      e);
            throw new SystemException(e, getErrorCode());
        } catch (BeansException e) {
            log.error("bean generation failed.", e);
            throw new SystemException(e, getErrorCode());
        }

        return handler;
    }

	@Override
	public abstract void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException;

	@Override
	public void destroy() {
	}

    @SuppressWarnings("rawtypes")
	protected abstract Class getHandlerClass();
    protected abstract String getErrorCode();
    public abstract String getDefaultHandlerBeanId();

    protected void setConfig(FilterConfig config) {
        if (log.isDebugEnabled()) {
            log.debug("setConfig() called.");
        }
        this.config = config;
    }

}
