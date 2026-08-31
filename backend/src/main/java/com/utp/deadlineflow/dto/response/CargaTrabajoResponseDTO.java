package com.utp.deadlineflow.dto.response;

public record CargaTrabajoResponseDTO(
        Long responsableId,
        String responsableNombre,
        long cargaActual,
        int umbralInterno,
        boolean sobreUmbral
) {}
