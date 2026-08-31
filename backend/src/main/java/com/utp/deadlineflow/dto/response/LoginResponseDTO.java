package com.utp.deadlineflow.dto.response;

public record LoginResponseDTO(
        String token,
        String tipo,
        String email,
        String rol
) {}
