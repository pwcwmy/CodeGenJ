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
}
