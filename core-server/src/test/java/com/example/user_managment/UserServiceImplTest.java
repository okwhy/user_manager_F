package com.example.user_managment;

import com.example.user_managment.model.User;
import com.example.user_managment.repository.UserRepository;
import com.example.user_managment.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository; // Мок репозитория

    @InjectMocks
    private UserServiceImpl userService; // Тестируемый сервис

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        // Инициализация тестовых данных
        user1 = new User(1L, "John Doe", "john.doe@example.com",28);
        user2 = new User(2L, "Jane Doe", "jane.doe@example.com",29);
    }

    @Test
    void testGetAllUsers() {
        // Подготовка моков
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Вызов метода
        List<User> users = userService.getAllUsers();

        // Проверка результата
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));

        // Проверка вызова метода репозитория
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        // Подготовка моков
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        // Вызов метода
        User foundUser = userService.getUserById(1L);

        // Проверка результата
        assertNotNull(foundUser);
        assertEquals(user1.getId(), foundUser.getId());
        assertEquals(user1.getName(), foundUser.getName());
        assertEquals(user1.getEmail(), foundUser.getEmail());

        // Проверка вызова метода репозитория
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        // Подготовка моков
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Проверка исключения
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.getUserById(99L);
        });

        // Проверка сообщения исключения
        assertEquals("User not found", exception.getReason());

        // Проверка вызова метода репозитория
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void testCreateUser() {
        // Подготовка моков
        when(userRepository.save(user1)).thenReturn(user1);

        // Вызов метода
        User savedUser = userService.createUser(user1);

        // Проверка результата
        assertNotNull(savedUser);
        assertEquals(user1.getId(), savedUser.getId());
        assertEquals(user1.getName(), savedUser.getName());
        assertEquals(user1.getEmail(), savedUser.getEmail());

        // Проверка вызова метода репозитория
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void testDeleteUser() {
        // Вызов метода
        userService.deleteUser(1L);

        // Проверка вызова метода репозитория
        verify(userRepository, times(1)).deleteById(1L);
    }
}
