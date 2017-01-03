package cn.superfw.framework.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import cn.superfw.framework.CommonContants;
import cn.superfw.framework.exception.BrowseException;
import cn.superfw.framework.exception.BusinessException;
import cn.superfw.framework.exception.NoAuthException;
import cn.superfw.framework.exception.ParameterException;
import cn.superfw.framework.exception.SystemException;

public abstract class BaseController {

    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler
    public String exception(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {

        // 根据不同的异常类型进行不同处理
        if (e instanceof BrowseException) {
            setExceptionMessage(request, e, e.getMessage());
        } else if(e instanceof ParameterException) {
            setExceptionMessage(request, e, e.getMessage());
        } else if (e instanceof BusinessException){
            setExceptionMessage(request, e, e.getMessage());
        } else if (e instanceof NoAuthException) {
			response.sendError(403);
        } else if (e instanceof SystemException){
            setExceptionMessage(request, e, "系统出错了，请重新操作一遍或者联系管理员。");
        } else if (e instanceof MaxUploadSizeExceededException){
            setExceptionMessage(request, e, "文件应不大于 " + getFileMB(((MaxUploadSizeExceededException)e).getMaxUploadSize()));
        } else {
            setExceptionMessage(request, e, null);
        }
        return "error";
    }

    private void setExceptionMessage(HttpServletRequest request, Exception e, String message) {

        log.error("-->异常类型：" + message);
        log.error("-->异常详细：" + ExceptionUtils.getFullStackTrace(e));

        request.setAttribute(CommonContants.ERR, message);

    }

    private String getFileMB(long byteFile){
        if(byteFile==0)
           return "0MB";
        long mb=1024*1024;
        return ""+byteFile/mb+"MB";
    }

}
