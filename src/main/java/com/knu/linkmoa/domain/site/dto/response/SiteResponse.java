package com.knu.linkmoa.domain.site.dto.response;

import lombok.Builder;

@Builder
public record SiteResponse(Long siteId,String stieName,String siteUrl) {
}
