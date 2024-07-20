package com.knu.linkmoa.domain.site.dto.response;

import lombok.Builder;


@Builder
public record SiteGetResponse
        (String siteName,
         String siteUrl
        ){
}
