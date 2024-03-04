package com.maxbohoniuk.springrestdemo.users;

import com.maxbohoniuk.springrestdemo.BaseItTest;
import com.maxbohoniuk.springrestdemo.TestUtil;
import com.maxbohoniuk.springrestdemo.users.model.User;
import com.maxbohoniuk.springrestdemo.users.model.UserRequestDto;
import com.maxbohoniuk.springrestdemo.users.model.UserResponseDto;
import com.maxbohoniuk.springrestdemo.users.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.UUID;

class UserControllerItTest extends BaseItTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_get_all_users() {
        //given
        User user = userRepository.save(TestUtil.buildUser("x@x.com"));

        //when
        ResponseEntity<UserResponseDto[]> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .exchange("/users", HttpMethod.GET, HttpEntity.EMPTY, UserResponseDto[].class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).length)
                .isEqualTo(userRepository.findAll().size());

        //cleanup
        userRepository.delete(user);
    }

    @Test
    void should_get_user_by_id() {
        //given
        User savedUser = userRepository.save(TestUtil.buildUser("x@x.com"));

        //when
        ResponseEntity<UserResponseDto> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .getForEntity("/users/" + savedUser.getId().toString(), UserResponseDto.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()))
                .usingRecursiveAssertion()
                .isEqualTo(savedUser.toResponseDto());

        //cleanup
        userRepository.delete(savedUser);
    }

    @Test
    void should_get_user_by_id_but_user_not_exist() {
        //given

        //when
        ResponseEntity<String> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .getForEntity("/users/" + UUID.randomUUID(), String.class);

        //then
        System.out.println(responseEntity);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Transactional
    void should_create_user() {
        //given
        UserRequestDto userResponseDto = UserRequestDto.builder()
                .email("qwerty@x.com")
                .name("Qwerty user")
                .password("pass")
                .build();

        //when
        ResponseEntity<UserResponseDto> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .postForEntity("/users", userResponseDto, UserResponseDto.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(userRepository.findByEmailIgnoreCase(userResponseDto.getEmail())).isNotEmpty();
    }

    @Test
    @Transactional
    void should_create_user_but_email_format_is_not_valid() {
        //given
        UserRequestDto userResponseDto = UserRequestDto.builder()
                .email("qwerty")
                .name("Qwerty user")
                .password("pass")
                .build();

        //when
        ResponseEntity<UserResponseDto> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .postForEntity("/users", userResponseDto, UserResponseDto.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}