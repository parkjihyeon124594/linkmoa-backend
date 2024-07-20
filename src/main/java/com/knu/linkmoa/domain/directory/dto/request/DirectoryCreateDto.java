package com.knu.linkmoa.domain.directory.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DirectoryCreateDto(
        @NotNull @Size(max=20) String directoryName,
        Long parentDirectoryId) {
}
