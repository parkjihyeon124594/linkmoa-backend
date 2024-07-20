package com.knu.linkmoa.domain.directory.controller;

import com.knu.linkmoa.domain.directory.dto.request.DirectoryCreateDto;
import com.knu.linkmoa.domain.directory.dto.request.DirectoryDeleteDto;
import com.knu.linkmoa.domain.directory.dto.request.DirectoryUpdateDto;
import com.knu.linkmoa.domain.directory.dto.response.ApiDirectoryResponse;
import com.knu.linkmoa.domain.directory.dto.response.DirectoryGetListResponseDto;
import com.knu.linkmoa.domain.directory.service.DirectoryService;
import com.knu.linkmoa.global.principal.PrincipalDetails;
import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directory")
public class DirectoryController {
    private final DirectoryService directoryService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponse<Long>> createDirectory(
            @RequestBody @Validated DirectoryCreateDto directoryCreateDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        ApiDirectoryResponse<Long> longApiDirectoryResponse = directoryService.saveDirectory(directoryCreateDto, principalDetails);

        return ResponseEntity.ok().body(longApiDirectoryResponse);
    }


    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseSpec> deleteDirectory(
            @RequestBody DirectoryDeleteDto directoryDeleteDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ApiResponseSpec apiResponseSpec = directoryService.deleteDirectory(directoryDeleteDto.directoryId(), principalDetails);

        return ResponseEntity.ok().body(apiResponseSpec);

    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponse> updateDirectory(
            @RequestBody @Validated DirectoryUpdateDto directoryUpdateDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        ApiDirectoryResponse<Long> apiDirectoryResponse = directoryService.updateDirectory(directoryUpdateDto, principalDetails);

        return ResponseEntity.ok().body(apiDirectoryResponse);
    }


    @GetMapping("{directoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponse<DirectoryGetListResponseDto>> getDirectoryListWithSiteList(
            @PathVariable("directoryId") Long directoryId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        ApiDirectoryResponse<DirectoryGetListResponseDto> directoryWithSites = directoryService.getDirectoryWithSites(directoryId, principalDetails);

        return ResponseEntity.ok().body(directoryWithSites);
    }

}



