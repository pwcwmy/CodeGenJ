package com.easylive.common.controller;



import com.easylive.common.entity.enums.ResponseCodeEnum;

import com.easylive.common.entity.vo.ResponseVO;

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
