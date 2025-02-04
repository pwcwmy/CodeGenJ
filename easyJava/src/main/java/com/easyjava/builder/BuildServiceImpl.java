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
            String parameterListName = "List<" + beanName + ">";
            String returnPaginationResultVOName = "PaginationResultVO<" + beanName + ">";
            String beanQueryName = beanName + Constants.SUFFIX_BEAN_QUERY;
            String beanMapperName = beanName + Constants.SUFFIX_MAPPER;
            String lowerFirstBeanMapperName = StringUtils.lowerCaseFirstLetter(beanMapperName);
            String beanServiceName = beanName + Constants.SUFFIX_SERVICE;

            //import com.easyjava.entity.enums.PageSizeEnum;
            //import com.easyjava.entity.po.UserInfo;
            //import com.easyjava.entity.query.SimplePage;
            //import com.easyjava.entity.query.UserInfoQuery;
            //import com.easyjava.entity.vo.PaginationResultVO;
            bw.write("import " + Constants.PACKAGE_ENUMS + ".PageSizeEnum;\n");
            bw.write("import " + Constants.PACKAGE_PO + "." + beanName + ";\n");
            bw.write("import " + Constants.PACKAGE_QUERY + ".SimplePage;\n");
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
            // @Override
            //	public PaginationResultVO<UserInfo> selectListByPage(UserInfoQuery query) {
            //		long totalCount = this.selectCountByParam(query);
            //		long pageSize = query.getPageSize() == null ? PageSizeEnum.SIZE20.getPageSize() : query.getPageSize();
            //		long pageNo = query.getPageNo() == null ? 1 : query.getPageNo();
            //
            //		SimplePage simplePage = new SimplePage(pageNo, totalCount, pageSize);
            //		query.setSimplePage(simplePage);
            //		List<UserInfo> list = this.userInfoMapper.selectList(query);
            //        return new PaginationResultVO<>(totalCount, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getTotalPage(), list);
            //	}
            BuildComment.createMethodComment(bw, "分页查询列表");
            bw.write("\t@Override\n");
            bw.write("\tpublic " + returnPaginationResultVOName + " selectListByPage(" + beanQueryName + " query) {\n");
            bw.write("\t\tlong totalCount = this.selectCountByParam(query);\n");
            bw.write("\t\tlong pageSize = query.getPageSize() == null ? PageSizeEnum.SIZE20.getPageSize() : query.getPageSize();\n");
            bw.write("\t\tlong pageNo = query.getPageNo() == null ? 1 : query.getPageNo();\n");
            bw.write("\t\tSimplePage simplePage = new SimplePage(pageNo, pageSize, totalCount);\n");
            bw.write("\t\tquery.setSimplePage(simplePage);\n");
            bw.write("\t\tList<UserInfo> list = this." + lowerFirstBeanMapperName + ".selectList(query);\n");
            bw.write("\t\treturn new PaginationResultVO<>(totalCount, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getTotalPage(), list);\n");
            bw.write("\t}\n\n");

            //  /**
            //     * 新增
            //     */
            //    Long insert(UserInfo userInfo) {
            //      return this.userInfoMapper.insert(userInfo);
            String BEAN = "bean";
            BuildComment.createMethodComment(bw, "新增");
            bw.write("\t@Override\n");
            bw.write("\tpublic Long insert(" + beanName + " " + BEAN + ") {\n");
            bw.write("\t\treturn this." + lowerFirstBeanMapperName + ".insert(" + BEAN + ");\n");
            bw.write("\t}\n\n");

            //    /**
            //     * 批量新增
            //     */
            //    Long insertBatch(UserInfo userInfo);
            BuildComment.createMethodComment(bw, "批量新增");
            bw.write("\t@Override\n");
            bw.write("\tpublic Long insertBatch(" + parameterListName +  " list) {\n");
            //  if (list == null || list.isEmpty()) {
            //            return 0L;
            //        }
            //  return this.userInfoMapper.insertBatch(list);
            bw.write("\t\tif (list == null || list.isEmpty()) {\n");
            bw.write("\t\t\treturn 0L;\n");
            bw.write("\t\t}\n");
            bw.write("\t\treturn this." + lowerFirstBeanMapperName + ".insertBatch(list);\n");
            bw.write("\t}\n\n");

            //    /**
            //     * 批量新增或修改
            //     */
            //    Long insertOrUpdateBatch(UserInfo userInfo);
            BuildComment.createMethodComment(bw, "批量新增或修改");
            bw.write("\t@Override\n");
            bw.write("\tpublic Long insertOrUpdateBatch(" + parameterListName +  " list) {\n");
            bw.write("\t\tif (list == null || list.isEmpty()) {\n");
            bw.write("\t\t\treturn 0L;\n");
            bw.write("\t\t}\n");
            bw.write("\t\treturn this." + lowerFirstBeanMapperName + ".insertOrUpdateBatch(list);\n");
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
                BuildComment.createMethodComment(bw, "根据" + methodName + "查询");
                bw.write("\t@Override\n");
                bw.write("\tpublic  " + returnListName + " selectBy" + methodName + " (" + paramName + ") {\n");
                // return (List<UserInfo>) this.userInfoMapper.selectByUserId(userId);
                bw.write("\t\treturn this." + lowerFirstBeanMapperName + ".selectBy" + methodName + "(" + mapperParamName + ");\n");
                bw.write("\t}\n\n");

                // 更新多传一个Bean xml中bean是小写
                // @Param("bean") T t
                BuildComment.createMethodComment(bw, "根据" + methodName + "更新");
                bw.write("\t@Override\n");
                bw.write("\tpublic  Long updateBy" + methodName + " (" + beanName + " " + BEAN +", " + paramName + ") {\n");
                bw.write("\t\treturn this." + lowerFirstBeanMapperName + ".updateBy" + methodName + "(" + BEAN + ", " + mapperParamName + ");\n");
                bw.write("\t}\n\n");

                BuildComment.createMethodComment(bw, "根据" + methodName + "删除");
                bw.write("\t@Override\n");
                bw.write("\tpublic  Long deleteBy" + methodName + " (" + paramName + ") {\n");
                bw.write("\t\treturn this." + lowerFirstBeanMapperName + ".deleteBy" + methodName + "(" + mapperParamName + ");\n");
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
