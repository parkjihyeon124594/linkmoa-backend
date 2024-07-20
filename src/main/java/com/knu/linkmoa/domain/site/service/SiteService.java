package com.knu.linkmoa.domain.site.service;


import com.knu.linkmoa.domain.directory.entity.Directory;
import com.knu.linkmoa.domain.directory.error.errorcode.DirectoryErrorCode;
import com.knu.linkmoa.domain.directory.error.exception.DirectoryException;
import com.knu.linkmoa.domain.directory.repository.DirectoryRepository;
import com.knu.linkmoa.domain.site.dto.request.SiteCreateDto;
import com.knu.linkmoa.domain.site.dto.request.SiteIdDto;
import com.knu.linkmoa.domain.site.dto.response.ApiSiteResponse;
import com.knu.linkmoa.domain.site.dto.request.SiteUpdateDto;
import com.knu.linkmoa.domain.site.dto.response.SiteGetResponse;
import com.knu.linkmoa.domain.site.entity.Site;
import com.knu.linkmoa.domain.site.error.errorcode.SiteErrorCode;
import com.knu.linkmoa.domain.site.error.exception.SiteException;
import com.knu.linkmoa.domain.site.repository.SiteRepository;
import com.knu.linkmoa.global.principal.PrincipalDetails;
import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;
    private final DirectoryRepository directoryRepository;

    @Transactional
    public ApiSiteResponse<Long> saveSite(SiteCreateDto siteCreateDto, PrincipalDetails principalDetails){
        Site newSite = null;


        if(siteCreateDto.directoryId() == null){
            newSite = Site.builder()
                    .siteName(siteCreateDto.siteName())
                    .siteUrl(siteCreateDto.siteUrl())
                    .memberId(principalDetails.getId())
                    .build();
        }
        else {
            Directory directory = directoryRepository.findById(siteCreateDto.directoryId())
                    .orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND_EXCEPTION));

            newSite = Site.builder()
                    .siteName(siteCreateDto.siteName())
                    .siteUrl(siteCreateDto.siteUrl())
                    .directory(directory)
                    .memberId(principalDetails.getId())
                    .build();
        }

        if(newSite.getMemberId().equals(principalDetails.getId())) {
            siteRepository.save(newSite);

            return ApiSiteResponse.<Long>builder()
                    .status(true)
                    .code(200)
                    .message("site 생성에 성공하였습니다.")
                    .data(newSite.getId())
                    .build();
        }
        else{
            return ApiSiteResponse.<Long>builder()
                    .status(false)
                    .code(403)
                    .message("site 생성 권한이 없습니다.")
                    .data(newSite.getId())
                    .build();
        }


    }

    @Transactional
    public ApiResponseSpec deleteSite(SiteIdDto siteIdDto, PrincipalDetails principalDetails){


        Site deletedSite = siteRepository.findById(siteIdDto.siteId())
                .orElseThrow(()->new SiteException(SiteErrorCode.SITE_NOT_FOUND_EXCEPTION));

        if(deletedSite.getMemberId().equals(principalDetails.getId())) {
            siteRepository.delete(deletedSite);

            return new ApiResponseSpec(true, 200, "site가 삭제됐습니다.");
        }
        else{
            return new ApiResponseSpec(false, 403, "site 삭제 권한이 없습니다.");
        }
    }

    @Transactional
    public ApiSiteResponse<Long> updateSite(SiteUpdateDto siteUpdateDto,PrincipalDetails principalDetails){
        Site updateSite = siteRepository.findById(siteUpdateDto.siteid())
                .orElseThrow(()-> new SiteException(SiteErrorCode.SITE_NOT_FOUND_EXCEPTION));

        if(updateSite.getMemberId().equals(principalDetails.getId())) {
            updateSite.updateSite(siteUpdateDto.siteName(), siteUpdateDto.siteUrl());
            siteRepository.save(updateSite);

            return ApiSiteResponse.<Long>builder()
                    .status(true)
                    .code(200)
                    .message("site 수정에 성공하였습니다.")
                    .data(updateSite.getId())
                    .build();
        }
        else{
            return ApiSiteResponse.<Long>builder()
                    .status(false)
                    .code(403)
                    .message("site 수정 권한이 없습니다.")
                    .data(updateSite.getId())
                    .build();
        }
    }

    public ApiSiteResponse<SiteGetResponse> getSite(SiteIdDto siteIdDto, PrincipalDetails principalDetails) {
        Site getSite = siteRepository.findById(siteIdDto.siteId())
                .orElseThrow(() -> new SiteException(SiteErrorCode.SITE_NOT_FOUND_EXCEPTION));

        SiteGetResponse siteGetResponse = SiteGetResponse.builder()
                .siteName(getSite.getSiteName())
                .siteUrl(getSite.getSiteUrl())
                .build();

        if (getSite.getMemberId().equals(principalDetails.getId())) {
            return ApiSiteResponse.<SiteGetResponse>builder()
                    .status(true)
                    .code(200)
                    .message("Site 조회에 성공하였습니다.")
                    .data(siteGetResponse)
                    .build();
        } else {
            return ApiSiteResponse.<SiteGetResponse>builder()
                    .status(false)
                    .code(403)
                    .message("Site 조회에 권한이 없습니다.")
                    .data(siteGetResponse)
                    .build();
        }
    }




}
