package com.maxbohoniuk.springrestdemo.users.service;

import com.maxbohoniuk.springrestdemo.TestUtil;
import com.maxbohoniuk.springrestdemo.users.model.User;
import com.maxbohoniuk.springrestdemo.users.repo.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class AuthenticatedUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthenticatedUserService authenticatedUserService;

    @Test
    void should_get_logged_in_user() {
        //given
        String userName = "x@x.com";
        SecurityContext mockedSecurityContext = Mockito.mock(SecurityContext.class);
        Authentication mockedAuthentication = Mockito.mock(Authentication.class);
        Mockito.when(mockedAuthentication.getName()).thenReturn(userName);
        Mockito.when(mockedSecurityContext.getAuthentication())
                .thenReturn(mockedAuthentication);
        SecurityContextHolder.setContext(mockedSecurityContext);

        User user = TestUtil.buildUser(userName);
        Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(user));

        //when
        User res = authenticatedUserService.getLoggedInUser();

        //then
        Mockito.verify(mockedAuthentication, Mockito.times(1)).getName();
        Assertions.assertThat(res).isEqualTo(user);
    }

    @Test
    void should_get_logged_in_user_but_user_is_not_in_db() {
        //given
        Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());

        //when
        User res = authenticatedUserService.getLoggedInUser();

        //then
        Assertions.assertThat(res).isNull();
    }
}