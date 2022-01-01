package kz.open.sankaz.service.impl;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import kz.open.sankaz.config.SmsProperties;
import kz.open.sankaz.service.SmsSender;
import kz.open.sankaz.util.PhoneUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomSmsSender implements SmsSender {

    private final SmsProperties smsProperties;

    @Autowired
    public CustomSmsSender(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    @Override
    public void sendSms(String phoneNumber, String message) {
        if (PhoneUtil.validatePhone(phoneNumber, null)) {
            PhoneNumber to = new PhoneNumber(phoneNumber);
            PhoneNumber from = new PhoneNumber(smsProperties.getTrialNumber());
            MessageCreator creator = Message.creator(to, from, message);
            creator.create();
            log.info("Send sms {} to {}", message, phoneNumber);
        } else {
            throw new IllegalArgumentException(
                    "Phone number [" + phoneNumber + "] is not a valid number"
            );
        }
    }
}
