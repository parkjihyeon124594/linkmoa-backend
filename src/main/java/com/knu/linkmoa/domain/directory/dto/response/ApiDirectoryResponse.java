package com.knu.linkmoa.domain.directory.dto.response;

import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.Builder;

public class ApiDirectoryResponse<T> extends ApiResponseSpec {

    private T data;


    @Builder
    public ApiDirectoryResponse(Boolean status, int code, String message,T data) {
        super(status, code, message);
        this.data=data;
    }
}
