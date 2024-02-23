package com.maxbohoniuk.springrestdemo.users.model;

import com.maxbohoniuk.springrestdemo.groups.model.GroupResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
public class UserResponseDto {
    private UUID id;
    private String name;
    private String email;
    private Set<GroupResponseDto> groups;
    private LocalDateTime createdAt;
}

