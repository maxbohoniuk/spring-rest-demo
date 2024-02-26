package com.maxbohoniuk.springrestdemo.groups.repo;

import com.maxbohoniuk.springrestdemo.TestUtil;
import com.maxbohoniuk.springrestdemo.groups.model.Group;
import com.maxbohoniuk.springrestdemo.users.model.User;
import com.maxbohoniuk.springrestdemo.users.repo.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldFindByIdAndMembersContaining() {
        //given
        User member = userRepository.save(TestUtil.buildUser("x@x.com"));
        Group savedGroup = groupRepository.save(
                Group.builder()
                        .name("Group")
                        .members(Set.of(member))
                        .creator(member)
                        .membersCount(1)
                        .build()
        );

        //when
        Optional<Group> res = groupRepository.findByIdAndMembersContaining(savedGroup.getId(), member);

        //then
        Assertions.assertThat(res).isNotEmpty();
    }

    @Test
    void shouldNotFindByIdAndMembersContaining() {
        //given
        User member = userRepository.save(TestUtil.buildUser("x@x.com"));
        Group savedGroup = groupRepository.save(
                Group.builder()
                        .name("Group")
                        .creator(member)
                        .membersCount(0)
                        .build()
        );

        //when
        Optional<Group> res = groupRepository.findByIdAndMembersContaining(savedGroup.getId(), member);

        //then
        Assertions.assertThat(res).isEmpty();
    }

    @Test
    void shouldIncrementMembersCount() {
        //given
        User member = userRepository.save(TestUtil.buildUser("x@x.com"));
        Group savedGroup = groupRepository.save(
                Group.builder()
                        .name("Group")
                        .creator(member)
                        .membersCount(0)
                        .build()
        );

        //when
        groupRepository.incrementMembersCount(savedGroup.getId());
        //clear the jpa cache
        entityManager.clear();

        //then
        Group refreshedGroup = groupRepository.findById(savedGroup.getId()).orElseThrow();
        Assertions.assertThat(refreshedGroup.getMembersCount())
                .isEqualTo(1);
    }
}