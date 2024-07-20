package com.knu.linkmoa.domain.site.dto.request;

import jakarta.validation.constraints.NotNull;

public record SiteIdDto(
        @NotNull Long siteId) {
}
