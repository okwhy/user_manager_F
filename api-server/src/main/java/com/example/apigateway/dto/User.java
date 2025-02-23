package com.example.apigateway.dto;

import lombok.Data;

@Data
public class User {
    private Long id;

    private String name;

    private String email;

    private int age;

}
