package com.easyjava.service.impl;

import java.util.List;

import com.easyjava.entity.enums.PageSizeEnum;
import com.easyjava.entity.po.UserInfo;
import com.easyjava.entity.query.SimplePage;
import com.easyjava.entity.query.UserInfoQuery;
import com.easyjava.entity.vo.PaginationResultVO;
import com.easyjava.mappers.UserInfoMapper;
import com.easyjava.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: 用户信息ServiceImpl
 * @author: peter
 * @Date: 2025/02/34
 */
@Service("UserInfoService")
public class UserInfoServiceImpl implements UserInfoService {
	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;
	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserInfo> selectListByParam(UserInfoQuery query) {
		return this.userInfoMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	@Override
	public Long selectCountByParam(UserInfoQuery query) {
		return this.userInfoMapper.selectCount(query);
	}

	/**
	 * 分页查询列表
	 */
	@Override
	public PaginationResultVO<UserInfo> selectListByPage(UserInfoQuery query) {
		long totalCount = this.selectCountByParam(query);
		long pageSize = query.getPageSize() == null ? PageSizeEnum.SIZE20.getPageSize() : query.getPageSize();
		long pageNo = query.getPageNo() == null ? 1 : query.getPageNo();

		SimplePage simplePage = new SimplePage(pageNo, totalCount, pageSize);

	}

	/**
	 * 新增
	 */
	@Override
	public Long insert(UserInfo userInfo) {
	}

	/**
	 * 批量新增
	 */
	@Override
	public Long insertBatch(UserInfo userInfo) {
	}

	/**
	 * 批量新增或修改
	 */
	@Override
	public Long insertOrUpdateBatch(UserInfo userInfo) {
	}

	/**
	 * 根据UserId查询
	 */
	@Override
	public  List<UserInfo> selectByUserId (String userId) {
	}

	/**
	 * 根据UserId更新
	 */
	@Override
	public  Long updateByUserId (UserInfo userInfo, String userId) {
	}

	/**
	 * 根据UserId删除
	 */
	@Override
	public  Long deleteByUserId (String userId) {
	}

	/**
	 * 根据Email查询
	 */
	@Override
	public  List<UserInfo> selectByEmail (String email) {
	}

	/**
	 * 根据Email更新
	 */
	@Override
	public  Long updateByEmail (UserInfo userInfo, String email) {
	}

	/**
	 * 根据Email删除
	 */
	@Override
	public  Long deleteByEmail (String email) {
	}

	/**
	 * 根据Nickname查询
	 */
	@Override
	public  List<UserInfo> selectByNickname (String nickname) {
	}

	/**
	 * 根据Nickname更新
	 */
	@Override
	public  Long updateByNickname (UserInfo userInfo, String nickname) {
	}

	/**
	 * 根据Nickname删除
	 */
	@Override
	public  Long deleteByNickname (String nickname) {
	}

}