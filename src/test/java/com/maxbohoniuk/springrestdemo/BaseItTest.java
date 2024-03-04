package com.maxbohoniuk.springrestdemo;

import com.maxbohoniuk.springrestdemo.users.model.User;
import com.maxbohoniuk.springrestdemo.users.repo.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BaseItTest {

    //@Container
    @ServiceConnection
    protected final static PostgreSQLContainer<?> postgreSQLContainer;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    protected static User authenticationUser;

    static {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
        postgreSQLContainer.start();
    }

    @BeforeAll
    static void setup(@Autowired UserRepository userRepository) {
        if(authenticationUser == null) {
            authenticationUser = userRepository.save(
                    TestUtil.buildUser("user@x.com")
            );
        }
    }

    @Test
    void test_db_container() {
        Assertions.assertThat(postgreSQLContainer.isCreated()).isTrue();
        Assertions.assertThat(postgreSQLContainer.isRunning()).isTrue();
    }


}
