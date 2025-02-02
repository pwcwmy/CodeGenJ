package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.utils.DateUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

public class BuildComment {
    /**
     * 构建类的注解
     */
    public static void createClassComment(BufferedWriter bw, String comment) throws Exception {
        /**
         * @Description
         * @Date
         */
        bw.write("/**\n");
        bw.write(" * @Description: " + comment + "\n");
        bw.write(" * @author: " + Constants.AUTHOR_COMMENT + "\n");
        bw.write(" * @Date: " + DateUtils.format(new Date(), "YYYY/MM/DD") + "\n");
        bw.write(" */\n");

    }

    /**
     * 构建字段的注解
     */
    public static void createFieldComment (BufferedWriter bw, String comment) throws Exception {
        /**
         *
         */
        bw.write("\t/**\n");
        bw.write("\t * " + (comment != null ? comment : "") + "\n");;
        bw.write("\t */\n");
    }

    /**
     * 构建方法的注解
     */
    public static void createMethodComment(BufferedWriter bw, String comment) throws IOException {
        /**
         *
         */
        bw.write("\t/**\n");
        bw.write("\t * " + (comment != null ? comment : "") + "\n");;
        bw.write("\t */\n");
    }
}
