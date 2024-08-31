package cn.polister.infosys.exception;

import cn.polister.infosys.enums.AppHttpCodeEnum;
import lombok.Getter;

@Getter
public class SystemException extends RuntimeException{

    private final int code;

    private final String msg;

    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }

}
