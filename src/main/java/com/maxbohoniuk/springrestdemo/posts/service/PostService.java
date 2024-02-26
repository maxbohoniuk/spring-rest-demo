package com.maxbohoniuk.springrestdemo.posts.service;

import com.maxbohoniuk.springrestdemo.groups.service.GroupService;
import com.maxbohoniuk.springrestdemo.posts.model.Post;
import com.maxbohoniuk.springrestdemo.posts.repo.PostRepository;
import com.maxbohoniuk.springrestdemo.users.service.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final GroupService groupService;

    public List<Post> getAllPosts(Optional<UUID> groupId, Optional<UUID> userId) {
        return postRepository.getPostByAuthorOrGroup(groupId.orElse(null), userId.orElse(null));
    }

    public Post addPost(Post post, Optional<UUID> groupId) {
        post.setAuthor(authenticatedUserService.getLoggedInUser());
        groupId.ifPresent(uuid -> post.setGroup(groupService.getGroupById(uuid)));
        return postRepository.save(post);
    }
}

