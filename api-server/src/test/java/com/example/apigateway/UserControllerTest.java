package com.example.apigateway;

import com.example.apigateway.controller.UserController;
import com.example.apigateway.dto.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RestTemplate restTemplate; // Мок RestTemplate

    @InjectMocks
    private UserController userController; // Тестируемый контроллер


    private final String coreServiceUrl="http://core-server:8081/api/users"; // URL Core сервиса

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        // Инициализация MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Инициализация тестовых данных
        user1 = new User(1L, "John Doe", "john.doe@example.com", 25);
        user2 = new User(2L, "Jane Doe", "jane.doe@example.com", 30);
//        ReflectionTestUtils.setField(userController, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(userController, "coreServiceUrl", "http://core-server:8081/api/users");
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Подготовка моков
        List<User> users = Arrays.asList(user1, user2);
        ResponseEntity<List<User>> responseEntity = new ResponseEntity<>(users, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(coreServiceUrl),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<List<User>>>any()
        )).thenReturn(responseEntity);

        // Вызов метода и проверка результата
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].age").value(25))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"))
                .andExpect(jsonPath("$[1].email").value("jane.doe@example.com"))
                .andExpect(jsonPath("$[1].age").value(30));

        // Проверка вызова метода RestTemplate
        verify(restTemplate, times(1)).exchange(
                eq(coreServiceUrl),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<List<User>>>any()
        );
    }

    @Test
    void testGetUserById() throws Exception {
        // Подготовка моков
        String url = coreServiceUrl + "/1";
        ResponseEntity<User> responseEntity = new ResponseEntity<>(user1, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.GET),
                isNull(),
                eq(User.class))
        ).thenReturn(responseEntity);

        // Вызов метода и проверка результата
        mockMvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(25));

        // Проверка вызова метода RestTemplate
        verify(restTemplate, times(1)).exchange(
                eq(url),
                eq(HttpMethod.GET),
                isNull(),
                eq(User.class)
        );
    }

    @Test
    void testCreateUser() throws Exception {
        // Подготовка моков
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> entity = new HttpEntity<>(user1, headers);
        ResponseEntity<User> responseEntity = new ResponseEntity<>(user1, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(coreServiceUrl),
                eq(HttpMethod.POST),
                eq(entity),
                eq(User.class))
        ).thenReturn(responseEntity);

        // Вызов метода и проверка результата
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(25));

        // Проверка вызова метода RestTemplate
        verify(restTemplate, times(1)).exchange(
                eq(coreServiceUrl),
                eq(HttpMethod.POST),
                eq(entity),
                eq(User.class)
        );
    }

    @Test
    void testDeleteUser() throws Exception {
        // Подготовка моков
        String url = coreServiceUrl + "/1";
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);

        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.DELETE),
                isNull(),
                eq(Void.class))
        ).thenReturn(responseEntity);

        // Вызов метода и проверка результата
        mockMvc.perform(delete("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Проверка вызова метода RestTemplate
        verify(restTemplate, times(1)).exchange(
                eq(url),
                eq(HttpMethod.DELETE),
                isNull(),
                eq(Void.class)
        );
    }
}