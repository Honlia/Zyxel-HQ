package cn.superfw.application.config;

import org.apache.commons.lang.math.NumberUtils;

import cn.superfw.framework.utils.PropertyUtil;

/**
 * 系统配置信息管理
 * 和system.properties中的定义一一对应
 */
public enum SystemConfig {

    /**  UVO类名(全路径) */
    USER_VALUE_OBJECT("uvo"),
    SEARCH_PAGE_SIZE("search.page.size"),
    MAIL_SEND_ENABLED("mail.send.enabled"),
    SYSTEM_MAIL_FROM("system.mail.from"),
    UPDATE_LOG_MAIL("update.log.mail"),

    /** 微信账号信息 */
    WECHART_APPID("wechart.appid"),
    WECHART_APPSECRET("wechart.appsecret");



    SystemConfig(String key) {
        this.key = key;
    }

    public String key() {
        return this.key;
    }

    public String value() {
        String value = PropertyUtil.getProperty(this.key);
        return value;
    }

    public int intValue() {
    	return NumberUtils.toInt(value());
    }


    private final String key;
}
