package com.maxbohoniuk.springkotlindemo.users

import com.maxbohoniuk.springkotlindemo.users.model.UserRequestDto
import com.maxbohoniuk.springkotlindemo.users.model.UserResponseDto
import com.maxbohoniuk.springkotlindemo.users.service.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getUsers(): List<UserResponseDto> = userService.getUsers().map { UserResponseDto(it) }

    @GetMapping("/{uuid}")
    fun getUser(@PathVariable(name = "uuid") uuid: UUID): UserResponseDto {
        return UserResponseDto(userService.getUserByUUID(uuid))
    }

    @PostMapping
    fun createUser(@RequestBody @Valid user: UserRequestDto): UserResponseDto =
        UserResponseDto(userService.createUser(user.toEntity()))
}