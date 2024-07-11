package com.knu.linkmoa.domain.site.error.exception;

import com.knu.linkmoa.domain.site.error.errorcode.SiteErrorCode;
import lombok.Getter;

@Getter
public class SiteException extends RuntimeException {

    private SiteErrorCode siteErrorCode;

    public SiteException(SiteErrorCode siteErrorCode) {
        this.siteErrorCode = siteErrorCode;
    }
}
