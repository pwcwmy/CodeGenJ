package com.easyjava.utils;

public class StringUtils {
    /**
     * 单词首字母转大写
     * @param field
     * @return
     */
    public static String upperCaseFirstLetter(String field) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(field)) {
            return field;
        }
        return field.substring(0, 1).toUpperCase() + field.substring(1);
    }
    /**
     * 单词首字母转小写
     * @param field
     * @return
     */
    public static String lowerCaseFirstLetter(String field) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(field)) {
            return field;
        }
        return field.substring(0, 1).toLowerCase() + field.substring(1);
    }

    public static void main(String[] args) {
        System.out.println(upperCaseFirstLetter("hello"));
        System.out.println(lowerCaseFirstLetter("Hello"));
    }
}
