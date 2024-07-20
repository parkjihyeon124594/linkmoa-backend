package com.knu.linkmoa.domain.site.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SiteCreateDto(
        @NotNull @Size(max=30) String siteName,
        @NotNull String siteUrl,
        Long directoryId) {
}
