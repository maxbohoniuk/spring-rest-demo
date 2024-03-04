package com.maxbohoniuk.springrestdemo.groups.model;

import com.maxbohoniuk.springrestdemo.users.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
