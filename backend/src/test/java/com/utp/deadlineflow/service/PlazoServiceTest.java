package com.utp.deadlineflow.service;

import com.utp.deadlineflow.entity.EstadoPlazo;
import com.utp.deadlineflow.entity.Plazo;
import com.utp.deadlineflow.exception.OperacionNoPermitidaException;
import com.utp.deadlineflow.repository.ExpedienteRepository;
import com.utp.deadlineflow.repository.PlazoRepository;
import com.utp.deadlineflow.repository.TareaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlazoServiceTest {

    @Mock private PlazoRepository plazoRepository;
    @Mock private ExpedienteRepository expedienteRepository;
    @Mock private TareaRepository tareaRepository;
    @Mock private BitacoraService bitacoraService;
    @Mock private ScoreRiesgoService scoreRiesgoService;

    @InjectMocks private PlazoService plazoService;

    @Test
    void debeAnularPlazoSinEliminarloFisicamente() {
        Plazo plazo = new Plazo(1L, EstadoPlazo.ACTIVO, LocalDate.now().plusDays(5));
        when(plazoRepository.findById(1L)).thenReturn(Optional.of(plazo));
        when(plazoRepository.save(any(Plazo.class))).thenAnswer(inv -> inv.getArgument(0));

        var resultado = plazoService.anular(1L, "Cliente desistió del proceso");

        assertThat(resultado.estado()).isEqualTo(EstadoPlazo.ANULADO);
        verify(plazoRepository, never()).deleteById(anyLong());
        verify(plazoRepository, never()).delete(any());
        verify(bitacoraService).registrar(eq(1L), any(), anyString());
    }

    @Test
    void debeRechazarAnulacionSinMotivo() {
        assertThatThrownBy(() -> plazoService.anular(1L, ""))
                .isInstanceOf(OperacionNoPermitidaException.class)
                .hasMessageContaining("motivo");

        verifyNoInteractions(plazoRepository);
    }

    @Test
    void debeLanzarExcepcionSiElPlazoNoExiste() {
        when(plazoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> plazoService.anular(99L, "Motivo válido de más de 10 caracteres"))
                .isInstanceOf(com.utp.deadlineflow.exception.RecursoNoEncontradoException.class);
    }
}
