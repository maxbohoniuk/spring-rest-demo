package com.maxbohoniuk.springrestdemo.groups.model;

import com.maxbohoniuk.springrestdemo.users.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GroupRequestDto {

    @NotBlank
    private String name;

    public Group toEntity(User creator) {
        return Group.builder()
                .name(name)
                .creator(creator)
                .build();
    }

}
