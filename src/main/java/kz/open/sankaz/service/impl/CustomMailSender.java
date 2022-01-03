package kz.open.sankaz.service.impl;

import kz.open.sankaz.properties.MailProperties;
import kz.open.sankaz.properties.NotificationProperties;
import kz.open.sankaz.model.NotificationTemplate;
import kz.open.sankaz.service.MailSender;
import kz.open.sankaz.util.TemplateProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class CustomMailSender implements MailSender {

    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private NotificationProperties notificationProperties;

    private final JavaMailSender mailSender;

    public CustomMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMail(String emailFrom, String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    @Override
    public void sendMail(NotificationTemplate template, Map<String, Object> params) {
        log.info("Start of sending Email with template {}", template.getCode());
        if(!validateEmailParams(params)){
            log.error(notificationProperties.getNotificationParamsIsNotValid());
            throw new RuntimeException(notificationProperties.getNotificationParamsIsNotValid());
        }
        String resultMessage = TemplateProcessor.processTemplate(template.getMessageTemplate(), params);

        sendMail((String) params.get(notificationProperties.getNotificationParamsKeysPhoneNumberTo()),
                (String) params.get(notificationProperties.getNotificationParamsKeysEmailTo()),
                template.getName(),
                resultMessage);
        log.info("End of sending Email with template {}", template.getCode());
    }

    private boolean validateEmailParams(Map<String, Object> params) {
        log.info("Start of validating Template Params...");
        // some logic
        log.info("End of validating Template Params...");
        return true; // TODO: validate through Spring Validator of Simple IF-ELSE
    }
}
