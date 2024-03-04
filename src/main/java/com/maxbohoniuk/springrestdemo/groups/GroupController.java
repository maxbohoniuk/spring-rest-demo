package com.maxbohoniuk.springrestdemo.groups;

import com.maxbohoniuk.springrestdemo.groups.model.Group;
import com.maxbohoniuk.springrestdemo.groups.model.GroupRequestDto;
import com.maxbohoniuk.springrestdemo.groups.model.GroupResponseDto;
import com.maxbohoniuk.springrestdemo.groups.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public List<GroupResponseDto> getAllGroups() {
        return groupService.getAllGroups().stream().map(Group::toResponseDto).toList();
    }

    @GetMapping("/{id}")
    public GroupResponseDto getGroup(@PathVariable("id") UUID id) {
        return groupService.getGroupById(id).toResponseDto();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupResponseDto createGroup(@RequestBody @Valid GroupRequestDto groupRequestDto) {
        return groupService.createGroup(groupRequestDto).toResponseDto();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> joinGroup(@PathVariable("id") UUID id) {
        groupService.joinGroup(id);
        return ResponseEntity.ok().build();
    }
}
