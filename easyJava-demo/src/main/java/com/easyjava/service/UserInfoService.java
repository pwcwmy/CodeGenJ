package com.easyjava.service;

import java.util.List;

import com.easyjava.entity.po.UserInfo;
import com.easyjava.entity.query.UserInfoQuery;
import com.easyjava.entity.vo.PaginationResultVO;

/**
 * @Description: 用户信息Service
 * @author: peter
 * @Date: 2025/02/37
 */
public interface UserInfoService {
	/**
	 * 根据条件查询列表
	 */
	List<UserInfo> selectListByParam(UserInfoQuery query);

	/**
	 * 根据条件查询数量
	 */
	Long selectCountByParam(UserInfoQuery query);

	/**
	 * 分页查询列表
	 */
	PaginationResultVO<UserInfo> selectListByPage(UserInfoQuery query);

	/**
	 * 新增
	 */
	Long insert(UserInfo bean);

	/**
	 * 批量新增
	 */
	Long insertBatch(List<UserInfo> list);

	/**
	 * 批量新增或修改
	 */
	Long insertOrUpdateBatch(List<UserInfo> list);

	/**
	 * 根据UserId查询
	 */
	 UserInfo selectByUserId (String userId);

	/**
	 * 根据UserId更新
	 */
	 Long updateByUserId (UserInfo bean, String userId);

	/**
	 * 根据UserId删除
	 */
	 Long deleteByUserId (String userId);

	/**
	 * 根据Email查询
	 */
	 UserInfo selectByEmail (String email);

	/**
	 * 根据Email更新
	 */
	 Long updateByEmail (UserInfo bean, String email);

	/**
	 * 根据Email删除
	 */
	 Long deleteByEmail (String email);

	/**
	 * 根据Nickname查询
	 */
	 UserInfo selectByNickname (String nickname);

	/**
	 * 根据Nickname更新
	 */
	 Long updateByNickname (UserInfo bean, String nickname);

	/**
	 * 根据Nickname删除
	 */
	 Long deleteByNickname (String nickname);

}