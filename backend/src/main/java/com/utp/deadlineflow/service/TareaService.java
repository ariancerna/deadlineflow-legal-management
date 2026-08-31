package com.utp.deadlineflow.service;

import com.utp.deadlineflow.dto.request.TareaRequestDTO;
import com.utp.deadlineflow.dto.response.TareaResponseDTO;
import com.utp.deadlineflow.entity.*;
import com.utp.deadlineflow.exception.OperacionNoPermitidaException;
import com.utp.deadlineflow.exception.RecursoNoEncontradoException;
import com.utp.deadlineflow.exception.TareaDuplicadaException;
import com.utp.deadlineflow.mapper.TareaMapper;
import com.utp.deadlineflow.repository.ExpedienteRepository;
import com.utp.deadlineflow.repository.TareaRepository;
import com.utp.deadlineflow.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TareaService {

    private static final List<EstadoTarea> ESTADOS_NO_ACTIVOS = List.of(EstadoTarea.CERRADA, EstadoTarea.ANULADA);

    private final TareaRepository tareaRepository;
    private final ExpedienteRepository expedienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final BitacoraService bitacoraService;

    @Autowired
    public TareaService(TareaRepository tareaRepository,
                         ExpedienteRepository expedienteRepository,
                         UsuarioRepository usuarioRepository,
                         BitacoraService bitacoraService) {
        this.tareaRepository = tareaRepository;
        this.expedienteRepository = expedienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.bitacoraService = bitacoraService;
    }

    // Constructor simplificado para pruebas unitarias que no requieren todas las dependencias
    public TareaService(TareaRepository tareaRepository) {
        this(tareaRepository, null, null, null);
    }

    /** Regla R4: valida duplicidad de título+tipo dentro del mismo expediente activo. */
    @Transactional
    public TareaResponseDTO crear(TareaRequestDTO request) {
        boolean existeDuplicada = tareaRepository.existsByTituloNormalizadoAndTipoAndExpedienteIdAndEstadoNotIn(
                request.titulo(), request.tipo(), request.expedienteId(), ESTADOS_NO_ACTIVOS);

        if (existeDuplicada) {
            throw new TareaDuplicadaException(
                    "Ya existe una tarea activa con el mismo título y tipo en este expediente");
        }

        Expediente expediente = expedienteRepository.findById(request.expedienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Expediente no encontrado: " + request.expedienteId()));

        Usuario responsable = usuarioRepository.findById(request.responsableId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Responsable no encontrado: " + request.responsableId()));

        Tarea tarea = Tarea.builder()
                .titulo(request.titulo())
                .tipo(request.tipo())
                .prioridad(request.prioridad())
                .estado(EstadoTarea.PENDIENTE)
                .expediente(expediente)
                .responsable(responsable)
                .fechaLimite(request.fechaLimite())
                .confirmacionSupervisor(false)
                .esDuplicada(false)
                .build();

        Tarea guardada = tareaRepository.save(tarea);
        bitacoraService.registrar(guardada.getId(), TipoEvento.CREACION_TAREA,
                "Tarea creada: " + guardada.getTitulo());

        return TareaMapper.toResponse(guardada, 0.0);
    }

    /** Regla R3: tareas de prioridad ALTA exigen confirmación del supervisor para cerrarse. */
    @Transactional
    public Tarea cambiarEstado(Tarea tarea, EstadoTarea nuevoEstado, boolean confirmacionSupervisor) {
        if (nuevoEstado == EstadoTarea.CERRADA
                && tarea.getPrioridad() == Prioridad.ALTA
                && !confirmacionSupervisor) {
            throw new OperacionNoPermitidaException(
                    "Las tareas críticas requieren confirmación del supervisor para cerrarse");
        }

        tarea.setEstado(nuevoEstado);
        tarea.setConfirmacionSupervisor(confirmacionSupervisor);

        if (bitacoraService != null) {
            bitacoraService.registrar(tarea.getId(), TipoEvento.CAMBIO_ESTADO_TAREA,
                    "Nuevo estado: " + nuevoEstado);
        }

        // La entidad gestionada ya contiene el cambio; devolverla evita depender del
        // valor de retorno del repositorio y conserva el resultado en pruebas unitarias.
        if (tareaRepository != null) {
            tareaRepository.save(tarea);
        }
        return tarea;
    }
}
