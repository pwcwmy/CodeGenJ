/*
package com.easyjava.controller;

import com.easyjava.entity.enums.ResponseCodeEnum;
import com.easyjava.entity.vo.ResponseVO;

public class ABaseController {
    protected static final String STATUS_SUCCESS = "success";
    protected static final String STATUS_ERROR = "error";
    protected <T> ResponseVO getSuccessResponseVO(T t) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setStatus(STATUS_SUCCESS);
        responseVO.setMessage(ResponseCodeEnum.CODE_200.getMessage());
        responseVO.setData(t);
        return responseVO;
    }

    */
/**
     * 处理业务错误
     * @param e
     * @param t
     * @return
     * @param <T>
     *//*

    protected <T> ResponseVO getBusinessErrorResponseVO(BusinessException e, T t) {
        ResponseVO vo = new ResponseVO<>();
        vo.setStatus(STATUS_ERROR);
        if (e.getCode() == null) {
            vo.setCode(ResponseCodeEnum.CODE_600.getCode());
        } else {
            vo.setCode(e.getCode());
        }
        vo.setMessage(e.getMessage());
        vo.setData(t);
        return vo;
    }

    */
/**
     * 处理服务端错误
     * @param t
     * @return
     * @param <T>
     *//*

    protected <T> ResponseVO getServerErrorResponseVO(T t) {
        ResponseVO vo = new ResponseVO<>();
        vo.setCode(ResponseCodeEnum.CODE_500.getCode());
        vo.setStatus(STATUS_ERROR);
        vo.setMessage(ResponseCodeEnum.CODE_500.getMessage());
        vo.setData(t);
        return vo;
    }
}
*/
