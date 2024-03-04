package com.maxbohoniuk.springrestdemo.users.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@Builder
public class UserResponseDto {
    private UUID id;
    private String name;
    private String email;
    private Set<String> groups;
    private LocalDateTime createdAt;
}

