package com.maxbohoniuk.springkotlindemo.users.model

import java.time.LocalDateTime
import java.util.*

data class UserResponseDto(
    val id: UUID,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime?
) {
    constructor(user: User) : this(user.id!!, user.name, user.email, user.createdAt)
}
