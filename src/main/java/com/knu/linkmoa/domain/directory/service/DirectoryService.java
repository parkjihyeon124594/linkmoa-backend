package com.knu.linkmoa.domain.directory.service;


import com.knu.linkmoa.domain.directory.dto.request.DirectoryCreateDto;
import com.knu.linkmoa.domain.directory.dto.request.DirectoryUpdateDto;
import com.knu.linkmoa.domain.directory.dto.response.ApiDirectoryResponse;
import com.knu.linkmoa.domain.directory.dto.response.DirectoryGetListResponseDto;
import com.knu.linkmoa.domain.directory.entity.Directory;
import com.knu.linkmoa.domain.directory.error.errorcode.DirectoryErrorCode;
import com.knu.linkmoa.domain.directory.error.exception.DirectoryException;
import com.knu.linkmoa.domain.directory.repository.DirectoryRepository;
import com.knu.linkmoa.domain.member.entity.Member;
import com.knu.linkmoa.domain.member.repository.MemberRepository;
import com.knu.linkmoa.domain.member.service.MemberService;
import com.knu.linkmoa.domain.site.dto.response.SiteResponse;
import com.knu.linkmoa.global.principal.PrincipalDetails;
import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;



    @Transactional
    public ApiDirectoryResponse<Long> saveDirectory(DirectoryCreateDto directoryCreateDto, PrincipalDetails principalDetails){

        Member member = memberService.findMemberByEmail(principalDetails.getEmail());

        Directory newDirectory ;

        if (directoryCreateDto.parentDirectoryId() != 0) {
            Directory parentDirectory = directoryRepository.findById(directoryCreateDto.parentDirectoryId())
                    .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND_EXCEPTION));

            newDirectory =Directory.builder()
                    .directoryName(directoryCreateDto.directoryName())
                    .member(member)
                    .parentDirectory(parentDirectory)
                    .build();

            parentDirectory.addChildDirectory(newDirectory);
        }else{
            newDirectory = Directory.builder()
                    .directoryName(directoryCreateDto.directoryName())
                    .member(member)
                    .build();
            // root 디렉토리의 parent_directory_id는 null로 저장됨
        }

        directoryRepository.save(newDirectory);  // 디렉토리를 저장. CascadeType.ALL로 인해 연관된 엔티티(부모-자식)도 함께 저장됨


        return ApiDirectoryResponse.<Long>builder()
                .status(true)
                .code(200)
                .message("directory 생성에 성공하였습니다.")
                .data(newDirectory.getId())
                .build();
    }

    @Transactional
    public ApiResponseSpec deleteDirectory(Long directoryId,PrincipalDetails principalDetails){
        Directory deleteDirectory = directoryRepository.findById(directoryId)
                .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND_EXCEPTION));

        Member member=memberService.findMemberByEmail(principalDetails.getEmail());

        if (member.getId().equals(deleteDirectory.getMember().getId())) {
            directoryRepository.delete(deleteDirectory);
            return new ApiResponseSpec(true, 200, "Directory 삭제에 성공하였습니다.");
        } else {
            return new ApiResponseSpec(false,403,"Directory 삭제 권한이 없습니다.");
        }
    }



    @Transactional
    public ApiDirectoryResponse<Long> updateDirectory(DirectoryUpdateDto directoryUpdateDto,PrincipalDetails principalDetails){
        Directory updateDirectory = directoryRepository.findById(directoryUpdateDto.directoryId())
                .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND_EXCEPTION));

        Member member=memberService.findMemberByEmail(principalDetails.getEmail());

        if(member.getId().equals(updateDirectory.getMember().getId())){
            updateDirectory.updateDirecotryName(directoryUpdateDto.directoryName());
            return ApiDirectoryResponse.<Long>builder()
                    .status(true)
                    .code(200)
                    .message("directory 수정에 성공하였습니다.")
                    .data(updateDirectory.getId())
                    .build();
        }else{
            return ApiDirectoryResponse.<Long>builder()
                    .status(false)
                    .code(403)
                    .message("directory 수정 권한이 없습니다,")
                    .data(updateDirectory.getId())
                    .build();
        }

    }


    public ApiDirectoryResponse<DirectoryGetListResponseDto> getDirectoryWithSites(Long directoryId, PrincipalDetails principalDetails){
        Directory getDirectory = directoryRepository.findById(directoryId)
                .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND_EXCEPTION));

        DirectoryGetListResponseDto directoryGetListResponseDto = convertDirectoryResponse(getDirectory);
        Member member=memberService.findMemberByEmail(principalDetails.getEmail());

        if(member.getId().equals(getDirectory.getMember().getId())){
            return ApiDirectoryResponse.<DirectoryGetListResponseDto>builder()
                    .status(true)
                    .code(200)
                    .message("directory 조회에 성공하였습니다.")
                    .data(directoryGetListResponseDto)
                    .build();
        }
        else{
            return ApiDirectoryResponse.<DirectoryGetListResponseDto>builder()
                    .status(false)
                    .code(403)
                    .message("directory 조회 권한이 없습니다.")
                    .build();
        }



    }

    public DirectoryGetListResponseDto convertDirectoryResponse(Directory directory){


        List<DirectoryGetListResponseDto> childDirectories = directory.getChildDirectories().stream()
                .map(this::convertDirectoryResponse)
                .collect(Collectors.toList());

        List<SiteResponse> siteResponseList = directory.getSites().stream()
                .map(site -> SiteResponse.builder()
                        .siteId(site.getId())
                        .stieName(site.getSiteName())
                        .siteUrl(site.getSiteUrl())
                        .build())
                .collect(Collectors.toList());



        /*List<DirectoryResponse> childDirectories =new ArrayList<>();

        for(Directory child : directory.getChildDirectories()){
            childDirectories.add(convertDirectoryResponse(child));
        }

        List<SiteResponse> siteResponseList = new ArrayList<>();

        for(Site site: directory.getSites()){
            SiteResponse siteResponse = SiteResponse.builder()
                    .siteId(site.getId())
                    .stieName(site.getSiteName())
                    .siteUrl(site.getSiteUrl())
                    .build();

            siteResponseList.add(siteResponse);
        }*/

        return DirectoryGetListResponseDto.builder()
                .directoryId(directory.getId())
                .directoryName(directory.getDirectoryName())
                .childDirectories(childDirectories)
                .siteResponseList(siteResponseList)
                .build();
    }






}
