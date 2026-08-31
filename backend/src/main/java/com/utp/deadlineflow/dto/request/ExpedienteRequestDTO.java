package com.utp.deadlineflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExpedienteRequestDTO(
        @NotBlank String numeroExpediente,
        @NotBlank String tipoProceso,
        @NotNull Long responsableId,
        Double honorariosPactados
) {}
