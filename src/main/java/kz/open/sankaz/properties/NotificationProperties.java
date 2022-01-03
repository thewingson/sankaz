package kz.open.sankaz.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@NoArgsConstructor
@Configuration
public class NotificationProperties {
    @Value("${notification.error.template.not-valid}")
    private String notificationTemplateIsNotValid;

    @Value("${notification.error.params.not-valid}")
    private String notificationParamsIsNotValid;

    @Value("${notification.params.keys.phone-number-from}")
    private String notificationParamsKeysPhoneNumberFrom;

    @Value("${notification.params.keys.phone-number-to}")
    private String notificationParamsKeysPhoneNumberTo;

    @Value("${notification.params.keys.email-from}")
    private String notificationParamsKeysEmailFrom;

    @Value("${notification.params.keys.email-to}")
    private String notificationParamsKeysEmailTo;
}
