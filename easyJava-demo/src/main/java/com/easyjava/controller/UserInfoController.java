package com.easyjava.controller;

import java.util.List;

import com.easyjava.entity.po.UserInfo;
import com.easyjava.entity.query.UserInfoQuery;
import com.easyjava.entity.vo.ResponseVO;
import com.easyjava.service.UserInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @Description: 用户信息Controller
 * @author: peter
 * @Date: 2025/02/37
 */
@RestController
@RequestMapping("userInfo")
public class UserInfoController extends AGlobalExceptionHandlerController {
	@Resource
	private UserInfoService userInfoService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("loadDataByPage")
	public ResponseVO loadDataByPage(UserInfoQuery query) {
		return getSuccessResponseVO(userInfoService.selectListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("insert")
	public ResponseVO insert(UserInfo bean) {
		userInfoService.insert(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("insertBatch")
	public ResponseVO insertBatch(List<UserInfo> list) {
		userInfoService.insertBatch(list);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增或修改
	 */
	@RequestMapping("insertOrUpdateBatch")
	public ResponseVO insertOrUpdateBatch(List<UserInfo> list) {
		userInfoService.insertOrUpdateBatch(list);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据UserId查询
	 */
	@RequestMapping("selectByUserId")
	public ResponseVO selectByUserId (String userId) {
		return getSuccessResponseVO(userInfoService.selectByUserId(userId));
	}

	/**
	 * 根据UserId更新
	 */
	@RequestMapping("updateByUserId")
	public  ResponseVO updateByUserId (UserInfo bean, String userId) {
		userInfoService.updateByUserId(bean, userId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据UserId删除
	 */
	@RequestMapping("deleteByUserId")
	public  ResponseVO deleteByUserId (String userId) {
		userInfoService.deleteByUserId(userId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Email查询
	 */
	@RequestMapping("selectByEmail")
	public ResponseVO selectByEmail (String email) {
		return getSuccessResponseVO(userInfoService.selectByEmail(email));
	}

	/**
	 * 根据Email更新
	 */
	@RequestMapping("updateByEmail")
	public  ResponseVO updateByEmail (UserInfo bean, String email) {
		userInfoService.updateByEmail(bean, email);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Email删除
	 */
	@RequestMapping("deleteByEmail")
	public  ResponseVO deleteByEmail (String email) {
		userInfoService.deleteByEmail(email);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Nickname查询
	 */
	@RequestMapping("selectByNickname")
	public ResponseVO selectByNickname (String nickname) {
		return getSuccessResponseVO(userInfoService.selectByNickname(nickname));
	}

	/**
	 * 根据Nickname更新
	 */
	@RequestMapping("updateByNickname")
	public  ResponseVO updateByNickname (UserInfo bean, String nickname) {
		userInfoService.updateByNickname(bean, nickname);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Nickname删除
	 */
	@RequestMapping("deleteByNickname")
	public  ResponseVO deleteByNickname (String nickname) {
		userInfoService.deleteByNickname(nickname);
		return getSuccessResponseVO(null);
	}

}