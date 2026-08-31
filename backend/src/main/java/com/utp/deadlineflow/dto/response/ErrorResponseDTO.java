package com.utp.deadlineflow.dto.response;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        int status,
        String error,
        String mensaje,
        LocalDateTime timestamp
) {}
