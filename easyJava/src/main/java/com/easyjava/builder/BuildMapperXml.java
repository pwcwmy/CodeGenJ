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
import java.util.Map;

public class BuildMapperXml {
    public static final Logger logger = LoggerFactory.getLogger(BuildMapperXml.class);

    public static final String BASE_COLUMN_LIST = "base_column_list";

    public static final String BASE_QUERY_CONDITION = "base_query_condition";

    public static final String EXTEND_QUERY_CONDITION = "extend_query_condition";

    public static final String QUERY_CONDITION = "query_condition";

    public static final String BASE_RESULT_MAP = "base_result_map";

    /**
     * tableInfo.getFieldList()去除了keyPrimary对应的fieldInfo集合
     */
    public static List<FieldInfo> removePrimaryKeyFieldInfoList;
    /**
     * Map<索引名，keyIndex对应的fieldInfo集合>
     */
    public static Map<String, List<FieldInfo>> keyIndexMap;

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_MAPPERS_XML);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPER;
        File queryFile = new File(folder, className + ".xml");
        OutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        // 关闭流的原则：先创建的后关闭
        try {
            out = new FileOutputStream(queryFile);
            osw = new OutputStreamWriter(out, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n");
            bw.write("        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n");
            bw.write("<mapper namespace=\"" + Constants.PACKAGE_MAPPERS_XML + "." + className + "\">\n");

            // 初始化主键对应的fieldInfo集合，给出removePrimaryKeyFieldInfoList
            initKeyIndexMapAndRemovePrimaryKeyList(tableInfo);

            // 下面主要是数据库field到实体Bean的映射 (column=fieldName->property=propertyName)
            buildField2Bean(tableInfo, bw);

            // 通用结果查询列
            buildBaseColumnList(tableInfo, bw);

            // 基础查询条件
            buildBaseQueryCondition(tableInfo, bw);

            // 扩展查询条件
            buildExtendQueryCondition(tableInfo, bw);

            // 通用查询条件，where聚合，多个include
            buildQueryCondition(tableInfo, bw);

            // 查询列表（集合）
            buildSelectList(tableInfo, bw);

            // 查询数量（集合）
            buildSelectCount(tableInfo, bw);

            // 单条插入
            buildInsert(tableInfo, bw);

            // 单条插入或者更新
            buildInsertOrUpdate(tableInfo, bw);

            // 批量插入
            buildInsertBatch(tableInfo, bw);

            // 批量插入或者更新
            buildInsertOrUpdateBatch(tableInfo, bw);

            // 根据keyIndex查询、更新、删除
            buildRudById(tableInfo, bw);

            // 这是最后的mapper闭合

            bw.write("</mapper>\n");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建" + className + "XML失败", e);
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

    private static void initKeyIndexMapAndRemovePrimaryKeyList(TableInfo tableInfo) {
        removePrimaryKeyFieldInfoList = new ArrayList<>(tableInfo.getFieldList());
        keyIndexMap = tableInfo.getKeyIndexMap();
        for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
            if ("PRIMARY".equals(entry.getKey())) {
                removePrimaryKeyFieldInfoList.removeAll(entry.getValue());
            }
        }
    }

    private static void buildRudById(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
            List<FieldInfo> keyIndexfieldInfoList = entry.getValue();
            int index = 0;
            StringBuilder methodName = new StringBuilder();
            StringBuilder paramName = new StringBuilder();
            for (FieldInfo fieldInfo : keyIndexfieldInfoList) {
                ++index;
                methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                // 联合多字段查询
                if (index < keyIndexfieldInfoList.size()) {
                    methodName.append("And");
                }

                // id = #{id} and name = #{name}
                paramName.append(fieldInfo.getFieldName() + " = #{" + fieldInfo.getPropertyName() +"}");
                // 联合多字段查询
                if (index < keyIndexfieldInfoList.size()) {
                    paramName.append(" and ");
                }
            }
            // 	<!--根据id查询-->
            //	<select id="selectById" resultMap="com.easyjava.entity.po.UserInfo">
            //		SELECT
            //		<include refid="base_column_list"/>
            //		FROM user_info
            //	    where id = #{id}
            //	</select>
            bw.write("\t<!--根据" + methodName + "查询-->\n");
            bw.write("\t<select id=\"selectBy" + methodName + "\" resultMap=\"" + BASE_RESULT_MAP +"\">\n");
            bw.write("\t\tSELECT\n");
            bw.write("\t\t<include refid=\"" + BASE_COLUMN_LIST + "\"/>\n");
            bw.write("\t\tFROM " + tableInfo.getTableName() + " \n");
            bw.write("\t\twhere " + paramName + "\n");
            bw.write("\t</select>\n\n");

            //	<!--根据id修改-->
            //	<update id="updateById" parameterType="com.easyjava.entity.po.UserInfo">
            //		UPDATE user_info
            //		<set>
            //			<if test="bean.companyId != null">
            //				company_id = #{bean.companyId}
            //			</if>
            //		</set>
            //      where
            //	</update>
            bw.write("\t<!--根据" + methodName + "更新-->\n");
            bw.write("\t<update id=\"updateBy" + methodName + "\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">\n");
            bw.write("\t\tUPDATE " + tableInfo.getTableName() + " \n");
            bw.write("\t\t<set>\n");
            for (FieldInfo fieldInfo : removePrimaryKeyFieldInfoList) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() +" = #{bean." + fieldInfo.getPropertyName() +"},\n");
                bw.write("\t\t\t</if>\n");
            }
            bw.write("\t\t</set>\n");
            bw.write("\t\twhere " + paramName + "\n");
            bw.write("\t</update>\n\n");

            //	<!--根据id删除-->
            //	<delete id="deleteById" resultType="java.lang.Long">
            //		DELETE
            //		from user_info
            //	    where id = #{id}
            //	</delete>
            bw.write("\t<!--根据" + methodName + "删除-->\n");
            bw.write("\t<delete id=\"deleteBy" + methodName + "\">\n");
            bw.write("\t\tDELETE\n");
            bw.write("\t\tFROM " + tableInfo.getTableName() + " \n");
            bw.write("\t\twhere " + paramName + "\n");
            bw.write("\t</delete>\n\n");
        }
    }


    private static void buildInsertBatch(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        bw.write("\t<!--批量插入-->\n");
        bw.write("\t<insert id=\"insertBatch\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">\n");
        // 构造 （，，，）
        StringBuilder columns = new StringBuilder();
        for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
            columns.append(fieldInfo.getFieldName()).append(", ");
        }
        String columnList = columns.substring(0, columns.lastIndexOf(", "));
        bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "(" + columnList + ")\n");

        // foreach values()
        bw.write("\t\tVALUES\n");
        bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">\n");
        bw.write("\t\t\t(\n");
        bw.write("\t\t\t");
        int index = 0;
        for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
            ++index;
            bw.write("#{item." + fieldInfo.getPropertyName() + "}");
            if (index < tableInfo.getFieldList().size()) {
                bw.write(", ");
            }
        }
        bw.write("\n\t\t\t)\n");
        bw.write("\t\t</foreach>\n");

        // 最后insert闭合
        bw.write("\t</insert>\n\n");
    }

    private static void buildInsertOrUpdateBatch(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        //<!--批量插入或更新-->
        //	<insert id="insertBatch" parameterType="">
        //		INSERT INTO user_info(,,,)
        //		VALUES
        //		<foreach collection="list" item="item" separator=",">
        //			(
        //			#{item.companyId},
        //			)
        //		</foreach>
        //		ON DUPLICATE key update
        //		nickname = VALUES(nickname),
        //	</insert>
        bw.write("\t<!--批量插入或更新-->\n");
        bw.write("\t<insert id=\"insertOrUpdateBatch\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">\n");
        // 构造 （，，，）
        StringBuilder columns = new StringBuilder();
        for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
            columns.append(fieldInfo.getFieldName()).append(", ");
        }
        String columnList = columns.substring(0, columns.lastIndexOf(", "));
        bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "(" + columnList + ")\n");

        // foreach values()
        bw.write("\t\tVALUES\n");
        bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">\n");
        bw.write("\t\t\t(\n");
        bw.write("\t\t\t");
        int index = 0;
        for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
            ++index;
            bw.write("#{item." + fieldInfo.getPropertyName() + "}");
            if (index < tableInfo.getFieldList().size()) {
                bw.write(", ");
            }
        }
        bw.write("\n\t\t\t)\n");
        bw.write("\t\t</foreach>\n");

        // 主键是不许被更新，这里要予以排除
        // 最后一个field后面不需要逗号
        bw.write("\t\tON DUPLICATE key update\n");
        int index2 = 0;
        for (FieldInfo fieldInfo : removePrimaryKeyFieldInfoList) {
            //		nickname = VALUES(nickname),
            ++ index2;
            bw.write("\t\t" + fieldInfo.getFieldName() +" = VALUES(" + fieldInfo.getFieldName() + ")");
            if (index2 < removePrimaryKeyFieldInfoList.size()) {
                bw.write(",\n");
            } else {
                bw.write("\n");
            }
        }

        // 最后insert闭合
        bw.write("\t</insert>\n\n");
    }

    private static void buildInsertOrUpdate(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        bw.write("\t<!--插入或者更新（匹配有值的字段)-->\n");
        bw.write("\t<insert id=\"insertOrUpdate\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">\n");

        // 如果存在自增长id， 生成selectKey
        FieldInfo autoIncrementFieldInfo = null;
        List<FieldInfo> removeAutoIncrementFieldInfoList = new ArrayList<>(tableInfo.getFieldList());
        for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
            if (null != fieldInfo.getAutoIncrement() && fieldInfo.getAutoIncrement()) {
                autoIncrementFieldInfo = fieldInfo;
                removeAutoIncrementFieldInfoList.remove(fieldInfo);
                break; // TODO: 自增长id只有一个吗？唯一的，自增id必须为键
            }
        }
        if (null != autoIncrementFieldInfo) {
            bw.write("\t<!--存在自增长id-->\n");
            bw.write("\t\t<selectKey keyProperty=\"bean." + autoIncrementFieldInfo.getPropertyName() + "\" resultType=\"" + autoIncrementFieldInfo.getJavaType() + "\" order=\"AFTER\">\n");
            bw.write("\t\t\tSELECT LAST_INSERT_ID()\n");
            bw.write("\t\t</selectKey>\n");
        }

        bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "\n");
        // 第一个trim
        bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        for (FieldInfo fieldInfo : removeAutoIncrementFieldInfoList) {
            // TODO: code review 一般自增id是不需要插入的
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
            bw.write("\t\t\t\t" + fieldInfo.getFieldName() +",\n");
            bw.write("\t\t\t</if>\n");
        }
        bw.write("\t\t</trim>\n");

        // 第二个trim
        bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">\n");
        for (FieldInfo fieldInfo : removeAutoIncrementFieldInfoList) {
            //			<if test="bean.companyId != null" >
            //				#{bean.companyId},
            //			</if>
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
            bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() +"},\n");
            bw.write("\t\t\t</if>\n");
        }
        bw.write("\t\t</trim>\n");

        // duplicate on key update
        // 第三个trim
        // 		ON DUPLICATE key update
        //		<trim prefix="" suffix="" suffixOverrides=",">
        //			<if test="bean.companyId != null">
        //				company_id = VALUES(company_id),
        //			</if>
        //		</trim>
        // TODO: 主键是不许被更新，这里要予以排除
        bw.write("\t\tON DUPLICATE key update\n");
        bw.write("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">\n");
        for (FieldInfo fieldInfo : removePrimaryKeyFieldInfoList) {
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
            bw.write("\t\t\t\t" + fieldInfo.getFieldName() +" = VALUES(" + fieldInfo.getFieldName() +"),\n");
            bw.write("\t\t\t</if>\n");
        }
        bw.write("\t\t</trim>\n");

        // 最后insert闭合
        bw.write("\t</insert>\n\n");
    }

    private static void buildInsert(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        //	<!--插入（匹配有值的字段)-->
        //	<insert id="insert" parameterType="com.easyjava.entity.po.UserInfo">
        //        <selectKey keyProperty="bean.id" resultType="Integer" order="AFTER">
        //            SELECT LAST_INSERT_ID()
        //        </selectKey>
        //        INSERT INTO user_info
        //        <trim prefix="(" suffix=")" suffixOverrides=",">
        //            <if test="bean.companyId != null">
        //                company_id,
        //            </if>
        //        </trim>
        //        <trim prefix="values (" suffix=")" suffixOverrides=",">
        //			<if test="bean.companyId != null" >
        //				#{bean.companyId},
        //			</if>
        //		  </trim>
        //    </insert>
        bw.write("\t<!--插入（匹配有值的字段)-->\n");
        bw.write("\t<insert id=\"insert\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">\n");

        // 如果存在自增长id， 生成selectKey
        FieldInfo autoIncrementFieldInfo = null;
        List<FieldInfo> removeAutoIncrementFieldInfoList = tableInfo.getFieldList();
        for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
            if (null != fieldInfo.getAutoIncrement() && fieldInfo.getAutoIncrement()) {
                autoIncrementFieldInfo = fieldInfo;
                removeAutoIncrementFieldInfoList.remove(fieldInfo);
                break; // TODO: 自增长id只有一个吗？唯一的，自增id必须为键
            }
        }
        if (null != autoIncrementFieldInfo) {
            bw.write("\t<!--存在自增长id-->\n");
            bw.write("\t\t<selectKey keyProperty=\"bean." + autoIncrementFieldInfo.getPropertyName() + "\" resultType=\"" + autoIncrementFieldInfo.getJavaType() + "\" order=\"AFTER\">\n");
            bw.write("\t\t\tSELECT LAST_INSERT_ID()\n");
            bw.write("\t\t</selectKey>\n");
        }

        bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "\n");
        // 第一个trim
        bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        for (FieldInfo fieldInfo : removeAutoIncrementFieldInfoList) {
            // TODO: code review 一般自增id是不需要插入的
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
            bw.write("\t\t\t\t" + fieldInfo.getFieldName() +",\n");
            bw.write("\t\t\t</if>\n");
        }
        bw.write("\t\t</trim>\n");

        // 第二个trim
        bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">\n");
        for (FieldInfo fieldInfo : removeAutoIncrementFieldInfoList) {
            //			<if test="bean.companyId != null" >
            //				#{bean.companyId},
            //			</if>
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
            bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() +"},\n");
            bw.write("\t\t\t</if>\n");
        }
        bw.write("\t\t</trim>\n");

        bw.write("\t</insert>\n\n");
    }

    private static void buildSelectCount(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        //	<select id="selectCount" resultType="java.lang.Integer">
        //		SELECT COUNT(1)
        //		FROM user_info
        //		<include refid="query_condition"/>
        //	</select>
        bw.write("\t<!--查询数量-->\n");
        bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Long\">\n");
        bw.write("\t\tSELECT COUNT(1)\n");
        bw.write("\t\tFROM " + tableInfo.getTableName() + " \n");
        bw.write("\t\t<include refid=\"" + QUERY_CONDITION + "\"/>\n");
        bw.write("\t</select>\n\n");
    }

    private static void buildSelectList(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        //    <!--查询集合-->
        //    <select id="selectList" resultMap="base_result_map">
        //        SELECT
        //        <include refid="base_column_list"></include>
        //        FROM user_info
        //        <include refid="query_condition"></include>
        //        <if test="query.orderBy != null">
        //            order by ${query.orderBy}
        //        </if>
        //        <if test="query.simplePage != null">
        //            limit #{query.simplePage.start}, #{query.simplePage.end}
        //        </if>
        //    </select>
        bw.write("\t<!--查询集合-->\n");
        bw.write("\t<select id=\"selectList\" resultMap=\"" + BASE_RESULT_MAP +"\">\n");
        bw.write("\t\tSELECT\n");
        bw.write("\t\t<include refid=\"" + BASE_COLUMN_LIST + "\"/>\n");
        bw.write("\t\tFROM " + tableInfo.getTableName() + " \n");
        bw.write("\t\t<include refid=\"" + QUERY_CONDITION + "\"/>\n");
        bw.write("\t\t<if test=\"query.orderBy != null\">\n");
        bw.write("\t\torder by ${query.orderBy}\n");
        bw.write("\t\t</if>\n");
        bw.write("\t\t<if test=\"query.simplePage != null\">\n");
        bw.write("\t\tlimit #{query.simplePage.start}, #{query.simplePage.end}\n");
        bw.write("\t\t</if>\n");
        bw.write("\t</select>\n\n");
    }

    private static void buildQueryCondition(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        //<!--通用查询条件，where聚合-->
        //	<sql id="query_condition">
        //		<where>
        //			<include refid=""/>
        //			<include refid=""/>
        //		</where>
        //	</sql>
        bw.write("\t<!--通用查询条件-->\n");
        bw.write("\t<sql id=\"" + QUERY_CONDITION + "\">\n");
        bw.write("\t\t<where>\n");
        bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION + "\"/>\n");
        bw.write("\t\t\t<include refid=\"" + EXTEND_QUERY_CONDITION + "\"/>\n");
        bw.write("\t\t</where>\n");
        bw.write("\t</sql>\n\n");
    }

    private static void buildExtendQueryCondition(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        bw.write("\t<!--扩展查询条件-->\n");
        //	<sql id="extend_query_condition">
        //		<if test="query.companyIdFuzzy != null and query.companyIdFuzzy != ''">
        //			and company_id like concat('%', #{query.companyIdFuzzy}, '%')
        //		</if>
        //	</sql>
        bw.write("\t<sql id=\"" + EXTEND_QUERY_CONDITION + "\">\n");

        for (FieldInfo fieldInfo : tableInfo.getExtendFieldList()) {
            String stringQuery = " and query." + fieldInfo.getPropertyName() + " != ''";
            String andWhere = "";
            if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())) {
                andWhere = "and " + fieldInfo.getFieldName() + " like concat('%', #{query." + fieldInfo.getPropertyName() + "}, '%')";

            } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                // <![CDATA[ and join_time >= str_to_date(#query.joinTimeStart), '%Y-%m-%d']]>
                // <![CDATA[ and join_time < date_sub(str_to_date(#query.joinTimeStart), '%Y-%m-%d'), interval -1 day)]]>
                if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_DATE_START)) {
                    andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " >= str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d')]]>";
                } else if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_DATE_END)) {
                    andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " < date_sub(str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d'), interval -1 day)]]>";
                }

            }
            // 普通的只要判断null， String要多判断一个空串
            bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null" + stringQuery +"\">\n");
            bw.write("\t\t\t" + andWhere + "\n");
            bw.write("\t\t</if>\n");
        }
        bw.write("\t</sql>\n\n");
    }

    private static void buildBaseQueryCondition(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        bw.write("\t<!--基础查询条件-->\n");
        bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION + "\">\n");
        //<if test="query.id != null">
        //  and id = #{query.id}
        //</if>
        for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
            String stringQuery = "";
            if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())) {
                stringQuery = " and query." + fieldInfo.getPropertyName() + " != ''";
            }
            // 普通的只要判断null， String要多判断一个空串
            bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null" + stringQuery +"\">\n");
            bw.write("\t\t\tand " + fieldInfo.getFieldName() + " = #{query." + fieldInfo.getPropertyName() + "}\n");
            bw.write("\t\t</if>\n");
        }
        bw.write("\t</sql>\n\n");
    }

    private static void buildBaseColumnList(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        bw.write("\t<!--通用结果查询列-->\n");
        bw.write("\t<sql id=\"" + BASE_COLUMN_LIST + "\">\n");
        StringBuilder columns = new StringBuilder();
        for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
            columns.append(fieldInfo.getFieldName()).append(", ");
        }
        String columnList = columns.substring(0, columns.lastIndexOf(", "));
        bw.write("\t\t" + columnList + "\n");
        bw.write("\t</sql>\n\n");
    }

    private static void buildField2Bean(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        bw.write("\t<!--实体映射-->\n");
        // type="com.easy.java.po.UserInfo"
        bw.write("\t<resultMap id=\"" + BASE_RESULT_MAP + "\" type=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">\n");

        //<id column="id" property="id"/>
        //<result column="id" property="id"/>
        // 如果主键只有一个，那么用id，多个的话用result
        FieldInfo idField = null;
        Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
        // 主键只有一个的时候获取并赋值给idField
        for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
            if ("PRIMARY".equals(entry.getKey())) {
                List<FieldInfo> fieldInfoList = entry.getValue();
                if (fieldInfoList.size() == 1) {
                    idField = fieldInfoList.get(0);
                    break;
                }
            }
        }
        for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
            bw.write("\t\t<!--" + fieldInfo.getComment() + "-->\n");
            String key = "";
            if (null != idField && fieldInfo.getPropertyName().equals(idField.getPropertyName())) {
                key = "id";
            } else {
                key = "result";
            }
            //<result column="id" property="id"/>
            bw.write("\t\t<" + key + " column=\"" + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>\n");
        }
        bw.write("\t</resultMap>\n\n");
    }
}
