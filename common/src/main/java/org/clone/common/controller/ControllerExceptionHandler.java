package org.clone.common.controller;

import org.clone.common.exception.BusinessException;
import org.clone.common.response.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理、数据预处理等
 */
@ControllerAdvice //spring3.2新注解
public class ControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 所有异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResponse exceptionHandler(Exception e) throws Exception{
        //LOG.info("seata ID: {}", RootContext.getXID());
        //如果在一次全局事务中出现异常，则不要包装返回值，直接抛给调用方，调用方回滚事务
//        if (StrUtil.isNotBlank(RootContext.getXID())) {
//            throw e;
//        }
        CommonResponse commonResp = new CommonResponse();
        LOG.error("Exception：", e);
        commonResp.setSuccess(false);
        commonResp.setMessage("Exception，please contact admin");
        return commonResp;
    }

    /**
     * 业务异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResponse exceptionHandler(BusinessException e) {
        CommonResponse commonResp = new CommonResponse();
        LOG.error("Business exception：{}", e.getAnEnum().getDesc());
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getAnEnum().getDesc());
        return commonResp;
    }

    /**
     * 校验异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public CommonResponse exceptionHandler(BindException e) {
        CommonResponse commonResp = new CommonResponse();
        LOG.error("Validation exception：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return commonResp;
    }

}
