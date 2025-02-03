package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildServiceImpl {
    public static final Logger logger = LoggerFactory.getLogger(BuildServiceImpl.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_SERVICES_IMPL);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String beanName = tableInfo.getBeanName();
        String lowerFirstBeanName = StringUtils.lowerCaseFirstLetter(beanName);
        String className = beanName + Constants.SUFFIX_SERVICE_IMPL;
        File queryFile = new File(folder, className + ".java");
        OutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        // 关闭流的原则：先创建的后关闭
        try {
            out = new FileOutputStream(queryFile);
            osw = new OutputStreamWriter(out, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write("package " + Constants.PACKAGE_SERVICES_IMPL + ";\n\n");
            bw.write("import java.util.List;\n\n");

            String returnListName = "List<" + beanName+ ">";
            String returnPaginationResultVOName = "PaginationResultVO<" + beanName + ">";
            String beanQueryName = beanName + Constants.SUFFIX_BEAN_QUERY;
            String beanMapperName = beanName + Constants.SUFFIX_MAPPER;
            String lowerFirstBeanMapperName = StringUtils.lowerCaseFirstLetter(beanMapperName);
            String beanServiceName = beanName + Constants.SUFFIX_SERVICE;

            // import com.easyjava.entity.po.UserInfo;
            //import com.easyjava.entity.query.UserInfoQuery;
            //import com.easyjava.entity.vo.PaginationResultVO;
            bw.write("import " + Constants.PACKAGE_PO + "." + beanName + ";\n");
            bw.write("import " + Constants.PACKAGE_QUERY + "." + beanQueryName + ";\n");
            bw.write("import " + Constants.PACKAGE_VO + "." + "PaginationResultVO;\n");

            // import com.easyjava.mappers.UserInfoMapper;
            bw.write("import " + Constants.PACKAGE_MAPPERS + "." + beanMapperName + ";\n");

            // import com.easyjava.service.UserInfoService;
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + beanServiceName + ";\n");

            // import org.springframework.stereotype.Service;
            bw.write("import org.springframework.stereotype.Service;\n\n");

            // import javax.annotation.Resource;
            bw.write("import javax.annotation.Resource;\n\n");

            // 注释
            BuildComment.createClassComment(bw, tableInfo.getComment() + Constants.SUFFIX_SERVICE_IMPL);

            // @Service
            bw.write("@Service(\"" + beanServiceName + "\")\n");
            bw.write("public class " + className + " implements " + beanServiceName + " {\n");

            // @Resource
            // private UserInfoMapper<UserInfo, UserInfoQuery> useInfoMapper;
            bw.write("\t@Resource\n");
            bw.write("\tprivate " + beanMapperName + "<" + beanName + ", " + beanQueryName + "> " + lowerFirstBeanMapperName + ";\n");


            // /**
            //	 * 根据条件查询列表
            //	 */
            //	List<UserInfo> findListByParam(UserInfoQuery query);
            BuildComment.createMethodComment(bw, "根据条件查询列表");
            bw.write("\t@Override\n");
            bw.write("\tpublic " + returnListName + " selectListByParam(" + beanQueryName + " query) {\n");
            bw.write("\t\treturn this." + lowerFirstBeanMapperName + ".selectList(query);\n");
            bw.write("\t}\n\n");

            //	/**
            //	 * 根据条件查询列表
            //	 */
            //	List<UserInfo> findCountByParam(UserInfoQuery query);
            BuildComment.createMethodComment(bw, "根据条件查询数量");
            bw.write("\t@Override\n");
            bw.write("\tpublic Long selectCountByParam(" + beanQueryName + " query) {\n");
            bw.write("\t\treturn this." + lowerFirstBeanMapperName + ".selectCount(query);\n");
            bw.write("\t}\n\n");

            //	/**
            //	 * 分页查询列表
            //	 */
            //	PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query);
            BuildComment.createMethodComment(bw, "分页查询列表");
            bw.write("\t@Override\n");
            bw.write("\tpublic " + returnPaginationResultVOName + " selectListByPage(" + beanQueryName + " query) {\n");
            bw.write("\t}\n\n");

            //  /**
            //     * 新增
            //     */
            //    Long insert(UserInfo userInfo);
            BuildComment.createMethodComment(bw, "新增");
            bw.write("\t@Override\n");
            bw.write("\tpublic Long insert(" + beanName + " " + lowerFirstBeanName + ") {\n");
            bw.write("\t}\n\n");

            //    /**
            //     * 批量新增
            //     */
            //    Long insertBatch(UserInfo userInfo);
            BuildComment.createMethodComment(bw, "批量新增");
            bw.write("\t@Override\n");
            bw.write("\tpublic Long insertBatch(" + beanName + " " + lowerFirstBeanName + ") {\n");
            bw.write("\t}\n\n");

            //    /**
            //     * 批量新增或修改
            //     */
            //    Long insertOrUpdateBatch(UserInfo userInfo);
            BuildComment.createMethodComment(bw, "批量新增或修改");
            bw.write("\t@Override\n");
            bw.write("\tpublic Long insertOrUpdateBatch(" + beanName + " " + lowerFirstBeanName + ") {\n");
            bw.write("\t}\n\n");

            // 先用唯一索引
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyIndexfieldInfoList = entry.getValue();
                int index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder paramName = new StringBuilder();
                for (FieldInfo fieldInfo : keyIndexfieldInfoList) {
                    ++index;
                    methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    // 联合查询
                    if (index < keyIndexfieldInfoList.size()) {
                        methodName.append("And");
                    }

                    // @Param("userId") String userId
                    paramName.append("String " + fieldInfo.getPropertyName());
                    // 联合查询
                    if (index < keyIndexfieldInfoList.size()) {
                        paramName.append(", ");
                    }
                }
                BuildComment.createMethodComment(bw, "根据" + methodName + "查询");
                bw.write("\t@Override\n");
                bw.write("\tpublic  " + returnListName + " selectBy" + methodName + " (" + paramName + ") {\n");
                bw.write("\t}\n\n");

                // 更新多传一个Bean xml中bean是小写
                // @Param("bean") T t
                BuildComment.createMethodComment(bw, "根据" + methodName + "更新");
                bw.write("\t@Override\n");
                bw.write("\tpublic  Long updateBy" + methodName + " (" + beanName + " " + lowerFirstBeanName +", " + paramName + ") {\n");
                bw.write("\t}\n\n");

                BuildComment.createMethodComment(bw, "根据" + methodName + "删除");
                bw.write("\t@Override\n");
                bw.write("\tpublic  Long deleteBy" + methodName + " (" + paramName + ") {\n");
                bw.write("\t}\n\n");
            }


            // 最后一个} 类闭合
            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建service失败", e);
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
