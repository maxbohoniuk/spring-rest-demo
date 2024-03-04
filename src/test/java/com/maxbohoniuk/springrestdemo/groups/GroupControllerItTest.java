package com.maxbohoniuk.springrestdemo.groups;

import com.maxbohoniuk.springrestdemo.BaseItTest;
import com.maxbohoniuk.springrestdemo.TestUtil;
import com.maxbohoniuk.springrestdemo.groups.model.Group;
import com.maxbohoniuk.springrestdemo.groups.model.GroupRequestDto;
import com.maxbohoniuk.springrestdemo.groups.model.GroupResponseDto;
import com.maxbohoniuk.springrestdemo.groups.repo.GroupRepository;
import com.maxbohoniuk.springrestdemo.users.model.User;
import com.maxbohoniuk.springrestdemo.users.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.Objects;
import java.util.UUID;

class GroupControllerItTest extends BaseItTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_get_all_groups() {
        //given
        groupRepository.saveAll(TestUtil.buildObjectsList(5, () -> TestUtil.buildGroup(authenticationUser)));

        //when
        ResponseEntity<GroupResponseDto[]> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .getForEntity("/groups", GroupResponseDto[].class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).length).isEqualTo(5);

        //cleanup
        groupRepository.deleteAll();
    }

    @Test
    void should_get_group_by_id() {
        //given
        Group group = groupRepository.save(TestUtil.buildGroup(authenticationUser));

        //when
        ResponseEntity<GroupResponseDto> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .getForEntity("/groups/" + group.getId(), GroupResponseDto.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody())).isEqualTo(group.toResponseDto());

        //cleanup
        groupRepository.deleteAll();
    }

    @Test
    void should_get_group_by_id_but_group_not_exist() {
        //given

        //when
        ResponseEntity<GroupResponseDto> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .getForEntity("/groups/" + UUID.randomUUID(), GroupResponseDto.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Transactional
    void should_create_group() {
        //given
        GroupRequestDto groupRequestDto = new GroupRequestDto("group");

        //when
        ResponseEntity<GroupResponseDto> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .postForEntity("/groups", groupRequestDto, GroupResponseDto.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(groupRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void should_create_group_but_name_is_blank() {
        //given
        GroupRequestDto groupRequestDto = new GroupRequestDto("");

        //when
        ResponseEntity<GroupResponseDto> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .postForEntity("/groups", groupRequestDto, GroupResponseDto.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(groupRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    void should_join_group() {
        //given
        User user = userRepository.save(TestUtil.buildUser("user2@x.com"));
        Group group = groupRepository.save(TestUtil.buildGroup(authenticationUser));
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        //when
        ResponseEntity<Void> responseEntity = testRestTemplate.withBasicAuth(user.getUsername(), "pass")
                .exchange("/groups/" + group.getId(), HttpMethod.PATCH, null, Void.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(groupRepository.findByIdAndMembersContaining(group.getId(), user)).isNotEmpty();

        //cleanup
        groupRepository.delete(group);
        userRepository.delete(user);
    }

}