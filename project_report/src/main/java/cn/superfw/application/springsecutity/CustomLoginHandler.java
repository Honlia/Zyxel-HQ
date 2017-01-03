package cn.superfw.application.springsecutity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import cn.superfw.application.web.uvo.MyUVO;
import cn.superfw.framework.CommonContants;
import cn.superfw.framework.web.UserValueObject;

/**
 * 只有Login成功之后才会执行
 * @author chenchao
 *
 */
public class CustomLoginHandler extends
        SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomLoginHandler.class);


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);

        // 这里可以追加开发人员自己的额外处理
        log.info("CustomLoginHandler#onAuthenticationSuccess() is called!");

        String username = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username =((UserDetails)principal).getUsername();
        }

        MyUVO uvo = (MyUVO)UserValueObject.createUserValueObject();
        PopulateUVOUtil.populateUVO(username, uvo);
        request.getSession().setAttribute(CommonContants.UVO, uvo);
    }
}
