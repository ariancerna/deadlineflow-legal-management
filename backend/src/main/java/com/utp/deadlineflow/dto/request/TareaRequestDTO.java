package com.utp.deadlineflow.dto.request;

import com.utp.deadlineflow.entity.Prioridad;
import com.utp.deadlineflow.entity.TipoTarea;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TareaRequestDTO(
        @NotBlank String titulo,
        @NotNull TipoTarea tipo,
        @NotNull Prioridad prioridad,
        @NotNull Long expedienteId,
        @NotNull Long responsableId,
        @NotNull @Future LocalDate fechaLimite
) {}
