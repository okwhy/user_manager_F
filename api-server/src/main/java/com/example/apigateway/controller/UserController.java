package com.example.apigateway.controller;

import com.example.apigateway.dto.User;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final RestTemplate restTemplate;

    @Value("${core.service.url}")
    private String coreServiceUrl;
    @GetMapping
    public List<User> getAllUsers() {
        // Отправляем запрос GET в Core сервис
        ResponseEntity<List<User>> response = restTemplate.exchange(coreServiceUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {} );
        return response.getBody();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        String url = coreServiceUrl +"/"+ id;  // Формируем URL для Core сервиса
        ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, null, User.class);
        return response.getBody();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        // Отправляем запрос POST в Core сервис
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        ResponseEntity<User> response = restTemplate.exchange(coreServiceUrl, HttpMethod.POST, entity, User.class);
        return response.getBody();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        String url =  coreServiceUrl +"/"+ id;  // Формируем URL для Core сервиса
        restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
    }
}
