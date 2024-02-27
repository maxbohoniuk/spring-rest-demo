package com.maxbohoniuk.springrestdemo.groups.service;

import com.maxbohoniuk.springrestdemo.TestUtil;
import com.maxbohoniuk.springrestdemo.groups.model.Group;
import com.maxbohoniuk.springrestdemo.groups.model.GroupRequestDto;
import com.maxbohoniuk.springrestdemo.groups.repo.GroupRepository;
import com.maxbohoniuk.springrestdemo.users.model.User;
import com.maxbohoniuk.springrestdemo.users.service.AuthenticatedUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks
    private GroupService groupService;

    @Test
    void should_create_group() {
        //given
        User user = TestUtil.buildUser("x@x.com");
        Mockito.when(authenticatedUserService.getLoggedInUser())
                .thenReturn(user);
        ArgumentCaptor<Group> argumentCaptor = ArgumentCaptor.forClass(Group.class);
        GroupRequestDto groupRequestDto = TestUtil.buildGroupRequestDto();

        //when
        groupService.createGroup(groupRequestDto);

        //then
        Mockito.verify(groupRepository).save(argumentCaptor.capture());
        Assertions.assertThat(argumentCaptor.getValue().getMembers()).contains(user);
        Assertions.assertThat(argumentCaptor.getValue().getCreator()).isEqualTo(user);
        Assertions.assertThat(argumentCaptor.getValue().getName()).isEqualTo(groupRequestDto.getName());
    }

    @Test
    void should_join_group() {
        //given
        User user = TestUtil.buildUser("x@x.com");
        Group group = TestUtil.buildGroup(user);
        Mockito.when(authenticatedUserService.getLoggedInUser())
                .thenReturn(user);
        Mockito.when(groupRepository.findByIdAndMembersContaining(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(groupRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(group));

        //when
        groupService.joinGroup(UUID.randomUUID());

        //then
        Assertions.assertThat(group.getMembers()).contains(user);
        Mockito.verify(groupRepository, Mockito.times(1)).incrementMembersCount(Mockito.any());
    }

    @Test
    void should_join_group_but_user_is_already_a_member() {
        //given
        User user = TestUtil.buildUser("x@x.com");
        Group group = TestUtil.buildGroup(user);
        Mockito.when(authenticatedUserService.getLoggedInUser())
                .thenReturn(user);
        Mockito.when(groupRepository.findByIdAndMembersContaining(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(group));

        //when
        groupService.joinGroup(UUID.randomUUID());

        //then
        Mockito.verify(groupRepository, Mockito.never()).incrementMembersCount(Mockito.any());
    }

    @Test
    void should_get_all_groups() {
        //given
        List<Group> groups = TestUtil.buildObjectsList(5, () -> TestUtil.buildGroup(TestUtil.buildUser("")));
        Mockito.when(groupRepository.findAll()).thenReturn(groups);

        //when
        List<Group> res = groupService.getAllGroups();

        //then
        Assertions.assertThat(res.size()).isEqualTo(groups.size());
        Assertions.assertThat(res).containsAll(groups);
    }

    @Test
    void should_get_group_by_id() {
        //given
        Group group = TestUtil.buildGroup(TestUtil.buildUser(""));
        Mockito.when(groupRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(group));

        //when
        Group res = groupService.getGroupById(UUID.randomUUID());

        //then
        Assertions.assertThat(res).isEqualTo(group);
    }

    @Test
    void should_get_group_by_id_but_group_not_exist() {
        //given
        Mockito.when(groupRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        //when then
        assertThrows(ResponseStatusException.class, () -> groupService.getGroupById(UUID.randomUUID()));
    }
}