package com.maxbohoniuk.springrestdemo.groups.service;

import com.maxbohoniuk.springrestdemo.groups.model.Group;
import com.maxbohoniuk.springrestdemo.groups.model.GroupRequestDto;
import com.maxbohoniuk.springrestdemo.groups.repo.GroupRepository;
import com.maxbohoniuk.springrestdemo.users.model.User;
import com.maxbohoniuk.springrestdemo.users.service.AuthenticatedUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final AuthenticatedUserService authenticatedUserService;

    @Transactional
    public Group createGroup(GroupRequestDto groupRequestDto) {
        User loggedIn = authenticatedUserService.getLoggedInUser();
        Group group = groupRequestDto.toEntity(loggedIn);
        group.addMember(loggedIn);
        return groupRepository.save(group);
    }

    @Transactional
    public void joinGroup(UUID id) {
        User loggedIn = authenticatedUserService.getLoggedInUser();
        if (groupRepository.findByIdAndMembersContaining(id, loggedIn).isEmpty()) {
            Group group = getGroupById(id);
            group.addMember(loggedIn);
            groupRepository.incrementMembersCount(id);
        }
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupById(UUID id) {
        return groupRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
