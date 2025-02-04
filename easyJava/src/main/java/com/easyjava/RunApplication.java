package com.easyjava;

import com.easyjava.bean.TableInfo;
import com.easyjava.builder.*;
import java.util.List;

public class RunApplication {
    public static void main(String[] args) {
        List<TableInfo> tableInfoList = BuildTable.getTables();
        BuildBase.execute();
        for (TableInfo tableInfo : tableInfoList) {
            BuildPo.execute(tableInfo);

            BuildQuery.execute(tableInfo);

            BuildMapper.execute(tableInfo);

            BuildMapperXml.execute(tableInfo);

            BuildService.execute(tableInfo);

            BuildServiceImpl.execute(tableInfo);

            BuildController.execute(tableInfo);
        }
    }
}
