package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuildQuery {
    public static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_QUERY);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY;
        File queryFile = new File(folder, className + ".java");
        OutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        // 关闭流的原则：先创建的后关闭
        try {
            out = new FileOutputStream(queryFile);
            osw = new OutputStreamWriter(out, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write("package " + Constants.PACKAGE_QUERY + ";\n\n");
            if (tableInfo.getHaveDate() || tableInfo.getHaveDatetime()) {
                bw.write("import java.util.Date;\n");
                bw.write(Constants.BEAN_DATE_FORMAT_CLASS + "\n");
                bw.write(Constants.BEAN_DATE_PARSE_CLASS + "\n");
                // 为了toString中的日期格式化，只添加一次DateUtils DateTimePatternEnum
                bw.write("import " + Constants.PACKAGE_ENUMS + ".DateTimePatternEnum;\n");
                bw.write("import " + Constants.PACKAGE_UTILS + ".DateUtils;\n");
            }
            if (tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;\n");
            }

            bw.newLine();
            BuildComment.createClassComment(bw, tableInfo.getComment() + "查询对象");
            bw.write("public class " + className + " {\n");

            List<FieldInfo> extendList = new ArrayList<>();
            // 开始生成类的各个字段属性
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                BuildComment.createFieldComment(bw, fieldInfo.getComment());
                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";\n\n");
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY + ";\n\n");
                    FieldInfo fuzzyFieldInfo = new FieldInfo();
                    fuzzyFieldInfo.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY);
                    fuzzyFieldInfo.setJavaType(fieldInfo.getJavaType());
                    extendList.add(fuzzyFieldInfo);
                }
                // 要为fuzzy字段生成set get方法，最简单的是加到tableInfo的fieldList, 但不能边循环边添加！
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    // 查的时候用String 不用Date
                    bw.write("\tprivate String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_DATE_START + ";\n\n");
                    bw.write("\tprivate String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_DATE_END + ";\n\n");
                    FieldInfo timeStartFieldInfo = new FieldInfo();
                    timeStartFieldInfo.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_DATE_START);
                    timeStartFieldInfo.setJavaType("String");
                    extendList.add(timeStartFieldInfo);
                    FieldInfo timeEndFieldInfo = new FieldInfo();
                    timeEndFieldInfo.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_DATE_END);
                    timeEndFieldInfo.setJavaType("String");
                    extendList.add(timeEndFieldInfo);
                }
            }
            // 将fuzzy timeStart timeEnd等字段加入fieldInfoList(只影响get set）
            List<FieldInfo> fieldInfoList = tableInfo.getFieldList();
            fieldInfoList.addAll(extendList);

            // 开始生成get set 方法
            for (FieldInfo fieldInfo : fieldInfoList) {
                // 对特殊的Boolean is_deleted getDeleted setDeleted
                if (null != fieldInfo.getFieldName() && fieldInfo.getFieldName().startsWith("is_")) { // 数据库没有bool，所以这里不判断bool
                    bw.write("\tpublic " + fieldInfo.getJavaType() + " get" + fieldInfo.getPropertyName().substring(2) + "() {\n");
                    bw.write("\t\treturn this." + fieldInfo.getPropertyName() + ";\n");
                    bw.write("\t}\n\n");
                    //set
                    bw.write("\tpublic void set" + fieldInfo.getPropertyName().substring(2) + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ") {\n");
                    bw.write("\t\tthis." + fieldInfo.getPropertyName() + " = " + fieldInfo.getPropertyName() + ";\n");
                    bw.write("\t}\n\n");
                    continue;
                }

                // getUserId
                bw.write("\tpublic " + fieldInfo.getJavaType() + " get" + StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()) + "() {\n");
                bw.write("\t\treturn this." + fieldInfo.getPropertyName() + ";\n");
                bw.write("\t}\n\n");
                //set
                bw.write("\tpublic void set" + StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()) + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ") {\n");
                bw.write("\t\tthis." + fieldInfo.getPropertyName() + " = " + fieldInfo.getPropertyName() + ";\n");
                bw.write("\t}\n\n");
            }

            StringBuffer sb = new StringBuffer(); // 用来构造toString
            int index = 0;
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                ++index;
                String propertyName = fieldInfo.getPropertyName();
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    propertyName = "DateUtils.format(" + propertyName + ", DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())";
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                    propertyName = "DateUtils.format(" + propertyName + ", DateTimePatternEnum.YYYY_MM_DD.getPattern())";
                }
                sb.append("\"" + (index > 1 ? ", " : "") + fieldInfo.getComment() + ": \" + (" + fieldInfo.getPropertyName() + " == null ? \"空\" : " + propertyName + ") " + (index < tableInfo.getFieldList().size() ? " + " : ""));
            }
            // @override toString
            /*@Override
            public String toString() {*/
            bw.write("\t@Override\n");
            bw.write("\tpublic String toString() {\n");
            bw.write("\t\treturn " + sb.toString() + ";\n");
            bw.write("\t}\n");

            // 最后一个} 类闭合
            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建query失败", e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
