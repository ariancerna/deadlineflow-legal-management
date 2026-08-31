package com.utp.deadlineflow.controller;

import com.utp.deadlineflow.dto.request.CambioEstadoTareaRequestDTO;
import com.utp.deadlineflow.dto.request.TareaRequestDTO;
import com.utp.deadlineflow.dto.response.TareaResponseDTO;
import com.utp.deadlineflow.entity.Tarea;
import com.utp.deadlineflow.exception.RecursoNoEncontradoException;
import com.utp.deadlineflow.mapper.TareaMapper;
import com.utp.deadlineflow.repository.TareaRepository;
import com.utp.deadlineflow.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tareas")
public class TareaController {

    private final TareaService tareaService;
    private final TareaRepository tareaRepository;

    public TareaController(TareaService tareaService, TareaRepository tareaRepository) {
        this.tareaService = tareaService;
        this.tareaRepository = tareaRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TareaResponseDTO crear(@Valid @RequestBody TareaRequestDTO request) {
        return tareaService.crear(request);
    }

    @PatchMapping("/{id}/estado")
    public TareaResponseDTO cambiarEstado(@PathVariable Long id,
                                           @Valid @RequestBody CambioEstadoTareaRequestDTO request) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tarea no encontrada: " + id));

        Tarea actualizada = tareaService.cambiarEstado(tarea, request.nuevoEstado(), request.confirmacionSupervisor());
        return TareaMapper.toResponse(actualizada, 0.0);
    }

    @GetMapping("/expediente/{expedienteId}")
    public List<TareaResponseDTO> listarPorExpediente(@PathVariable Long expedienteId) {
        return tareaRepository.findByExpedienteId(expedienteId).stream()
                .map(t -> TareaMapper.toResponse(t, 0.0))
                .toList();
    }
}
