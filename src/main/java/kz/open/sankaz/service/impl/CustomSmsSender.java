package kz.open.sankaz.service.impl;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import kz.open.sankaz.properties.NotificationProperties;
import kz.open.sankaz.properties.SmsProperties;
import kz.open.sankaz.model.NotificationTemplate;
import kz.open.sankaz.service.SmsSender;
import kz.open.sankaz.util.PhoneUtil;
import kz.open.sankaz.util.TemplateProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CustomSmsSender implements SmsSender {

    @Autowired
    private SmsProperties smsProperties;
    @Autowired
    private NotificationProperties notificationProperties;

    @Override
    public void sendSms(String phoneNumberTo, String phoneNumberFrom,  String message) {
        if (PhoneUtil.validatePhone(phoneNumberTo, null)
                && PhoneUtil.validatePhone(phoneNumberFrom, null) ) {
            PhoneNumber to = new PhoneNumber(phoneNumberTo);
            PhoneNumber from = new PhoneNumber(phoneNumberFrom);
            MessageCreator creator = Message.creator(to, from, message);
            creator.create();
            log.info("Send sms {} to {}", message, phoneNumberTo);
        } else {
            throw new IllegalArgumentException(
                    "Phone number [" + phoneNumberTo + "] is not a valid number"
            );
        }
    }

    @Override
    public void sendSms(NotificationTemplate template, Map<String, Object> params) {
        log.info("Start of sending SMS with template {}", template.getCode());
        if(!validateSmsParams(params)){
            log.error(notificationProperties.getNotificationParamsIsNotValid());
            throw new RuntimeException(notificationProperties.getNotificationParamsIsNotValid());
        }
        String resultMessage = TemplateProcessor.processTemplate(template.getMessageTemplate(), params);

        sendSms((String) params.get(notificationProperties.getNotificationParamsKeysPhoneNumberFrom()),
                (String) params.get(notificationProperties.getNotificationParamsKeysPhoneNumberTo()),
                resultMessage);
        log.info("End of sending SMS with template {}", template.getCode());
    }

    private boolean validateSmsParams(Map<String, Object> params) {
        log.info("Start of validating Template Params...");
        // some logic
        log.info("End of validating Template Params...");
        return true; // TODO: validate through Spring Validator of Simple IF-ELSE
    }
}
