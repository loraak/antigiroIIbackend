package com.antigiro.antigiro.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.antigiro.antigiro.models.InventarioResiduo;
import com.antigiro.antigiro.models.PeriodoRecoleccion;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.destinatario}")
    private String destinatario;

    public void enviarResumenPeriodo(PeriodoRecoleccion periodo, List<InventarioResiduo> inventario) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("Período #" + periodo.getId() + " cerrado - Resumen de inventario");
        mensaje.setText(construirCuerpo(periodo, inventario));
        mailSender.send(mensaje);
    }

    private String construirCuerpo(PeriodoRecoleccion periodo, List<InventarioResiduo> inventario) {
        StringBuilder sb = new StringBuilder();
        sb.append("RESUMEN DEL PERÍODO #").append(periodo.getId()).append("\n\n");
        sb.append("Fecha inicio: ").append(periodo.getFechaIngreso()).append("\n");
        sb.append("Fecha cierre: ").append(periodo.getFechaCierre()).append("\n");
        sb.append("Peso total acumulado: ").append(periodo.getPesoTotal()).append(" g\n\n");
        sb.append("DETALLE DE RESIDUOS:\n");
        sb.append("─────────────────────────────────\n");

        for (InventarioResiduo item : inventario) {
            sb.append("• ").append(item.getResiduo().getNombre())
                .append(" | Cantidad: ").append(item.getCantidad())
                .append(" | Peso total: ").append(item.getPesoTotal()).append(" g\n");
        }

        sb.append("─────────────────────────────────\n");
        sb.append("Cerrado por: ").append(
            periodo.getUsuarioCierre() != null 
                ? periodo.getUsuarioCierre().getNombre() 
                : "Sistema automático"
        );
        return sb.toString();
    }
} 
