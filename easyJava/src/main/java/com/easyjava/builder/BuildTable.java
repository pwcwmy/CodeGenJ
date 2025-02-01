package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.JsonUtils;
import com.easyjava.utils.PropertiesUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildTable {
    // 引入日志对象
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    private static Connection conn = null;

    // 定义几个关键的数据库sql
    private static String SQL_SHOW_TABLE_STATUS = "show table status";
    private static String SQL_SHOW_TABLE_FIELDS = "show full fields from %s";
    private static String SQL_SHOW_TABLE_INDEX = "show index from %s";


    // 初始化类时连接数据库
    static {
        String driverName = PropertiesUtils.getProperty("db.driver.name");
        String url = PropertiesUtils.getProperty("db.url");
        String username = PropertiesUtils.getProperty("db.username");
        String password = PropertiesUtils.getProperty("db.password");
        try {
            Class.forName(driverName); // 使用该静态方法的目的是为了动态加载类对象
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            logger.error("数据库连接失败", e);
        }
    }

    public static void getTables() {
        PreparedStatement ps = null;
        ResultSet tableResult = null;
        List<TableInfo> tableInfoList = new ArrayList<>();
        try {
            ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            tableResult = ps.executeQuery();
            while (tableResult.next()) {
                String tableName = tableResult.getString("NAME");
                String comment = tableResult.getString("comment");
                TableInfo tableInfo = new TableInfo();
                String beanName = tableName;
                if (Constants.IGNORE_TABLE_PREFIX) {
                    beanName = tableName.substring(beanName.indexOf("_") + 1);
                }
                beanName = processFiled(beanName, true);
                // 集中set, 常见bug: 漏set字段
                // 1. 设置基本属性
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(comment);
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_PARAMS); // 示例：UserInfoQuery
                // 2. 设置具体字段信息
                tableInfo.setFieldList(readFieldInfo(tableInfo));
                // 3. 读取索引并设置tableInfo的keyIndexMap
                readKeyIndexInfo(tableInfo);
                logger.info("tableInfo: {}", JsonUtils.convertObject2Json(tableInfo));
                // 检验set是否漏字段
                // logger.info("表: {}， 备注: {}, JavaBean: {}, JavaParamBean: {}", tableInfo.getTableName(), tableInfo.getComment(), tableInfo.getBeanName(), tableInfo.getBeanParamName());
            }
        } catch (Exception e) {
            logger.error("读取表信息失败", e);
        } finally {
            if (tableResult != null) {
                try {
                    tableResult.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 读取字段信息
     * @param tableInfo
     * @return
     */
    private static List<FieldInfo> readFieldInfo(TableInfo tableInfo) {
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet fieldResult = null;
        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS, tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            while (fieldResult.next()) {
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfoList.add(fieldInfo); // 先放进来再说
                String fieldName = fieldResult.getString("Field");
                String type = fieldResult.getString("Type");
                String extra = fieldResult.getString("Extra");
                String comment = fieldResult.getString("Comment");
                // type只需varchar(10)前面的varchar
                if (type.indexOf("(") > 0) {
                    type = type.substring(0, type.indexOf("("));
                }
                // 集中set
                fieldInfo.setFieldName(fieldName);
                fieldInfo.setComment(comment);
                fieldInfo.setPropertyName(processFiled(fieldName, false));
                fieldInfo.setSqlType(type);
                fieldInfo.setJavaType(processJavaType(type));
                fieldInfo.setAutoIncrement("auto_increment".equalsIgnoreCase(extra));
                // if-else 简化写法
                tableInfo.setHaveDate(ArrayUtils.contains(Constants.SQL_DATE_TYPES, type));
                tableInfo.setHaveDatetime(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type));
                tableInfo.setHaveBigDecimal(ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES, type));

//                logger.info("fieldName: {}, comment: {}, propertyName: {}, sqlType: {}, javaType: {}, autoIncrement: {}",
//                        fieldInfo.getFieldName(), fieldInfo.getComment(), fieldInfo.getPropertyName(), fieldInfo.getSqlType(), fieldInfo.getJavaType(), fieldInfo.getAutoIncrement());
            }
        } catch (SQLException e) {
            logger.error("获取具体字段信息失败", e);
        } finally {
            if (fieldResult != null) {
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // 这个这里不能关，否则最外层就没用了
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return fieldInfoList;
    }

    /**
     * 读取索引信息
     * @param tableInfo
     * @return
     */
    private static List<FieldInfo> readKeyIndexInfo(TableInfo tableInfo) {
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet fieldResult = null;
        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX, tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            while (fieldResult.next()) {
                String keyName = fieldResult.getString("key_name");
                String columnName = fieldResult.getString("column_name");
                Integer nonUnique = fieldResult.getInt("non_unique");
                if (nonUnique == 1) { // 1:普通索引, 跳过，只处理0表示的唯一索引
                    continue;
                }
                // 这里逻辑对吗
                // 先去拿，null就创建，接着根据columnName往里面添加fieldInfo
                List<FieldInfo> keyFieldList = tableInfo.getKeyIndexMap().get(keyName);
                if (null == keyFieldList) {
                    keyFieldList = new ArrayList<>();
                    tableInfo.getKeyIndexMap().put(keyName, keyFieldList);
                }
                for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                    if (fieldInfo.getFieldName().equals(columnName)) {
                        keyFieldList.add(fieldInfo);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("获取索引信息失败", e);
        } finally {
            if (fieldResult != null) {
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return fieldInfoList;
    }



    private static String processFiled(String field, Boolean upperCaseFirstLetter) {
        StringBuffer sb = new StringBuffer();
        String[] fields = field.split("_");
        sb.append(upperCaseFirstLetter ? StringUtils.upperCaseFirstLetter(fields[0]) : fields[0]);

        for (int i = 1; i < fields.length; i++) {
            sb.append(StringUtils.upperCaseFirstLetter(fields[i]));
        }
        return sb.toString();
    }

    private static String processJavaType(String type) {
        if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPES, type)) {
            return "Integer";
        } else if (ArrayUtils.contains(Constants.SQL_LONG_TYPES, type)) {
            return "Long";
        } else if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, type)) {
            return "String";
        } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
            return "Date";
        } else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES, type)) {
            return "BigDecimal";
        } else {
            throw new RuntimeException("无法识别的类型：" + type);
        }
    }
}
