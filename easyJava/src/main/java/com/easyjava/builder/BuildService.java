package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.JsonUtils;
import com.easyjava.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildService {
    public static final Logger logger = LoggerFactory.getLogger(BuildService.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_SERVICE);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String beanName = tableInfo.getBeanName();
        String lowerFirstBeanName = StringUtils.lowerCaseFirstLetter(beanName);
        String className = beanName + Constants.SUFFIX_SERVICE;
        File queryFile = new File(folder, className + ".java");
        OutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        // 关闭流的原则：先创建的后关闭
        try {
            out = new FileOutputStream(queryFile);
            osw = new OutputStreamWriter(out, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write("package " + Constants.PACKAGE_SERVICE + ";\n\n");
            bw.write("import java.util.List;\n\n");

            String returnListName = "List<" + beanName+ ">";
            String parameterListName = "List<" + beanName + ">";
            String returnPaginationResultVOName = "PaginationResultVO<" + beanName + ">";
            String beanQueryName = beanName + Constants.SUFFIX_BEAN_QUERY;

            // import com.easyjava.entity.po.UserInfo;
            //import com.easyjava.entity.query.UserInfoQuery;
            //import com.easyjava.entity.vo.PaginationResultVO;

            bw.write("import " + Constants.PACKAGE_PO + "." + beanName + ";\n");
            bw.write("import " + Constants.PACKAGE_QUERY + "." + beanQueryName + ";\n");
            bw.write("import " + Constants.PACKAGE_VO + "." + "PaginationResultVO;\n\n");
            BuildComment.createClassComment(bw, tableInfo.getComment() + Constants.SUFFIX_SERVICE);
            bw.write("public interface " + className + " {\n");

            // /**
            //	 * 根据条件查询列表
            //	 */
            //	List<UserInfo> findListByParam(UserInfoQuery query);
            BuildComment.createMethodComment(bw, "根据条件查询列表");
            bw.write("\t" + returnListName + " selectListByParam(" + beanQueryName + " query);\n\n");

            //	/**
            //	 * 根据条件查询数量
            //	 */
            //	List<UserInfo> findCountByParam(UserInfoQuery query);
            BuildComment.createMethodComment(bw, "根据条件查询数量");
            bw.write("\tLong selectCountByParam(" + beanQueryName + " query);\n\n");

            //	/**
            //	 * 分页查询列表
            //	 */
            //	PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query);
            BuildComment.createMethodComment(bw, "分页查询列表");
            bw.write("\t" + returnPaginationResultVOName + " selectListByPage(" + beanQueryName + " query);\n\n");

            //  /**
            //     * 新增
            //     */
            //    Long insert(UserInfo userInfo);
            String BEAN = "bean";
            BuildComment.createMethodComment(bw, "新增");
            bw.write("\tLong insert(" + beanName + " " + BEAN + ");\n\n");

            //    /**
            //     * 批量新增
            //     */
            //    Long insertBatch(UserInfo userInfo);
            BuildComment.createMethodComment(bw, "批量新增");
            bw.write("\tLong insertBatch(" + parameterListName +  " list);\n\n");

            //    /**
            //     * 批量新增或修改
            //     */
            //    Long insertOrUpdateBatch(UserInfo userInfo);
            BuildComment.createMethodComment(bw, "批量新增或修改");
            bw.write("\tLong insertOrUpdateBatch(" + parameterListName +  " list);\n\n");

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
                bw.write("\t " + beanName + " selectBy" + methodName + " (" + paramName + ");\n\n");

                // 更新多传一个Bean xml中bean是小写
                // @Param("bean") T t
                BuildComment.createMethodComment(bw, "根据" + methodName + "更新");
                bw.write("\t Long updateBy" + methodName + " (" + beanName + " " + BEAN +", " + paramName + ");\n\n");

                BuildComment.createMethodComment(bw, "根据" + methodName + "删除");
                bw.write("\t Long deleteBy" + methodName + " (" + paramName + ");\n\n");
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
