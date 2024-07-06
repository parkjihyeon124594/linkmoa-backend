package com.knu.linkmoa.domain.member.dto.response;

import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiMemberResponse<T> extends ApiResponseSpec<T> {
    public ApiMemberResponse(Boolean status, int code, String message, T data) {
        super(status, code, message, data);
    }
}
