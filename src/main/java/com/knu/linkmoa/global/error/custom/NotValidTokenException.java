package com.knu.linkmoa.global.error.custom;

import com.knu.linkmoa.global.error.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class NotValidTokenException extends RuntimeException{

    private ErrorCode errorCode;

    public NotValidTokenException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public NotValidTokenException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public NotValidTokenException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public NotValidTokenException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public NotValidTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

}
