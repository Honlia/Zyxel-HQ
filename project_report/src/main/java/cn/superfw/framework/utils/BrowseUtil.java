package cn.superfw.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BrowseUtil {
    public final static String IE11="MSIE 11.0";
    public final static String IE10="MSIE 10.0";
    public final static String IE9="MSIE 9.0";
    public final static String IE8="MSIE 8.0";
    public final static String IE7="MSIE 7.0";
    public final static String IE6="MSIE 6.0";
    public final static String MAXTHON="Maxthon";
    public final static String QQ="QQBrowser";
    public final static String GREEN="GreenBrowser";
    public final static String SE360="360SE";
    public final static String FIREFOX="Firefox";
    public final static String OPERA="Opera";
    public final static String CHROME="Chrome";
    public final static String SAFARI="Safari";
    public final static String OTHER="其它";


    public static String checkBrowse(String userAgent){
        if(regex(OPERA, userAgent))return OPERA;
        if(regex(CHROME, userAgent))return CHROME;
        if(regex(FIREFOX, userAgent))return FIREFOX;
        if(regex(SAFARI, userAgent))return SAFARI;
        if(regex(SE360, userAgent))return SE360;
        if(regex(GREEN,userAgent))return GREEN;
        if(regex(QQ,userAgent))return QQ;
        if(regex(MAXTHON, userAgent))return MAXTHON;
        if(regex(IE11,userAgent))return IE11;
        if(regex(IE10,userAgent))return IE10;
        if(regex(IE9,userAgent))return IE9;
        if(regex(IE8,userAgent))return IE8;
        if(regex(IE7,userAgent))return IE7;
        if(regex(IE6,userAgent))return IE6;
        return OTHER;
    }
    public static boolean regex(String regex,String str){
        Pattern p =Pattern.compile(regex,Pattern.MULTILINE);
        Matcher m=p.matcher(str);
        return m.find();
    }
}
