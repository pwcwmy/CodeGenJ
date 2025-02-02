package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BuildPo {
    public static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_PO);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File poFile = new File(folder, tableInfo.getBeanName() + ".java");
        OutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        // 关闭流的原则：先创建的后关闭
        try {
            out = new FileOutputStream(poFile);
            osw = new OutputStreamWriter(out, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write("package " + Constants.PACKAGE_PO + ";\n\n");
            bw.write("import java.io.Serializable;\n");
            if (tableInfo.getHaveDate() || tableInfo.getHaveDatetime()) {
                bw.write("import java.util.Date;\n");
                bw.write(Constants.BEAN_DATE_FORMAT_CLASS + "\n");
                bw.write(Constants.BEAN_DATE_PARSE_CLASS + "\n");
                // 只添加一次DateUtils DateTimePatternEnum
                bw.write("import " + Constants.PACKAGE_ENUMS + ".DateTimePatternEnum;\n");
                bw.write("import " + Constants.PACKAGE_UTILS + ".DateUtils;\n");
            }
            if (tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;\n");
            }

            // 判断是否有ignore_json 属性
            Boolean haveIgnoreFields = false;
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (ArrayUtils.contains(Constants.IGNORE_BEAN2JSON_FIELD.split(","), fieldInfo.getPropertyName())) {
                    haveIgnoreFields = true;
                    break;
                }
            }
            if (haveIgnoreFields) {
                bw.write(Constants.IGNORE_BEAN2JSON_CLASS + "\n");
            }

            bw.newLine();
            BuildComment.createClassComment(bw, tableInfo.getComment());
            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {\n");

            // 开始生成类的各个字段属性
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                BuildComment.createFieldComment(bw, fieldInfo.getComment());
                // 添加@JsonIgnore注解, ignore的字段可能有多个，逗号分割
                if (ArrayUtils.contains(Constants.IGNORE_BEAN2JSON_FIELD.split(","), fieldInfo.getPropertyName())) {
                    bw.write("\t" + Constants.IGNORE_BEAN2JSON_EXPRESSION + "\n");
                }
                // 添加日期序列化注解
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, "yyyy-MM-dd") + "\n");
                    bw.write("\t" + String.format(Constants.BEAN_DATE_PARSE_EXPRESSION, "yyyy-MM-dd") + "\n");
                }
                // 添加日期反序列化注解
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, "yyyy-MM-dd HH:mm:ss") + "\n");
                    bw.write("\t" + String.format(Constants.BEAN_DATE_PARSE_EXPRESSION, "yyyy-MM-dd HH:mm:ss") + "\n");
                }
                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";\n\n");
            }

            // 开始生成get set 方法
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                // 对特殊的Boolean is_deleted getDeleted setDeleted
                if (fieldInfo.getFieldName().startsWith("is_")) { // 数据库没有bool，所以这里不判断bool
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
            logger.error("创建po失败", e);
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
