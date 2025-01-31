package com.easyjava.bean;

import com.easyjava.utils.PropertiesUtils;

public class Constants {
    /**
     * 是否忽略表前缀
     */
    public static Boolean IGNORE_TABLE_PREFIX;
    /**
     * 生成的参数bean名称后缀
     */
    public static String SUFFIX_BEAN_PARAMS;

    static {
        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getProperty("ignore.table.prefix"));
        SUFFIX_BEAN_PARAMS = PropertiesUtils.getProperty("suffix.bean.params");
    }

    public static final String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};

    public static final String[] SQL_DATE_TYPES = new String[]{"date"};

    public static final String[] SQL_DECIMAL_TYPES = new String[]{"decimal", "double", "float"};

    public static final String[] SQL_STRING_TYPES = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};

    public static final String[] SQL_INTEGER_TYPES = new String[]{"int", "tinyint"};

    public static final String[] SQL_LONG_TYPES = new String[]{"bigint"};
}
