package com.utp.deadlineflow.dto.request;

import com.utp.deadlineflow.entity.EstadoTarea;
import jakarta.validation.constraints.NotNull;

public record CambioEstadoTareaRequestDTO(
        @NotNull EstadoTarea nuevoEstado,
        boolean confirmacionSupervisor
) {}
