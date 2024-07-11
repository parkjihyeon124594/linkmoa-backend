package com.knu.linkmoa.domain.site.error.errorcode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SiteErrorCode {

    SITE_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,"site를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}