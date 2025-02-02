package com.easyjava.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: 用户信息Mapper
 * @author: peter
 * @Date: 2025/02/34
 */
public interface UserInfoMapper<T, P> extends BaseMapper {
	/**
	 * 根据UserId查询
	 */
	 T selectByUserId (@Param("userId") String userId);

	/**
	 * 根据UserId更新
	 */
	 Integer updateByUserId (@Param("bean") T t, @Param("userId") String userId);

	/**
	 * 根据UserId删除
	 */
	 Integer deleteByUserId (@Param("userId") String userId);

	/**
	 * 根据Email查询
	 */
	 T selectByEmail (@Param("email") String email);

	/**
	 * 根据Email更新
	 */
	 Integer updateByEmail (@Param("bean") T t, @Param("email") String email);

	/**
	 * 根据Email删除
	 */
	 Integer deleteByEmail (@Param("email") String email);

	/**
	 * 根据Nickname查询
	 */
	 T selectByNickname (@Param("nickname") String nickname);

	/**
	 * 根据Nickname更新
	 */
	 Integer updateByNickname (@Param("bean") T t, @Param("nickname") String nickname);

	/**
	 * 根据Nickname删除
	 */
	 Integer deleteByNickname (@Param("nickname") String nickname);

}