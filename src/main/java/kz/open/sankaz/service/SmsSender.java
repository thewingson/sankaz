package kz.open.sankaz.service;

import kz.open.sankaz.model.NotificationTemplate;

import java.util.Map;

public interface SmsSender {
    void sendSms(String phoneNumberTo, String phoneNumberFrom,  String message);
    void sendSms(NotificationTemplate template, Map<String, Object> params);
}
