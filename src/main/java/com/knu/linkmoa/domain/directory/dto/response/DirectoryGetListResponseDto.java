package com.knu.linkmoa.domain.directory.dto.response;

import com.knu.linkmoa.domain.site.dto.response.SiteResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record DirectoryGetListResponseDto

        (Long directoryId, String directoryName, List<DirectoryGetListResponseDto> childDirectories, List<SiteResponse> siteResponseList) {
}
