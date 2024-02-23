package com.maxbohoniuk.springkotlindemo.users.service

import com.maxbohoniuk.springkotlindemo.users.model.User
import com.maxbohoniuk.springkotlindemo.users.repo.UserReposiroty
import org.springframework.http.HttpStatusCode
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class UserService(
    private val userReposiroty: UserReposiroty,
    private val passwordEncoder: PasswordEncoder
): UserDetailsService {

    fun getUserByUUID(uuid: UUID): User {
        return userReposiroty.findById(uuid)
            .orElseThrow { ResponseStatusException(HttpStatusCode.valueOf(404), "User not exist") }
    }

    fun createUser(user: User): User {
        return userReposiroty.findByEmailIgnoreCase(user.email)
            .orElseGet { userReposiroty.save(user.copy(password = passwordEncoder.encode(user.password))) }
    }

    fun getUsers(): List<User> = userReposiroty.findAll()
    override fun loadUserByUsername(username: String?): UserDetails {
        return userReposiroty.findByEmailIgnoreCase(username ?: "").orElseThrow()
    }

    fun getLoggedInUser(): User {
        val email = SecurityContextHolder.getContext().authentication.name

        return userReposiroty.findByEmailIgnoreCase(email).orElseThrow()
    }

}