package com.easyjava.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseMapper<T, P> {
    /*
     * insert: (插入），<br/>
     */
    Long insert(@Param("bean") T t);

    /*
     * insertOrUpdate: (插入或者更新），<br/>
     */
    Long insertOrUpdate(@Param("bean") T t);

    /*
     * insertBatch: (批量插入），<br/>
     */
    Long insertBatch(@Param("list") List<T> list);

    /*
     * insertOrUpdateBatch: (批量插入或者更新），<br/>
     */
    Long insertOrUpdateBatch(@Param("list") List<T> list);

    /*
     * selectList: (根据参数查询集合），<br/>
     */
    List<T> selectList(@Param("query") P p);

    /*
     * selectCount: (根据参数查询数量），<br/>
     */
    Long selectCount(@Param("query") P p);
}
