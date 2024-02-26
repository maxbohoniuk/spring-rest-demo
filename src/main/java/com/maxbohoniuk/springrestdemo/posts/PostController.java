package com.maxbohoniuk.springrestdemo.posts;


import com.maxbohoniuk.springrestdemo.posts.model.Post;
import com.maxbohoniuk.springrestdemo.posts.model.PostDto;
import com.maxbohoniuk.springrestdemo.posts.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostDto> getAllPosts(@RequestParam(name = "group", required = false) Optional<UUID> groupId,
                                     @RequestParam(name = "user", required = false) Optional<UUID> userId) {
        return postService.getAllPosts(groupId, userId).stream()
                .map(Post::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public PostDto addPost(@RequestBody PostDto postDto,
                           @RequestParam(name = "group", required = false) Optional<UUID> groupId) {
        return postService.addPost(postDto.toEntity(), groupId).toDto();
    }
}

