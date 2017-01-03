package cn.superfw.framework.utils;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

    public static String getPathInfo(ServletRequest request) {
        if (request == null) {
            return null;
        }

        return ((HttpServletRequest) request).getRequestURI().replaceFirst(
            ((HttpServletRequest) request).getContextPath(), "");
    }

    public static ServletContext getServletContext(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        return request.getSession(true).getServletContext();
    }

    public static boolean isChanged(ServletRequest request) {

        if (request == null) {
            return true;
        }

        String pathInfo = getPathInfo(request);

        String prevPathInfo = (String) request.getAttribute("PREV_PATH_INFO");
        if (prevPathInfo == null
            || pathInfo == null
            || !toCompareStr(prevPathInfo).equals(toCompareStr(pathInfo))) {
            return true;
        }

        return false;
    }

    private static String toCompareStr(String str) {
        try {
            int beginIndex = str.indexOf('/') + 1;
            int endIndex = str.indexOf('/', beginIndex);
            return str.substring(beginIndex, endIndex);
        } catch (IndexOutOfBoundsException e) {
            return str;
        }
    }

    public static String getSessionHash(HttpServletRequest req) {
        if (req == null) {
            return null;
        }
        return Md5Util.hashAsHexString(req.getSession(true).getId());
    }

    public static String dumpRequest(HttpServletRequest req) {
        return dumpRequestParameters(req) + " , " + dumpRequestAttributes(req);
    }

    @SuppressWarnings("rawtypes")
    public static String dumpRequestAttributes(HttpServletRequest req) {
        if (req == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(" RequestAttributes {");
        Enumeration enumeration = req.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            Object value = req.getAttribute(key);
            builder.append(key);
            builder.append(" = ");
            builder.append(value);
            if (enumeration.hasMoreElements()) {
                builder.append(" , ");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    @SuppressWarnings("rawtypes")
    public static String dumpRequestParameters(HttpServletRequest req) {
        if (req == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(" RequestParameters {");
        Enumeration enumeration = req.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String[] values = req.getParameterValues(key);
            for (int i = 0; i < values.length; i++) {
                    builder.append(key);
                    builder.append("[");
                    builder.append(i);
                    builder.append("] = ");
                    builder.append(values[i]);
                    if (i < values.length - 1) {
                        builder.append(" , ");
                    }
            }
            if (enumeration.hasMoreElements()) {
                builder.append(" , ");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    public static String deleteUrlParam(String url, String key) {

        if (url == null || "".equals(url) || key == null || "".equals(key)) {
            return url;
        }

        int start = url.indexOf("?");

        StringBuilder returnUrl = new StringBuilder(url);

        if (start >= 0) {

            String tmp = url.substring(start + 1);

            returnUrl = new  StringBuilder(url.substring(0, start));

            String[] params = tmp.split("&");

            for (int i = 0; i < params.length; i++) {
                String param = params[i];

                if (!param.startsWith(key + "=")) {

                    if (returnUrl.indexOf("?") < 0
                            && !returnUrl.toString().endsWith("?")) {
                        returnUrl.append("?");
                    } else if (!returnUrl.toString().endsWith("&")) {
                        returnUrl.append("&");
                    }

                    returnUrl.append(param);
                }
            }
        }

        return returnUrl.toString();
    }
}
