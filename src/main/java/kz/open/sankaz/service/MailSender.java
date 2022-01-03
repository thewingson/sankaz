package kz.open.sankaz.service;

import kz.open.sankaz.model.NotificationTemplate;

import java.util.Map;

public interface MailSender {
    void sendMail(String emailFrom, String emailTo, String subject, String message);
    void sendMail(NotificationTemplate template, Map<String, Object> params);
}
