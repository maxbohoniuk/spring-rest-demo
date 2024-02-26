package com.maxbohoniuk.springrestdemo;

import com.maxbohoniuk.springrestdemo.groups.model.Group;
import com.maxbohoniuk.springrestdemo.groups.repo.GroupRepository;
import com.maxbohoniuk.springrestdemo.users.model.User;
import com.maxbohoniuk.springrestdemo.users.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConditionalOnProperty(value = "spring.jpa.hibernate.ddl-auto", havingValue = "create")
@RequiredArgsConstructor
public class InitialDataLoader {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @EventListener
    @Transactional
    public void loadData(ContextRefreshedEvent event) {
        User userA = userRepository.save(User.builder()
                .email("a@gmail.com")
                .name("User A")
                .password("$2a$12$smqNzEbf4nekvBNDIXJ5T.F3Uh0LNRwAIC1YgVmwpisePR2SacEEa")
                .build());

        User userB = userRepository.save(User.builder()
                .email("b@gmail.com")
                .name("User B")
                .password("$2a$12$smqNzEbf4nekvBNDIXJ5T.F3Uh0LNRwAIC1YgVmwpisePR2SacEEa")
                .build());

        User userC = userRepository.save(User.builder()
                .email("c@gmail.com")
                .name("User C")
                .password("$2a$12$smqNzEbf4nekvBNDIXJ5T.F3Uh0LNRwAIC1YgVmwpisePR2SacEEa")
                .build());

        groupRepository.save(Group.builder()
                .name("Group A")
                .creator(userA)
                .members(Set.of(userA))
                .build());

        groupRepository.save(Group.builder()
                .name("Group B")
                .creator(userB)
                .members(Set.of(userB))
                .build());

        groupRepository.save(Group.builder()
                .name("Group C")
                .creator(userC)
                .members(Set.of(userC))
                .build());
    }
}
