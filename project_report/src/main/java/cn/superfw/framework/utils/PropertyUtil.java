package cn.superfw.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PropertyUtil {

    private static final Logger log = LoggerFactory.getLogger(PropertyUtil.class);

    public static final String DEFAULT_PROPERTY_FILE = "system.properties";

    private static TreeMap<String, String> props = new TreeMap<String, String>();

    static {
        load(DEFAULT_PROPERTY_FILE);
    }

    public static String getProperty(String key, String defaultValue) {
        String result = props.get(key);
        if (result == null) {
            return defaultValue;
        }
        return result;
    }

    public static String getProperty(String key) {
        String result = props.get(key);
        if (result == null) {
            return "";
        }
        return result;
    }


    @SuppressWarnings({ "unused", "rawtypes" })
	private static void load(String name) {
        StringBuilder key = new StringBuilder();
        Properties p = readPropertyFile(name);
        for (Map.Entry e : p.entrySet()) {
            props.put((String) e.getKey(), (String) e.getValue());
        }
    }


    private static Properties readPropertyFile(String name) {

        InputStream is = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(name);
        if (is == null) {
            is = PropertyUtil.class.getResourceAsStream("/" + name);
        }

        Properties p = new Properties();
        try {
            try {
                p.load(is);
            } catch (NullPointerException e) {
                System.err.println("!!! PANIC: Cannot load " + name + " !!!");
            } catch (IOException e) {
                System.err.println("!!! PANIC: Cannot load " + name + " !!!");
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return p;
    }

}
