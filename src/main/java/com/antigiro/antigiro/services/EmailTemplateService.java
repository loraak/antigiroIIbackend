package com.antigiro.antigiro.services;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateService {

    public String buildAlertaEmail(String nombreUsuario, Long periodoId, int totalRegistros, int capacidadMaxima, double porcentaje) {
        return """
            <!DOCTYPE html>
            <html lang="es">
                <head>
                    <meta charset="UTF-8"/>
                    <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                    .container { max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 8px;
                    box-shadow: 0 2px 8px rgba(0,0,0,0.1); overflow: hidden; }
                    .header { background-color: #FF8C00; color: white; padding: 24px 32px; }
                    .header h1 { margin: 0; font-size: 22px; }
                    .body { padding: 28px 32px; color: #333; }
                    .body p { line-height: 1.6; }
                    .stat-box { background-color: #FFF3E0; border-left: 4px solid #FF8C00;
                    padding: 16px 20px; border-radius: 4px; margin: 20px 0; }
                    .stat-box p { margin: 6px 0; font-size: 15px; }
                    .progress-bar-bg { background-color: #e0e0e0; border-radius: 8px; height: 18px; margin: 10px 0; }
                    .progress-bar-fill { background-color: #FF8C00; height: 18px; border-radius: 8px;
                    width: %s%%; }
                    .footer { background-color: #f0f0f0; text-align: center; padding: 16px;
                    font-size: 12px; color: #888; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                        <h1>Alerta: Periodo Casi Lleno</h1>
                        </div>
                        <div class="body">
                        <p>Hola <strong>%s</strong>,</p>
                        <p>El <strong>Periodo #%d</strong> ha alcanzado el <strong>%.1f%%</strong> de su capacidad.
                            Te recomendamos revisar el estado del periodo y considerar cerrarlo pronto.</p>
                        <div class="stat-box">
                            <p>Registros actuales: <strong>%d</strong></p>
                            <p>Capacidad máxima: <strong>%d</strong></p>
                            <p>Porcentaje de llenado: <strong>%.1f%%</strong></p>
                            <div class="progress-bar-bg">
                            <div class="progress-bar-fill"></div>
                            </div>
                        </div>
                        <p>Por favor ingresa al sistema para gestionar el periodo.</p>
                        </div>
                        <div class="footer">
                        Este correo fue generado automáticamente por el sistema Antigiro. Por favor no responder.
                        </div>
                    </div>
                </body>
            </html>
        """.formatted(
            String.valueOf((int) porcentaje),
            nombreUsuario, periodoId, porcentaje,
            totalRegistros, capacidadMaxima, porcentaje);
    }

    public String buildPeriodoEmail(String nombreUsuario, Long periodoId, String pesoTotal, int totalRegistros) {
        return """
            <!DOCTYPE html>
            <html lang="es">
                <head>
                    <meta charset="UTF-8"/>
                    <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                    .container { max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 8px;
                    box-shadow: 0 2px 8px rgba(0,0,0,0.1); overflow: hidden; }
                    .header { background-color: #2E7D32; color: white; padding: 24px 32px; }
                    .header h1 { margin: 0; font-size: 22px; }
                    .body { padding: 28px 32px; color: #333; }
                    .body p { line-height: 1.6; }
                    .stat-box { background-color: #E8F5E9; border-left: 4px solid #2E7D32;
                                padding: 16px 20px; border-radius: 4px; margin: 20px 0; }
                    .stat-box p { margin: 6px 0; font-size: 15px; }
                    .footer { background-color: #f0f0f0; text-align: center; padding: 16px;
                    font-size: 12px; color: #888; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                        <h1>Periodo Cerrado Exitosamente</h1>
                        </div>
                        <div class="body">
                        <p>Hola <strong>%s</strong>,</p>
                        <p>El <strong>Periodo #%d</strong> ha sido cerrado. A continuación el resumen final:</p>
                        <div class="stat-box">
                            <p>Total de registros: <strong>%d</strong></p>
                            <p>Peso total recolectado: <strong>%s kg</strong></p>
                        </div>
                        <p>Puedes consultar el detalle completo del periodo en el sistema.</p>
                        </div>
                        <div class="footer">
                        Este correo fue generado automáticamente por el sistema Antigiro. Por favor no responder.
                        </div>
                    </div>
                </body>
            </html>
        """.formatted(nombreUsuario, periodoId, totalRegistros, pesoTotal);
    }
}
