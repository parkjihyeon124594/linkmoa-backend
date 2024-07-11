package com.knu.linkmoa.domain.directory.service;


import com.knu.linkmoa.domain.directory.dto.request.DirectoryCreateDto;
import com.knu.linkmoa.domain.directory.dto.request.DirectoryUpdateDto;
import com.knu.linkmoa.domain.directory.dto.response.ApiDirectoryResponse;
import com.knu.linkmoa.domain.directory.dto.response.DirectoryResponse;
import com.knu.linkmoa.domain.directory.entity.Directory;
import com.knu.linkmoa.domain.directory.error.errorcode.DirectoryErrorCode;
import com.knu.linkmoa.domain.directory.error.exception.DirectoryException;
import com.knu.linkmoa.domain.directory.repository.DirectoryRepository;
import com.knu.linkmoa.domain.member.entity.Member;
import com.knu.linkmoa.domain.member.repository.MemberRepository;
import com.knu.linkmoa.domain.site.dto.response.SiteResponse;
import com.knu.linkmoa.global.principal.PrincipalDetails;
import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public ApiDirectoryResponse<Long> saveDirectory(DirectoryCreateDto directoryCreateDto, PrincipalDetails principalDetails, Long parentDirectoryId){

        Member member = memberRepository.findByEmail(principalDetails.getEmail()).
                orElseThrow(()->new UsernameNotFoundException("해당 email에 해당하는 Member가 없습니다"));

        Directory newDirectory ;

        if (parentDirectoryId != 0) {
            Directory parentDirectory = directoryRepository.findById(parentDirectoryId)
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
    public ApiResponseSpec deleteDirectory(Long directoryId){
        Directory deleteDirectory = directoryRepository.findById(directoryId)
                .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND_EXCEPTION));

        directoryRepository.delete(deleteDirectory);

        return new ApiResponseSpec(true,200,"directory 삭제에 성공하였습니다.");
    }

    @Transactional
    public ApiDirectoryResponse<Long> updateDirectory(DirectoryUpdateDto directoryUpdateDto,Long directoryId){
        Directory updateDirectory = directoryRepository.findById(directoryId)
                .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND_EXCEPTION));

        updateDirectory.updateDirecotryName(directoryUpdateDto.directoryName());


        return ApiDirectoryResponse.<Long>builder()
                .status(true)
                .code(200)
                .message("directory 수정에 성공하였습니다.")
                .data(updateDirectory.getId())
                .build();
    }

    public ApiDirectoryResponse<DirectoryResponse> getDirectoryWithSites(Long directoryId){
        Directory directory = directoryRepository.findById(directoryId)
                .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND_EXCEPTION));

        DirectoryResponse directoryResponse =convertDirectoryResponse(directory);

        return ApiDirectoryResponse.<DirectoryResponse>builder()
                .status(true)
                .code(200)
                .message("directory 조회에 성공하였습니다.")
                .data(directoryResponse)
                .build();

    }

    public DirectoryResponse convertDirectoryResponse(Directory directory){


        List<DirectoryResponse> childDirectories = directory.getChildDirectories().stream()
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

        return DirectoryResponse.builder()
                .directoryId(directory.getId())
                .directoryName(directory.getDirectoryName())
                .childDirectories(childDirectories)
                .siteResponseList(siteResponseList)
                .build();
    }






}
