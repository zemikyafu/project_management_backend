package org.project_management.application.services.Invitation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.project_management.application.exceptions.BadRequestException;
import org.project_management.application.exceptions.EmailException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public boolean sendEmail(String recipientEmail, String subject, String invitationText, String invitationUrl,String token) {
        if (recipientEmail == null || recipientEmail.isEmpty()) {
            throw new BadRequestException("Recipient email cannot be null or empty");
        }

        String htmlContent = "<html><body style='font-family: Arial, sans-serif; background-color: #f4f4f9; padding: 20px;'>"
                + "<div style='max-width: 600px; margin: 0 auto; background-color: white; "
                + "padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>"
                + "<h2 style='color: #333;'>You're Invited to Join Our Platform!</h2>"
                + "<p style='font-size: 16px; color: #555;'>"
                + invitationText
                + "</p>"
                + "<div style='text-align: center; margin-top: 30px;'>"
                + "<a href='" + invitationUrl + "' style='font-size: 16px; padding: 12px 30px; background-color: "
                +"#4CAF50; color: white; text-decoration: none; border-radius: 25px; font-weight: bold; display: inline-block;'>Accept Invitation</a>"
                + "</div>"
                + "<p style='font-size: 12px; color: #aaa; margin-top: 30px;'>This is an automated email, please do not reply.</p>"
                + "</div>"
                + "</body></html>";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException | MailException e) {
            throw new EmailException("Failed to send email.");
        }
        return true;
    }
}
