package com.easyjava.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseMapper<T, P> {
    /*
     * insert: (插入），<br/>
     */
    Integer insert(@Param("Bean") T t);

    /*
     * insertOrUpdate: (插入或者更新），<br/>
     */
    Integer insertOrUpdate(@Param("Bean") T t);

    /*
     * insertBatch: (批量插入），<br/>
     */
    Integer insertBatch(@Param("List") List<T> list);

    /*
     * insertOrUpdateBatch: (批量插入或者更新），<br/>
     */
    Integer insertOrUpdateBatch(@Param("List") List<T> list);

    /*
     * selectList: (根据参数查询集合），<br/>
     */
    List<T> selectList(@Param("Query") P p);

    /*
     * selectCount: (根据参数查询数量），<br/>
     */
    Integer selectCount(@Param("Query") P p);
}
