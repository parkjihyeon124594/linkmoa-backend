package com.knu.linkmoa.global.spec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ApiResponseSpec<T> {
    private Boolean status;
    private int code;
    private String message;
    private T data;
}
