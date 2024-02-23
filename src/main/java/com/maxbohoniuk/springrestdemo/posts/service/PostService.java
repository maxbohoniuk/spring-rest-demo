package com.maxbohoniuk.springrestdemo.posts.service;

import com.maxbohoniuk.springrestdemo.posts.model.Post;
import com.maxbohoniuk.springrestdemo.posts.repo.PostRepository;
import com.maxbohoniuk.springrestdemo.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post addPost(Post post) {
        post.setAuthor(userService.getLoggedInUser());
        return postRepository.save(post);
    }
}

