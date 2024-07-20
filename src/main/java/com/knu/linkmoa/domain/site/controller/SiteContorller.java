package com.knu.linkmoa.domain.site.controller;

import com.knu.linkmoa.domain.site.dto.request.SiteCreateDto;
import com.knu.linkmoa.domain.site.dto.request.SiteIdDto;
import com.knu.linkmoa.domain.site.dto.request.SiteUpdateDto;
import com.knu.linkmoa.domain.site.dto.response.ApiSiteResponse;
import com.knu.linkmoa.domain.site.dto.response.SiteGetResponse;
import com.knu.linkmoa.domain.site.service.SiteService;
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
@RequestMapping("/api/site")
public class SiteContorller {

    private final SiteService siteService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiSiteResponse<Long>> createSite(
            @RequestBody @Validated SiteCreateDto siteCreateDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            )
    {
        ApiSiteResponse<Long> apiSiteCreateResponse = siteService.saveSite(siteCreateDto, principalDetails);

        return ResponseEntity.ok().body(apiSiteCreateResponse);
    }


    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseSpec> deleteSite(
            @RequestBody @Validated SiteIdDto siteIdDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            ){

        ApiResponseSpec apiSiteDeleteResponseSpec = siteService.deleteSite(siteIdDto, principalDetails);

    return ResponseEntity.ok().body(apiSiteDeleteResponseSpec);

    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiSiteResponse<Long>> updateSite(
            @RequestBody @Validated SiteUpdateDto siteUpdateDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            ){

        ApiSiteResponse<Long> apiSiteUpdateResponse = siteService.updateSite(siteUpdateDto, principalDetails);

        return ResponseEntity.ok().body(apiSiteUpdateResponse);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiSiteResponse<SiteGetResponse>> getSite(
            @RequestBody @Validated SiteIdDto siteIdDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        ApiSiteResponse<SiteGetResponse> getSite = siteService.getSite(siteIdDto, principalDetails);

        return ResponseEntity.ok().body(getSite);
    }
}
