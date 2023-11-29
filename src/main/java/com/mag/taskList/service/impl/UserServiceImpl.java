package com.mag.taskList.service.impl;

import com.mag.taskList.domain.exception.ResourceNotFoundException;
import com.mag.taskList.domain.user.Role;
import com.mag.taskList.domain.user.User;
import com.mag.taskList.repository.UserRepository;
import com.mag.taskList.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    /**
     * Пароль будем хэшировать
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Поиск пользователя
     * по идентификатору. Если пользователь не найден,
     * отображается исключение {@link ResourceNotFoundException}
     *
     * @param id идентификатор пользователя
     * @return пользователь (объект класса User)
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getById", key = "#id")
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    /**
     * Получает пользователя по username (электронная почта).
     * Если пользователь не найден, отображается исключение
     * {@link ResourceNotFoundException}
     *
     * @param username никнейм пользователя
     * @return пользователь
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getByUsername", key = "#username")
    public User getByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

    }

    /**
     * Обновляет информацию о пользователе. Этот метод заменяет старую информацию
     * о пользователе на новую (в кэше), поэтому используется put.
     *
     * @param user пользователь, информацию о котором, следует
     *             обновить
     * @return пользователь с обновленной информацией
     */
    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = "UserService::getById", key = "#user.id"),
            @CachePut(value = "UserService::getByUsername", key = "#user.username"),

    })
    public User update(User user) {
        // Хэшируем "сырой" пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.update(user);
        return user;
    }

    /**
     * Добавляет нового пользователя в систему.
     * Поскольку пользователи будут добавляться, используется cacheable.
     *
     * @param user пользователь, который должен быть добавлен (создан)
     * @return пользователь
     */
    @Override
    @Transactional
    @Caching(cacheable = {
            @Cacheable(value = "UserService::getById", key = "#user.id"),
            @Cacheable(value = "UserService::getByUsername", key = "#user.username"),
    })
    public User create(User user) {

        // Проверяем существует ли такой пользователь в системе
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }

        // Проверяем, совпадают ли пароли
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException("Password and password confirmation " +
                    "do not match.");
        }

        // Хеширование пароля пользователя
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.create(user);
        // Задаем роль пользователя (по умолчанию) при создании
        Set<Role> roles = Set.of(Role.ROLE_USER);
        // Сохранить роль пользователя в БД
        userRepository.insertUserRole(user.getId(), Role.ROLE_USER);
        // Сохранить указанную роль в объект
        user.setRoles(roles);

        return user;
    }

    /**
     * Проверяет, что пользователь является владельцем задачи
     *
     * @param userId идентификатор пользователя
     * @param taskId идентификатор задачи
     * @return true - задача принадлежит пользователю,
     * false - задача не принадлежит пользователю
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::isTaskOwner", key = "#userId + '.' + #taskId")
    public boolean isTaskOwner(Long userId, Long taskId) {
        return userRepository.isTaskOwner(userId, taskId);
    }

    /**
     * Удаляет пользователя по его идентификатору.
     * Помимо этого, метод удаляет данные из кэша, используя
     * {@link CacheEvict}
     *
     * @param id идентификатор пользователя
     */
    @Override
    @Transactional
    @CacheEvict(value = "UserService::getById", key = "#id")
    public void delete(Long id) {
        userRepository.delete(id);
    }

}
