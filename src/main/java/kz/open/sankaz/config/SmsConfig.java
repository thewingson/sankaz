package kz.open.sankaz.config;

import com.twilio.Twilio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SmsConfig {

    private final SmsProperties smsProperties;

    @Autowired
    public SmsConfig(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
        Twilio.init(
                smsProperties.getAccountSid(),
                smsProperties.getAuthToken()
        );
        log.info("Twilio initialized ... with account sid {} ", smsProperties.getAccountSid());
    }
}
