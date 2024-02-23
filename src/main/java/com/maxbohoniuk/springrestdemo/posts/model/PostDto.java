package com.maxbohoniuk.springrestdemo.posts.model;

import com.maxbohoniuk.springrestdemo.users.model.UserResponseDto;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostDto {
    private UUID id;
    private String title;
    private String content;
    private UserResponseDto author;

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}

