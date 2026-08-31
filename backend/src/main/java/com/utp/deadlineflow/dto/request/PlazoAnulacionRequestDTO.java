package com.utp.deadlineflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlazoAnulacionRequestDTO(
        @NotBlank @Size(min = 10, max = 500) String motivo
) {}
