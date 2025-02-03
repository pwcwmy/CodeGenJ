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

public class BuildMapper {
    public static final Logger logger = LoggerFactory.getLogger(BuildMapper.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_MAPPERS);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPER;
        File queryFile = new File(folder, className + ".java");
        OutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        // 关闭流的原则：先创建的后关闭
        try {
            out = new FileOutputStream(queryFile);
            osw = new OutputStreamWriter(out, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write("package " + Constants.PACKAGE_MAPPERS + ";\n\n");
            bw.write("import org.apache.ibatis.annotations.Param;\n\n");
            BuildComment.createClassComment(bw, tableInfo.getComment() + "Mapper");
            bw.write("public interface " + className + "<T, P> extends BaseMapper {\n");

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
                    paramName.append("@Param(\"" + fieldInfo.getPropertyName() + "\") String " + fieldInfo.getPropertyName());
                    // 联合查询
                    if (index < keyIndexfieldInfoList.size()) {
                        paramName.append(", ");
                    }
                }
                BuildComment.createMethodComment(bw, "根据" + methodName + "查询");
                bw.write("\t T selectBy" + methodName + " (" + paramName + ");\n\n");

                // 更新多传一个Bean xml中bean是小写
                // @Param("bean") T t
                BuildComment.createMethodComment(bw, "根据" + methodName + "更新");
                bw.write("\t Integer updateBy" + methodName + " (@Param(\"bean\") T t, " + paramName + ");\n\n");

                BuildComment.createMethodComment(bw, "根据" + methodName + "删除");
                bw.write("\t Integer deleteBy" + methodName + " (" + paramName + ");\n\n");
            }
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
