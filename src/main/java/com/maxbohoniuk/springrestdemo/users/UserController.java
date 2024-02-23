package com.maxbohoniuk.springrestdemo.users;


import com.maxbohoniuk.springrestdemo.users.model.User;
import com.maxbohoniuk.springrestdemo.users.model.UserRequestDto;
import com.maxbohoniuk.springrestdemo.users.model.UserResponseDto;
import com.maxbohoniuk.springrestdemo.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getUsers() {
        return userService.getUsers().stream().map(User::toResponseDto).toList();
    }

    @GetMapping("/{uuid}")
    public UserResponseDto getUser(@PathVariable(name = "uuid") UUID uuid) {
        return userService.getUserByUUID(uuid).toResponseDto();
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody @Valid UserRequestDto user) {
        return userService.createUser(user.toEntity()).toResponseDto();
    }
}

