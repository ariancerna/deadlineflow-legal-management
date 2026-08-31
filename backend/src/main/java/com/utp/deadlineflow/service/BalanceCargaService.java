package com.utp.deadlineflow.service;

import com.utp.deadlineflow.dto.response.CargaTrabajoResponseDTO;
import com.utp.deadlineflow.entity.Usuario;
import com.utp.deadlineflow.repository.TareaRepository;
import com.utp.deadlineflow.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BalanceCargaService {

    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${deadlineflow.balance-carga.umbral-tareas-activas:8}")
    private int umbralTareasActivas;

    public BalanceCargaService(TareaRepository tareaRepository, UsuarioRepository usuarioRepository) {
        this.tareaRepository = tareaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /** Reto "Diferencial Wow": balanceador visual de carga con detección de sobreumbral. */
    public List<CargaTrabajoResponseDTO> calcularBalance() {
        Map<Long, Usuario> usuarios = usuarioRepository.findAll().stream()
                .collect(Collectors.toMap(Usuario::getId, u -> u));

        return tareaRepository.contarTareasActivasPorResponsable().stream()
                .map(fila -> {
                    Long responsableId = (Long) fila[0];
                    long carga = (long) fila[1];
                    Usuario usuario = usuarios.get(responsableId);
                    return new CargaTrabajoResponseDTO(
                            responsableId,
                            usuario != null ? usuario.getNombre() : "Desconocido",
                            carga,
                            umbralTareasActivas,
                            carga > umbralTareasActivas
                    );
                })
                .collect(Collectors.toList());
    }
}
