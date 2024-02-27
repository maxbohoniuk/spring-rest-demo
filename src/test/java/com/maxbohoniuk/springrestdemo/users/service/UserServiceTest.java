package com.maxbohoniuk.springrestdemo.users.service;

import com.maxbohoniuk.springrestdemo.TestUtil;
import com.maxbohoniuk.springrestdemo.users.model.User;
import com.maxbohoniuk.springrestdemo.users.repo.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserService userService;

    @Test
    void should_get_user_by_UUID() {
        //given
        User user = TestUtil.buildUser("x@x.com");
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        //when
        User res = userService.getUserByUUID(UUID.randomUUID());
        
        //then
        Assertions.assertThat(res).isEqualTo(user);
    }

    @Test
    void should_get_user_by_UUID_but_user_not_exist() {
        //given
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        //when then
        assertThrows(ResponseStatusException.class, () -> userService.getUserByUUID(UUID.randomUUID()));
    }

    @Test
    void should_create_user() {
        //given
        User user = TestUtil.buildUser("x@x.com");
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("encoded");
        Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

        //when
        userService.createUser(user);

        //then
        Mockito.verify(userRepository, Mockito.times(1)).save(argumentCaptor.capture());
        Assertions.assertThat(argumentCaptor.getValue().getPassword()).isEqualTo("encoded");
    }

    @Test
    void should_create_user_but_user_already_exist() {
        //given
        User user = TestUtil.buildUser("x@x.com");
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("encoded");
        Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(user));

        //when
        User res = userService.createUser(user);

        //then
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
        Assertions.assertThat(res).isEqualTo(user);
    }

    @Test
    void should_get_users() {
        //given
        List<User> users = TestUtil.buildObjectsList(10, () -> TestUtil.buildUser("x@x.com"));
        Mockito.when(userRepository.findAll()).thenReturn(users);

        //when
        List<User> res = userService.getUsers();

        //then
        Assertions.assertThat(res.size()).isEqualTo(users.size());
        Assertions.assertThat(res).containsAll(users);
    }

    @Test
    void should_load_user_by_username() {
        //given
        User user = TestUtil.buildUser("x@x.com");
        Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString()))
                .thenReturn(Optional.of(user));

        //when
        UserDetails res = userService.loadUserByUsername(Mockito.anyString());

        //then
        Assertions.assertThat(res).isEqualTo(user);
    }

    @Test
    void should_load_user_by_username_but_user_not_exist() {
        //given
        Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString()))
                .thenReturn(Optional.empty());

        //when then
        assertThrows(ResponseStatusException.class, () -> userService.loadUserByUsername(Mockito.anyString()));
    }
}