package com.maxbohoniuk.springrestdemo.users.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserResponseDto {
    private UUID id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}

