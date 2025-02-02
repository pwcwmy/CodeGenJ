package com.easyjava.utils;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesUtils {
    private static Properties props = new Properties();
    private static Map<String, String> PORPER_MAP = new ConcurrentHashMap<>();

    static {
        InputStream is = null;
        try {
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");
            // 如果需要读中文配置
            // new InputStream(is, "gbk") 根据idea的file encoding一致
            props.load(is);

            Iterator<Map.Entry<Object, Object>> iterator = props.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> entry = iterator.next();
                PORPER_MAP.put(entry.getKey().toString(), entry.getValue().toString());
            }

        } catch (Exception e) {
//            throw new RuntimeException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getProperty(String key) {
        return PORPER_MAP.get(key);
    }

    public static void main(String[] args) {
        System.out.println(getProperty("db.driver.name"));
    }
}
