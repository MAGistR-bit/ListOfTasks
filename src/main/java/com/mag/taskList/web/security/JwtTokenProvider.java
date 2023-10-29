package com.mag.taskList.web.security;

import com.mag.taskList.domain.exception.AccessDeniedException;
import com.mag.taskList.domain.user.Role;
import com.mag.taskList.domain.user.User;
import com.mag.taskList.service.UserService;
import com.mag.taskList.service.props.JwtProperties;
import com.mag.taskList.web.dto.auth.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Данный сервис обеспечивает взаимодействие с токенами
 */
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private Key key;

    /**
     * Инициализация ключа
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    /**
     * Создает access_token для пользователя
     *
     * @param userId   идентификатор пользователя
     * @param username логин пользователя
     * @param roles    роли
     * @return access_token
     */
    public String createAccessToken(Long userId,
                                    String username,
                                    Set<Role> roles) {

        // Хранит информацию о пользователе (в token)
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);
        claims.put("roles", resolveRoles(roles));

        // Дата создания токена
        Date now = new Date();
        // Время, когда токен перестанет быть действительным
        Date validity = new Date(now.getTime() + jwtProperties.getAccess());

        // Собрать информацию в токен
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)           // установить дату создания токена
                .setExpiration(validity)    // установить срок жизни токена
                .signWith(key)              // подписать токен (с помощью ключа)
                .compact();                 // сбор данных

    }

    /**
     * Преобразует Set в List
     *
     * @param roles роли, которые могут быть у пользователя
     * @return роли, хранящиеся в List
     */
    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }


    /**
     * Создает refresh_token
     *
     * @param userId   идентификатор пользователя
     * @param username логин пользователя
     * @return refresh_token, представляющий собой строку
     */
    public String createRefreshToken(Long userId,
                                     String username) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);

        // Дата создания токена
        Date now = new Date();
        // Срок жизни токена
        Date validity = new Date(now.getTime() + jwtProperties.getRefresh());


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }


    /**
     * Обновляет пару токенов (access, refresh)
     *
     * @param refreshToken refresh_token, который уже имеется у пользователя
     * @return объект JwtResponse (пара токенов)
     */
    public JwtResponse refreshUserTokens(String refreshToken) {

        JwtResponse jwtResponse = new JwtResponse();

        // Проверка на валидность refresh_token
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException();
        }

        // Извлекаем id из refresh_token
        Long userId = Long.valueOf(getId(refreshToken));
        // Получаем пользователя
        User user = userService.getById(userId);

        // Устанавливаем необходимые данные
        jwtResponse.setId(userId);
        jwtResponse.setUsername(user.getUsername());

        jwtResponse.setAccessToken(
                createAccessToken(userId, user.getUsername(), user.getRoles())
        );

        jwtResponse.setRefreshToken(
                createRefreshToken(userId, user.getUsername())
        );

        return jwtResponse;
    }


    /**
     * Проверяет токен на валидность
     *
     * @param token токен, который необходимо проверить
     * @return true - токен является действительным,
     * false - токен не является действительным
     */
    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return !claims.getBody().getExpiration().before(new Date());
    }


    /**
     * Получает id из токена
     *
     * @param token токен, имеющийся в системе
     * @return идентификатор (id) токена
     */
    private String getId(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()                  // получить тело токена
                .get("id")                  // получить объект по ключу (id)
                .toString();                // возвратить строковый объект
    }

    /**
     * Предоставляем Spring Security информацию
     * о пользователе, которого мы проверили.
     * @param token токен
     * @return {@link Authentication}
     */
    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());

    }

    /**
     * Получает логин пользователя (username) из токена
     * @param token токен, который имеется в системе
     * @return логин пользователя
     */
    private String getUsername(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();            // получить subject, поскольку там хранится username
    }
}
