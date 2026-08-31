package com.utp.deadlineflow.service;

import com.utp.deadlineflow.entity.EstadoPlazo;
import com.utp.deadlineflow.entity.Plazo;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreRiesgoServiceTest {

    private final ScoreRiesgoService service = new ScoreRiesgoService();

    @Test
    void debeAsignarScoreAltoAPlazoUrgenteConTareasPendientes() {
        Plazo plazo = new Plazo(1L, EstadoPlazo.ACTIVO, LocalDate.now().plusDays(2));

        double score = service.calcular(plazo, 4);

        assertThat(score).isGreaterThan(70.0);
    }

    @Test
    void debeAsignarScoreBajoAPlazoLejanoSinTareasPendientes() {
        Plazo plazo = new Plazo(2L, EstadoPlazo.ACTIVO, LocalDate.now().plusDays(45));

        double score = service.calcular(plazo, 0);

        assertThat(score).isLessThan(30.0);
    }

    @Test
    void debeAsignarScoreMaximoAPlazoVencido() {
        Plazo plazo = new Plazo(3L, EstadoPlazo.ACTIVO, LocalDate.now().minusDays(1));

        double score = service.calcular(plazo, 0);

        assertThat(score).isGreaterThanOrEqualTo(60.0);
    }
}
