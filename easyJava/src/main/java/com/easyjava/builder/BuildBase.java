package com.easyjava.builder;

import com.easyjava.bean.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import java.io.*;
import java.util.List;

public class BuildBase {
    private static final Logger logger = LoggerFactory.getLogger(BuildBase.class);

    public static void execute() {
        List<String> headerInfoList = new ArrayList<>();
        // 生成DateTimePatternEnum
        headerInfoList.add("package " + Constants.PACKAGE_ENUMS + ";");
        build(headerInfoList, "DateTimePatternEnum", Constants.PATH_ENUMS);
        headerInfoList.clear();

        // 生成DateUtils
        headerInfoList.add("package " + Constants.PACKAGE_UTILS + ";");
        build(headerInfoList, "DateUtils", Constants.PATH_UTILS);
        headerInfoList.clear();

        // 生成BaseMapper
        headerInfoList.add("package " + Constants.PACKAGE_MAPPERS + ";");
        build(headerInfoList, "BaseMapper", Constants.PATH_MAPPERS);
        headerInfoList.clear();

        // 生成DateTimePatternEnum
        headerInfoList.add("package " + Constants.PACKAGE_ENUMS + ";");
        build(headerInfoList, "PageSizeEnum", Constants.PATH_ENUMS);
        headerInfoList.clear();

        // 生成SimplePage
        // import com.easyjava.enums.PageSizeEnum;
        headerInfoList.add("package " + Constants.PACKAGE_QUERY + ";");
        headerInfoList.add("import " + Constants.PACKAGE_ENUMS + ".PageSizeEnum;\n");
        build(headerInfoList, "SimplePage", Constants.PATH_QUERY);
        headerInfoList.clear();

        // 生成BaseQuery
        headerInfoList.add("package " + Constants.PACKAGE_QUERY + ";");
        build(headerInfoList, "BaseQuery", Constants.PATH_QUERY);
        headerInfoList.clear();

        // 生成PaginationResultVO
        headerInfoList.add("package " + Constants.PACKAGE_VO + ";");
        build(headerInfoList, "PaginationResultVO", Constants.PATH_VO);

        // 生成ResponseVO
        build(headerInfoList, "ResponseVO", Constants.PATH_VO);
        headerInfoList.clear();

        // 生成BusinessException
        headerInfoList.add("package " + Constants.PACKAGE_EXCEPTION + ";");
        // import com.easyjava.entity.enums.ResponseCodeEnum;
        headerInfoList.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum;\n");
        build(headerInfoList, "BusinessException", Constants.PATH_EXCEPTION);
        headerInfoList.clear();

        // 生成ResponseCodeEnum
        headerInfoList.add("package " + Constants.PACKAGE_ENUMS + ";");
        build(headerInfoList, "ResponseCodeEnum", Constants.PATH_ENUMS);
        headerInfoList.clear();

        // 生成AGlobalExceptionHandlerController
        // package com.easyjava.controller;
        //
        //import com.easyjava.entity.enums.ResponseCodeEnum;
        //import com.easyjava.entity.vo.ResponseVO;
        //import com.easyjava.exception.BusinessException;
        headerInfoList.add("package " + Constants.PACKAGE_CONTROLLER + ";\n\n");
        headerInfoList.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum;\n");
        headerInfoList.add("import " + Constants.PACKAGE_VO + ".ResponseVO;\n");
        headerInfoList.add("import " + Constants.PACKAGE_EXCEPTION + ".BusinessException;\n");
        build(headerInfoList, "AGlobalExceptionHandlerController", Constants.PATH_CONTROLLER);
        headerInfoList.clear();

        // 生成ABaseController
        // package com.easyjava.controller;
        //
        //import com.easyjava.entity.enums.ResponseCodeEnum;
        //import com.easyjava.entity.vo.ResponseVO;
        headerInfoList.add("package " + Constants.PACKAGE_CONTROLLER + ";\n\n");
        headerInfoList.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum;\n");
        headerInfoList.add("import " + Constants.PACKAGE_VO + ".ResponseVO;\n");
        build(headerInfoList, "ABaseController", Constants.PATH_CONTROLLER);
        headerInfoList.clear();


    }

    private static void build(List<String> headerInfoList, String fileName, String outputPath) {
        File folder = new File(outputPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File javaFile = new File(outputPath, fileName + ".java");

        OutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;

        InputStream in = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            out = new FileOutputStream(javaFile);
            osw = new OutputStreamWriter(out, "UTF-8");
            bw = new BufferedWriter(osw);

            String templatePath = BuildBase.class.getClassLoader().getResource("template/" + fileName + ".txt").getPath();
            in = new FileInputStream(templatePath);
            isr = new InputStreamReader(in, "UTF-8");
            br = new BufferedReader(isr);

            for (String head : headerInfoList) {
                bw.write(head);
                bw.newLine();
                if (head.contains("package")) {
                    bw.newLine();
                }
            }

            String lineInfo = null;
            while ((lineInfo = br.readLine()) != null) {
                bw.write(lineInfo + "\n");
            }
            bw.flush();
        } catch (Exception e) {
            logger.error("生成基础类:{}, 失败: ", fileName, e);
        } finally {
            // 输入流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 输出流
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
