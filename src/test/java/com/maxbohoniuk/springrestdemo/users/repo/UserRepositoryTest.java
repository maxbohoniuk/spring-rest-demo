package com.maxbohoniuk.springrestdemo.users.repo;

import com.maxbohoniuk.springrestdemo.TestUtil;
import com.maxbohoniuk.springrestdemo.users.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_find_by_email_ignore_case() {
        //given
        userRepository.save(TestUtil.buildUser("qwerty@x.com"));

        //when
        Optional<User> res = userRepository.findByEmailIgnoreCase("QWerTY@X.CoM");
        Optional<User> res2 = userRepository.findByEmailIgnoreCase("q@x.com");

        //then
        Assertions.assertThat(res).isNotEmpty();
        Assertions.assertThat(res2).isEmpty();
    }
}