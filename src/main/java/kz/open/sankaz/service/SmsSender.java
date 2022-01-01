package kz.open.sankaz.service;

public interface SmsSender {
    void sendSms(String phoneNumber, String message); // TODO: переписать через шаблонизатор
}
