package com.easyjava.bean;

public class FieldInfo {
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * bean属性名称
     */
    private String propertyName;
    /**
     * sql中类型
     */
    private String sqlType;
    /**
     * java中类型
     */
    private String javaType;
    /**
     * 字段备注
     */
    private String comment;
    /**
     * 字段是否自增长
     */
    private Boolean isAutoIncrement = false;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }
}
