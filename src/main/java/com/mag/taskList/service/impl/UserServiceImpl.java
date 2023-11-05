package com.mag.taskList.service.impl;

import com.mag.taskList.domain.exception.ResourceNotFoundException;
import com.mag.taskList.domain.user.Role;
import com.mag.taskList.domain.user.User;
import com.mag.taskList.repository.UserRepository;
import com.mag.taskList.service.UserService;
import lombok.RequiredArgsConstructor;
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
    public User getByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

    }

    /**
     * Обновляет информацию о пользователе
     *
     * @param user пользователь, информацию о котором, следует
     *             обновить
     * @return пользователь с обновленной информацией
     */
    @Override
    @Transactional
    public User update(User user) {
        // Хэшируем "сырой" пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.update(user);
        return user;
    }

    /**
     * Добавляет нового пользователя в систему
     * @param user пользователь, который должен быть добавлен (создан)
     * @return пользователь
     */
    @Override
    @Transactional
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
    public boolean isTaskOwner(Long userId, Long taskId) {
        return userRepository.isTaskOwner(userId, taskId);
    }

    /**
     * Удаляет пользователя по его идентификатору
     *
     * @param id идентификатор пользователя
     */
    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.delete(id);
    }

}
