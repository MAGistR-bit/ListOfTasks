package com.mag.taskList.web.dto.auth;

import lombok.Data;

/**
 * JwtResponse создаем самостоятельно, поэтому
 * валидация не требуется.
 */
@Data
public class JwtResponse {

    private Long id;
    private String username;
    private String accessToken;
    private String refreshToken;
}
