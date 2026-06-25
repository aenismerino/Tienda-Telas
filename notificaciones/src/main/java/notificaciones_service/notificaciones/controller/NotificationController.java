package notificaciones_service.notificaciones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import notificaciones_service.notificaciones.DTO.NotificationRequest;
import notificaciones_service.notificaciones.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> enviarNotificacion(@RequestBody NotificationRequest request) {
        try {
            notificationService.enviarCorreo(request);
            return ResponseEntity.ok("Notificación enviada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar: " + e.getMessage());
        }
    }
}
