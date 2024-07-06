package com.knu.linkmoa.global.error.custom;

import com.knu.linkmoa.global.error.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException{

    private ErrorCode errorCode;

    public MemberException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public MemberException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public MemberException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public MemberException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public MemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

}
