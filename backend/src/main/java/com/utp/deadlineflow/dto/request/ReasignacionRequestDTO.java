package com.utp.deadlineflow.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReasignacionRequestDTO(
        @NotNull Long tareaId,
        @NotNull Long nuevoResponsableId
) {}
