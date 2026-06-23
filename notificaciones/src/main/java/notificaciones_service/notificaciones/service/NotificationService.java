package notificaciones_service.notificaciones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import notificaciones_service.notificaciones.DTO.NotificationRequest;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(NotificationRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getDestinatario());
        message.setSubject(request.getAsunto());
        message.setText(request.getCuerpo());
        message.setFrom("no-reply@tuuniversidad.com");

        mailSender.send(message);
        System.out.println("Correo enviado con éxito a: " + request.getDestinatario());
    }
}