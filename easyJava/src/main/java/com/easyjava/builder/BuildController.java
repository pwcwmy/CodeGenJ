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

public class BuildController {
    public static final Logger logger = LoggerFactory.getLogger(BuildController.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_CONTROLLER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String beanName = tableInfo.getBeanName();
        String lowerFirstBeanName = StringUtils.lowerCaseFirstLetter(beanName);
        String className = beanName + Constants.SUFFIX_CONTROLLER;
        File queryFile = new File(folder, className + ".java");
        OutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        // 关闭流的原则：先创建的后关闭
        try {
            out = new FileOutputStream(queryFile);
            osw = new OutputStreamWriter(out, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write("package " + Constants.PACKAGE_CONTROLLER + ";\n\n");
            bw.write("import java.util.List;\n\n");

            String returnListName = "List<" + beanName+ ">";
            String parameterListName = "List<" + beanName + ">";
            String returnPaginationResultVOName = "PaginationResultVO<" + beanName + ">";
            String beanQueryName = beanName + Constants.SUFFIX_BEAN_QUERY;
            String beanServiceName = beanName + Constants.SUFFIX_SERVICE;
            String lowerFirstBeanServiceName = StringUtils.lowerCaseFirstLetter(beanServiceName);

            //import com.easyjava.entity.po.UserInfo;
            //import com.easyjava.entity.query.UserInfoQuery;
            //import com.easyjava.entity.vo.ResponseVO;
            //import com.easyjava.service.UserInfoService;
            //import org.springframework.web.bind.annotation.RequestMapping;
            //import org.springframework.web.bind.annotation.RestController;
            bw.write("import " + Constants.PACKAGE_PO + "." + beanName + ";\n");
            bw.write("import " + Constants.PACKAGE_QUERY + "." + beanQueryName + ";\n");
            bw.write("import " + Constants.PACKAGE_VO + ".ResponseVO;\n");
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + beanServiceName + ";\n");
            bw.write("import org.springframework.web.bind.annotation.RequestMapping;\n");
            bw.write("import org.springframework.web.bind.annotation.RestController;\n");

            // import javax.annotation.Resource;
            bw.write("import javax.annotation.Resource;\n\n");

            // 注释
            BuildComment.createClassComment(bw, tableInfo.getComment() + Constants.SUFFIX_CONTROLLER);

            // @RestController
            bw.write("@RestController\n");
            bw.write("@RequestMapping(\"" + lowerFirstBeanName + "\")\n");
            bw.write("public class " + className + " extends AGlobalExceptionHandlerController {\n");

            // @Resource
            // private UserInfoService userInfoService;
            bw.write("\t@Resource\n");
            bw.write("\tprivate " + beanServiceName + " " + lowerFirstBeanServiceName + ";\n");


            // /**
            //	 * 根据条件分页查询 三合一（selectList selectCount selectByPage)
            //	 */
            //	List<UserInfo> findListByParam(UserInfoQuery query);
            BuildComment.createMethodComment(bw, "根据条件分页查询");
            bw.write("\t@RequestMapping(\"loadDataByPage\")\n");
            bw.write("\tpublic ResponseVO loadDataByPage(" + beanQueryName + " query) {\n");
            bw.write("\t\treturn getSuccessResponseVO(" + lowerFirstBeanServiceName + ".selectListByPage(query));\n");
            bw.write("\t}\n\n");

            //  /**
            //     * 新增
            //     */
            //    Long insert(UserInfo userInfo) {
            //      return this.userInfoMapper.insert(userInfo);
            String BEAN = "bean";
            BuildComment.createMethodComment(bw, "新增");
            bw.write("\t@RequestMapping(\"insert\")\n");
            bw.write("\tpublic ResponseVO insert(" + beanName + " " + BEAN + ") {\n");
            bw.write("\t\t" + lowerFirstBeanServiceName + ".insert(" + BEAN + ");\n");
            bw.write("\t\treturn getSuccessResponseVO(null);\n");
            bw.write("\t}\n\n");

            //    /**
            //     * 批量新增
            //     */
            //    Long insertBatch(UserInfo userInfo);
            BuildComment.createMethodComment(bw, "批量新增");
            bw.write("\t@RequestMapping(\"insertBatch\")\n");
            bw.write("\tpublic ResponseVO insertBatch(" + parameterListName +  " list) {\n");
            bw.write("\t\t" + lowerFirstBeanServiceName + ".insertBatch(list);\n");
            bw.write("\t\treturn getSuccessResponseVO(null);\n");
            bw.write("\t}\n\n");

            //    /**
            //     * 批量新增或修改
            //     */
            //    Long insertOrUpdateBatch(UserInfo userInfo);
            BuildComment.createMethodComment(bw, "批量新增或修改");
            bw.write("\t@RequestMapping(\"insertOrUpdateBatch\")\n");
            bw.write("\tpublic ResponseVO insertOrUpdateBatch(" + parameterListName +  " list) {\n");
            bw.write("\t\t" + lowerFirstBeanServiceName + ".insertOrUpdateBatch(list);\n");
            bw.write("\t\treturn getSuccessResponseVO(null);\n");
            bw.write("\t}\n\n");

            // 先用唯一索引
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyIndexfieldInfoList = entry.getValue();
                int index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder paramName = new StringBuilder();
                StringBuilder mapperParamName = new StringBuilder();
                for (FieldInfo fieldInfo : keyIndexfieldInfoList) {
                    ++index;
                    methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    // String userId
                    paramName.append("String ").append(fieldInfo.getPropertyName());
                    mapperParamName.append(fieldInfo.getPropertyName());
                    // 联合查询
                    if (index < keyIndexfieldInfoList.size()) {
                        methodName.append("And");
                        paramName.append(", ");
                        mapperParamName.append(", ");
                    }
                }
                String selectName = "selectBy" + methodName;
                String updateName = "updateBy" + methodName;
                String deleteName = "deleteBy" + methodName;

                BuildComment.createMethodComment(bw, "根据" + methodName + "查询");
                bw.write("\t@RequestMapping(\"" + selectName + "\")\n");
                bw.write("\tpublic ResponseVO " + selectName + " (" + paramName + ") {\n");
                // return getSuccessResponseVO(userInfoService.selectById());
                bw.write("\t\treturn getSuccessResponseVO(" + lowerFirstBeanServiceName +"." + selectName + "(" +  mapperParamName + "));\n");
                bw.write("\t}\n\n");

                // 更新多传一个Bean xml中bean是小写
                // @Param("bean") T t
                BuildComment.createMethodComment(bw, "根据" + methodName + "更新");
                bw.write("\t@RequestMapping(\"" + updateName + "\")\n");
                bw.write("\tpublic  ResponseVO updateBy" + methodName + " (" + beanName + " " + BEAN +", " + paramName + ") {\n");
                //  userInfoService.updateByUserId(userInfo, userId);
                //  return getSuccessResponseVO(null);
//                bw.write("\t\treturn this." + lowerFirstBeanMapperName + ".updateBy" + methodName + "(" + lowerFirstBeanName + ", " + mapperParamName + ");\n");
                bw.write("\t\t" + lowerFirstBeanServiceName + "." + updateName + "(" + BEAN + ", " + mapperParamName + ");\n");
                bw.write("\t\treturn getSuccessResponseVO(null);\n");
                bw.write("\t}\n\n");

                BuildComment.createMethodComment(bw, "根据" + methodName + "删除");
                bw.write("\t@RequestMapping(\"" + deleteName + "\")\n");
                bw.write("\tpublic  ResponseVO deleteBy" + methodName + " (" + paramName + ") {\n");
//                bw.write("\t\treturn this." + lowerFirstBeanMapperName + ".deleteBy" + methodName + "(" + mapperParamName + ");\n");
                bw.write("\t\t" + lowerFirstBeanServiceName + "." + deleteName + "(" + mapperParamName + ");\n");
                bw.write("\t\treturn getSuccessResponseVO(null);\n");
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
