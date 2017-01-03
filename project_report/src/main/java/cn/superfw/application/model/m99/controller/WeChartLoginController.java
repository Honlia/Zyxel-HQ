package cn.superfw.application.model.m99.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superfw.application.config.SystemConfig;
import cn.superfw.framework.web.BaseController;
import cn.superfw.framework.wechart.pojo.SNSUserInfo;
import cn.superfw.framework.wechart.pojo.WeixinOauth2Token;
import cn.superfw.framework.wechart.util.AdvancedUtil;

/**
 * 微信授权网页登录控制器
 * 
 * @author chenchao
 *
 */
@Controller
public class WeChartLoginController extends BaseController {

    /**
     * 微信网页授权登录认证
     * 
     * @param code
     * @param state
     * @return
     */
	@RequestMapping(value = "/code", method = RequestMethod.GET)
    @ResponseBody
    public SNSUserInfo oAuth2Authentication(
            @RequestParam(value = "code", required = true) String code,
            @RequestParam(value = "state", required = false) String state) {

        // 丛配置信息中获取微信开发者信息
    	String appid = SystemConfig.WECHART_APPID.value();
        String appsecret = SystemConfig.WECHART_APPSECRET.value();
    	
        // 通过code换取access_token
    	WeixinOauth2Token oauth2Token = AdvancedUtil.getOauth2AccessToken(appid, appsecret, code);
    	
    	// 获取access_token失败的情况
    	if (oauth2Token == null) {
    		return null;
    	}
    	
    	// 通过access_token拉取微信用户信息
    	SNSUserInfo snsUserInfo = AdvancedUtil.getSNSUserInfo(oauth2Token.getAccessToken(), oauth2Token.getOpenId());
    	
    	// UVO创建等
    	// TODO
        
    	return snsUserInfo;

    }

}
