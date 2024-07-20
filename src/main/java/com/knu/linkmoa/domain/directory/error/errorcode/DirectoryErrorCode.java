package com.knu.linkmoa.domain.directory.error.errorcode;

import com.knu.linkmoa.global.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DirectoryErrorCode implements ErrorCode {

    DIRECTORY_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,"directory를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
