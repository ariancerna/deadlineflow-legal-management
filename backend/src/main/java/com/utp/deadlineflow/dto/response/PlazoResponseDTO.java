package com.utp.deadlineflow.dto.response;

import com.utp.deadlineflow.entity.EstadoPlazo;

import java.time.LocalDate;

public record PlazoResponseDTO(
        Long id,
        EstadoPlazo estado,
        LocalDate fechaLimite
) {}
