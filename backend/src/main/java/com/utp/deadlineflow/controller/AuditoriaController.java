package com.utp.deadlineflow.controller;

import com.utp.deadlineflow.dto.response.AuditoriaResponseDTO;
import com.utp.deadlineflow.service.AuditoriaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/** R2: solo lectura para todos los roles habilitados (incluido AUDITOR). No expone POST/PUT/DELETE. */
@RestController
@RequestMapping("/api/v1/auditoria")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public List<AuditoriaResponseDTO> buscar(
            @RequestParam(required = false) Long expedienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return auditoriaService.buscar(expedienteId, desde, hasta);
    }
}
