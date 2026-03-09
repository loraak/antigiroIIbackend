package com.antigiro.antigiro.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.antigiro.antigiro.models.PeriodoRecoleccion;
import com.antigiro.antigiro.repositories.InventarioResiduoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacionService {
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;
    private final InventarioResiduoRepository inventarioResiduoRepository;

    @Value("${antigiro.periodo.capacidad-maxima:100}")
    private int capacidadMaxima;

    @Value("${antigiro.periodo.umbral-alerta:90}")
    private double umbralAlerta;

public EmailResultado verificarLlenado(PeriodoRecoleccion periodo) {
        if (periodo == null || periodo.getUsuarioIngreso() == null) {
            return EmailResultado.noEnviado("Periodo o usuario nulo");
        }

        int totalRegistros = inventarioResiduoRepository.countByPeriodoId(periodo.getId());
        double porcentaje = ((double) totalRegistros / capacidadMaxima) * 100.0;

        if (porcentaje >= umbralAlerta) {
            String email = periodo.getUsuarioIngreso().getEmail();
            String nombre = periodo.getUsuarioIngreso().getNombre();

            String subject = String.format(
                    "Alerta: Periodo #%d ha alcanzado el %.0f%% de capacidad",
                    periodo.getId(), porcentaje);

            String body = emailTemplateService.buildAlertaEmail(
                    nombre, periodo.getId(), totalRegistros, capacidadMaxima, porcentaje);

            try {
                emailService.sendHtmlEmail(email, subject, body);
                return EmailResultado.enviado(
                        "ALERTA_LLENADO",
                        email,
                        String.format("Periodo #%d al %.0f%% de capacidad", periodo.getId(), porcentaje));
            } catch (Exception e) {
                log.error("Error al enviar correo de alerta para Periodo #{}: {}", periodo.getId(), e.getMessage());
                return EmailResultado.noEnviado("Error SMTP: " + e.getMessage());
            }
        }

        return EmailResultado.noEnviado("Umbral no alcanzado");
    }

    public EmailResultado notificarCierre(PeriodoRecoleccion periodo) {
        if (periodo == null || periodo.getUsuarioIngreso() == null) {
            return EmailResultado.noEnviado("Periodo o usuario nulo");
        }

        int totalRegistros = inventarioResiduoRepository.countByPeriodoId(periodo.getId());
        String pesoTotal = periodo.getPesoTotal() != null
                ? periodo.getPesoTotal().toPlainString()
                : "0.00";

        String email = periodo.getUsuarioIngreso().getEmail();
        String nombre = periodo.getUsuarioIngreso().getNombre();

        String subject = String.format("Periodo #%d cerrado - Resumen final", periodo.getId());

        String body = emailTemplateService.buildPeriodoEmail(
                nombre, periodo.getId(), pesoTotal, totalRegistros);

        try {
            emailService.sendHtmlEmail(email, subject, body);

            return EmailResultado.enviado(
                    "CIERRE_PERIODO",
                    email,
                    String.format("Periodo #%d cerrado con %d registros y %s kg",
                            periodo.getId(), totalRegistros, pesoTotal));
        } catch (Exception e) {
            log.error("Error al enviar correo de cierre para Periodo #{}: {}", periodo.getId(), e.getMessage());
            return EmailResultado.noEnviado("Error SMTP: " + e.getMessage());
        }
    }

    public record EmailResultado(
            boolean enviado,
            String tipo,
            String destinatario,
            String detalle
    ) {
        public static EmailResultado enviado(String tipo, String destinatario, String detalle) {
            return new EmailResultado(true, tipo, destinatario, detalle);
        }

        public static EmailResultado noEnviado(String detalle) {
            return new EmailResultado(false, null, null, detalle);
        }
    }
}
