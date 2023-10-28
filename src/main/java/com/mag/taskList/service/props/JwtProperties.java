package com.mag.taskList.service.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Данный класс содержит свойства JWT-токена.
 * Свойства находятся в application.yaml
 */
@Component
@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    /**
     * Секрет, который необходим серверу
     * (для подписания токенов)
     */
    private String secret;
    /**
     * Срок жизни access_token
     */
    private long access;
    /**
     * Срок жизни refresh_token
     */
    private long refresh;

}
