package com.utp.deadlineflow.service;

import com.utp.deadlineflow.entity.Plazo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Reto "Diferencial Wow": Score de Riesgo de Plazo.
 * Combina urgencia (días restantes) y carga de tareas pendientes asociadas al expediente.
 * Escala 0-100, donde valores más altos indican mayor riesgo de incumplimiento.
 */
@Service
public class ScoreRiesgoService {

    private static final int VENTANA_URGENCIA_DIAS = 30;

    public double calcular(Plazo plazo, int tareasPendientes) {
        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), plazo.getFechaLimite());

        double factorUrgencia = diasRestantes <= 0
                ? 100.0
                : Math.max(0.0, 100.0 * (1.0 - ((double) diasRestantes / VENTANA_URGENCIA_DIAS)));

        double factorCarga = Math.min(100.0, tareasPendientes * 15.0);

        // Ponderación: 65% urgencia por fecha, 35% carga de tareas pendientes
        double score = (factorUrgencia * 0.65) + (factorCarga * 0.35);

        return Math.round(Math.min(100.0, Math.max(0.0, score)) * 100.0) / 100.0;
    }
}
