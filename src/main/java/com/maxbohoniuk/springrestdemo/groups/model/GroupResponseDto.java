package com.maxbohoniuk.springrestdemo.groups.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class GroupResponseDto {

    private UUID id;

    private String name;

    private String creator;

    private Set<String> members;

    private Integer membersCount;

    private LocalDateTime createdAt;
}
