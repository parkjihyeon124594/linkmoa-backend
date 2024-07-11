package com.knu.linkmoa.domain.site.dto.response;


import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiSiteResponse<T> extends ApiResponseSpec {

    private T data;
    @Builder
    public ApiSiteResponse(Boolean status, int code, String message,T data) {
        super(status, code, message);
        this.data=data;
    }
}
