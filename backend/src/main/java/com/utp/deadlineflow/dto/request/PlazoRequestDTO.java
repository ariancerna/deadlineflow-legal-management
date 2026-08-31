package com.utp.deadlineflow.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PlazoRequestDTO(
        @NotNull Long expedienteId,
        @NotNull @Future LocalDate fechaLimite
) {}
