package com.maxbohoniuk.springrestdemo.users.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRequestDto {
    @NotBlank
    private final String name;

    @Email
    private final String email;

    @NotBlank
    private final String password;

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }
}

