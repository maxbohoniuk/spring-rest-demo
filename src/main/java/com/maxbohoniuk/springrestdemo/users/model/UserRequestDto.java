package com.maxbohoniuk.springkotlindemo.users.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserRequestDto(
    @NotBlank
    val name: String,
    @Email
    val email: String,
    @NotBlank
    val password: String,
) {
    fun toEntity(): User = User(id = null, name = name, email = email, password = password)
}
