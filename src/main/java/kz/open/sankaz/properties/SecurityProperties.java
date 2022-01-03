package kz.open.sankaz.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@NoArgsConstructor
@Configuration
public class SecurityProperties {
    @Value("${security.token.secret}")
    private String securityTokenSecret;
}
