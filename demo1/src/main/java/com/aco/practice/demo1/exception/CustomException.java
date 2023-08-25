package com.aco.practice.demo1.exception;

import com.aco.practice.basic.util.ApiHttpCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: HaoJianXu
 * @Date: 2020/6/5 21:42
 */
@Api(value = "自定义异常")
public class CustomException extends RuntimeException {

    @ApiModelProperty(value = "异常状态码")
    private ApiHttpCode code;

    @ApiModelProperty(value = "异常信息")
    private String msg;

    public CustomException(ApiHttpCode code){
        super(code.getMsg());
        this.code = code;
    }

    public CustomException(ApiHttpCode code, String msg){
        super(msg);
        this.code = code;
    }

    public CustomException(String msg){
        super(msg);
        this.code = ApiHttpCode.ERROR;
    }

    public ApiHttpCode getCode() {
        return code;
    }

    public void setCode(ApiHttpCode code) {
        this.code = code;
    }
}
