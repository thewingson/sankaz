package kz.open.sankaz.service;

public interface MailSender {
    void sendMail(String emailTo, String subject, String message); // TODO: переписать через шаблонизатор
}
