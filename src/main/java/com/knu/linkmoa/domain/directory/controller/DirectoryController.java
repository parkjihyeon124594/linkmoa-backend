package com.knu.linkmoa.domain.directory.controller;

import com.knu.linkmoa.domain.directory.dto.request.DirectoryCreateDto;
import com.knu.linkmoa.domain.directory.dto.response.ApiDirectoryResponse;
import com.knu.linkmoa.domain.directory.service.DirectoryService;
import com.knu.linkmoa.global.principal.PrincipalDetails;
import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directory")
public class DirectoryController {
    private final DirectoryService directoryService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponse<Long>> createDirectory(
            @RequestBody DirectoryCreateDto directoryCreateDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            ){
        ApiDirectoryResponse<Long> longApiDirectoryResponse = directoryService.saveDirectory(directoryCreateDto, principalDetails);

        return ResponseEntity.ok().body(longApiDirectoryResponse);
    }


    @DeleteMapping("/{directoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseSpec> deleteDirectory(
            @PathVariable("directoryId") Long directoryId,
    @AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        ApiResponseSpec apiResponseSpec = directoryService.deleteDirectory(directoryId, principalDetails);

        return ResponseEntity.ok().body(apiResponseSpec);

    }



}
