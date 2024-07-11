package com.knu.linkmoa.domain.site.service;


import com.knu.linkmoa.domain.directory.entity.Directory;
import com.knu.linkmoa.domain.directory.error.errorcode.DirectoryErrorCode;
import com.knu.linkmoa.domain.directory.error.exception.DirectoryException;
import com.knu.linkmoa.domain.directory.repository.DirectoryRepository;
import com.knu.linkmoa.domain.member.entity.Member;
import com.knu.linkmoa.domain.member.repository.MemberRepository;
import com.knu.linkmoa.domain.site.dto.request.SiteCreateDto;
import com.knu.linkmoa.domain.site.dto.response.ApiSiteResponse;
import com.knu.linkmoa.domain.site.dto.request.SiteUpdateDto;
import com.knu.linkmoa.domain.site.entity.Site;
import com.knu.linkmoa.domain.site.error.errorcode.SiteErrorCode;
import com.knu.linkmoa.domain.site.error.exception.SiteException;
import com.knu.linkmoa.domain.site.repository.SiteRepository;
import com.knu.linkmoa.global.principal.PrincipalDetails;
import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;
    private final MemberRepository memberRepository;
    private final DirectoryRepository directoryRepository;

    @Transactional
    public ApiSiteResponse<Long> saveSite(SiteCreateDto siteCreateDto, PrincipalDetails principalDetails, Long directoryId){

        Member member = memberRepository.findByEmail(principalDetails.getMember().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 email에 해당하는 Member가 없습니다"));

        Directory directory =directoryRepository.findById(directoryId)
                .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND_EXCEPTION));

        Site newSite = Site.builder()
                .siteName(siteCreateDto.siteName())
                .siteUrl(siteCreateDto.siteUrl())
                .directory(directory)
                .build();

        siteRepository.save(newSite);

        return ApiSiteResponse.<Long>builder()
                .status(true)
                .code(200)
                .message("site 생성에 성공하였습니다.")
                .data(newSite.getId())
                .build();
    }

    @Transactional
    public ApiResponseSpec deleteSite(Long siteId,PrincipalDetails principalDetails){

        Site site = siteRepository.findById(siteId)
                .orElseThrow(()->new SiteException(SiteErrorCode.SITE_NOT_FOUND_EXCEPTION));

        siteRepository.delete(site);

        return new ApiResponseSpec(true,200,"site가 삭제됐습니다.");
    }

    @Transactional
    public ApiSiteResponse<Long> updateSite(SiteUpdateDto siteUpdateDto, Long siteId){
        Site updatSite = siteRepository.findById(siteId)
                .orElseThrow(()-> new SiteException(SiteErrorCode.SITE_NOT_FOUND_EXCEPTION));

        updatSite.updateSite(siteUpdateDto.siteName(),siteUpdateDto.siteUrl());
        siteRepository.save(updatSite);

        return ApiSiteResponse.<Long>builder()
                .status(true)
                .code(200)
                .message("site 수정에 성공하였습니다.")
                .data(updatSite.getId())
                .build();
    }



}
