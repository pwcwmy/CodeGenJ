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
 * @Date: 2025/02/37
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
		SimplePage simplePage = new SimplePage(pageNo, pageSize, totalCount);
		query.setSimplePage(simplePage);
		List<UserInfo> list = this.userInfoMapper.selectList(query);
		return new PaginationResultVO<>(totalCount, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getTotalPage(), list);
	}

	/**
	 * 新增
	 */
	@Override
	public Long insert(UserInfo bean) {
		return this.userInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Long insertBatch(List<UserInfo> list) {
		if (list == null || list.isEmpty()) {
			return 0L;
		}
		return this.userInfoMapper.insertBatch(list);
	}

	/**
	 * 批量新增或修改
	 */
	@Override
	public Long insertOrUpdateBatch(List<UserInfo> list) {
		if (list == null || list.isEmpty()) {
			return 0L;
		}
		return this.userInfoMapper.insertOrUpdateBatch(list);
	}

	/**
	 * 根据UserId查询
	 */
	@Override
	public  UserInfo selectByUserId (String userId) {
		return this.userInfoMapper.selectByUserId(userId);
	}

	/**
	 * 根据UserId更新
	 */
	@Override
	public  Long updateByUserId (UserInfo bean, String userId) {
		return this.userInfoMapper.updateByUserId(bean, userId);
	}

	/**
	 * 根据UserId删除
	 */
	@Override
	public  Long deleteByUserId (String userId) {
		return this.userInfoMapper.deleteByUserId(userId);
	}

	/**
	 * 根据Email查询
	 */
	@Override
	public  UserInfo selectByEmail (String email) {
		return this.userInfoMapper.selectByEmail(email);
	}

	/**
	 * 根据Email更新
	 */
	@Override
	public  Long updateByEmail (UserInfo bean, String email) {
		return this.userInfoMapper.updateByEmail(bean, email);
	}

	/**
	 * 根据Email删除
	 */
	@Override
	public  Long deleteByEmail (String email) {
		return this.userInfoMapper.deleteByEmail(email);
	}

	/**
	 * 根据Nickname查询
	 */
	@Override
	public  UserInfo selectByNickname (String nickname) {
		return this.userInfoMapper.selectByNickname(nickname);
	}

	/**
	 * 根据Nickname更新
	 */
	@Override
	public  Long updateByNickname (UserInfo bean, String nickname) {
		return this.userInfoMapper.updateByNickname(bean, nickname);
	}

	/**
	 * 根据Nickname删除
	 */
	@Override
	public  Long deleteByNickname (String nickname) {
		return this.userInfoMapper.deleteByNickname(nickname);
	}

}