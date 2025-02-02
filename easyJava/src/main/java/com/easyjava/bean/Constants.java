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
    public static String SUFFIX_BEAN_QUERY;
    /**
     * PACKAGE_BASE
     */
    public static String PACKAGE_BASE;
    /**
     * PACKAGE_PO
     */
    public static String PACKAGE_PO;
    /**
     * PACKAGE_QUERY
     */
    public static String PACKAGE_QUERY;

    /**
     * 下面都是PATH
     * 生成代码的base位置
     */
    public static String PATH_BASE;
    /**
     * java代码的位置
     */
    public static String PATH_JAVA;
    /**
     * 生成Resources的位置
     */
    public static String PATH_RESOURCES;
    /**
     * 生成Po的位置
     */
    public static String PATH_PO;
    /**
     * 生成Query的位置
     */
    public static String PATH_QUERY;
    /**
     * 注释中作者名
     */
    public static String AUTHOR_COMMENT;

    // ctrl+shift+u 切换大小写
//    # 序列化需要忽略的属性
//    ignore.bean.2json.filed=userId
//    ignore.bean.2json.expression=@JsonIgnore
//    ignore.bean.2json.class=import com.fasterxml.jackson.annotation.JsonIgnore;
//
//#日期格式序列化
//    bean.date.format.expression=@JsonFormat(pattern = "%s", timezone = "GMT+8")
//    bean.date.format.class=import com.fasterxml.jackson.annotation.JsonFormat;
//#日期格式反序列化
//    bean.date.parse.expression=@DateTimeFormat(pattern = "%s")
//    bean.date.parse.class=import org.springframework.format.annotation.DateTimeFormat;
    public static String IGNORE_BEAN2JSON_FIELD;
    public static String IGNORE_BEAN2JSON_EXPRESSION;
    public static String IGNORE_BEAN2JSON_CLASS;

    public static String BEAN_DATE_FORMAT_EXPRESSION;
    public static String BEAN_DATE_FORMAT_CLASS;
    public static String BEAN_DATE_PARSE_EXPRESSION;
    public static String BEAN_DATE_PARSE_CLASS;

    public static String PACKAGE_UTILS;
    public static String PATH_UTILS;

    public static String PACKAGE_ENUMS;
    public static String PATH_ENUMS;

//  生成Query时对String类型模糊搜索后缀，对时间类型范围查询
    public static String SUFFIX_BEAN_QUERY_FUZZY;
    public static String SUFFIX_BEAN_QUERY_DATE_START;
    public static String SUFFIX_BEAN_QUERY_DATE_END;
    public static String SUFFIX_MAPPER;

    public static String PACKAGE_MAPPERS;
    public static String PATH_MAPPERS;


    static {
        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getProperty("ignore.table.prefix"));
        SUFFIX_BEAN_QUERY = PropertiesUtils.getProperty("suffix.bean.query");
        PACKAGE_BASE = PropertiesUtils.getProperty("package.base");
        PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getProperty("package.po");
        PACKAGE_QUERY = PACKAGE_BASE + "." + PropertiesUtils.getProperty("package.query");
        PACKAGE_UTILS = PACKAGE_BASE + "." + PropertiesUtils.getProperty("package.utils");
        PACKAGE_ENUMS = PACKAGE_BASE + "." + PropertiesUtils.getProperty("package.enums");
        PACKAGE_MAPPERS = PACKAGE_BASE + "." + PropertiesUtils.getProperty("package.mappers");

        PATH_BASE = PropertiesUtils.getProperty("path.base") + "java/" + PACKAGE_BASE.replace(".", "/");
        PATH_PO = PropertiesUtils.getProperty("path.base") + "java/" + PACKAGE_PO.replace(".", "/");
        PATH_QUERY = PropertiesUtils.getProperty("path.base") + "java/" + PACKAGE_QUERY.replace(".", "/");
        PATH_UTILS = PropertiesUtils.getProperty("path.base") + "java/" + PACKAGE_UTILS.replace(".", "/");
        PATH_ENUMS = PropertiesUtils.getProperty("path.base") + "java/" + PACKAGE_ENUMS.replace(".", "/");
        PATH_MAPPERS = PropertiesUtils.getProperty("path.base") + "java/" + PACKAGE_MAPPERS.replace(".", "/");

        AUTHOR_COMMENT = PropertiesUtils.getProperty("author.comment");

        IGNORE_BEAN2JSON_FIELD = PropertiesUtils.getProperty("ignore.bean2json.field");
        IGNORE_BEAN2JSON_CLASS = PropertiesUtils.getProperty("ignore.bean2json.class");
        IGNORE_BEAN2JSON_EXPRESSION = PropertiesUtils.getProperty("ignore.bean2json.expression");

        BEAN_DATE_FORMAT_EXPRESSION = PropertiesUtils.getProperty("bean.date.format.expression");
        BEAN_DATE_FORMAT_CLASS = PropertiesUtils.getProperty("bean.date.format.class");
        BEAN_DATE_PARSE_EXPRESSION = PropertiesUtils.getProperty("bean.date.parse.expression");
        BEAN_DATE_PARSE_CLASS = PropertiesUtils.getProperty("bean.date.parse.class");

        SUFFIX_BEAN_QUERY_FUZZY = PropertiesUtils.getProperty("suffix.bean.query.fuzzy");
        SUFFIX_BEAN_QUERY_DATE_START = PropertiesUtils.getProperty("suffix.bean.query.date.start");
        SUFFIX_BEAN_QUERY_DATE_END = PropertiesUtils.getProperty("suffix.bean.query.date.end");
        SUFFIX_MAPPER = PropertiesUtils.getProperty("suffix.mapper");

    }

    public static final String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};

    public static final String[] SQL_DATE_TYPES = new String[]{"date"};

    public static final String[] SQL_DECIMAL_TYPES = new String[]{"decimal", "double", "float"};

    public static final String[] SQL_STRING_TYPES = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};

    public static final String[] SQL_INTEGER_TYPES = new String[]{"int", "tinyint"};

    public static final String[] SQL_LONG_TYPES = new String[]{"bigint"};

    public static void main(String[] args) {
       System.out.println(PATH_MAPPERS);

    }
}
