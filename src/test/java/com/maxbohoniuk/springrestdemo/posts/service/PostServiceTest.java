package com.maxbohoniuk.springrestdemo.posts.service;

import com.maxbohoniuk.springrestdemo.TestUtil;
import com.maxbohoniuk.springrestdemo.groups.model.Group;
import com.maxbohoniuk.springrestdemo.groups.service.GroupService;
import com.maxbohoniuk.springrestdemo.posts.model.Post;
import com.maxbohoniuk.springrestdemo.posts.repo.PostRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private PostService postService;

    @Test
    void should_get_all_posts() {
        //given
        List<Post> posts = TestUtil.buildObjectsList(5, TestUtil::buildPost);
        Mockito.when(postRepository.getPostByAuthorOrGroup(Mockito.any(), Mockito.any()))
                .thenReturn(posts);

        //when
        List<Post> res = postService.getAllPosts(Optional.of(UUID.randomUUID()), Optional.of(UUID.randomUUID()));

        //then
        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.size()).isEqualTo(posts.size());
    }

    @Test
    void should_add_post() {
        //given
        ArgumentCaptor<Post> argumentCaptor = ArgumentCaptor.forClass(Post.class);
        User user = TestUtil.buildUser("x@x.com");
        Mockito.when(authenticatedUserService.getLoggedInUser()).thenReturn(user);
        Post post = TestUtil.buildPost();

        //when
        postService.addPost(post, Optional.empty());

        //then
        Mockito.verify(postRepository, Mockito.times(1)).save(argumentCaptor.capture());
        Assertions.assertThat(argumentCaptor.getValue().getAuthor()).isEqualTo(user);
        Mockito.verify(groupService, Mockito.never()).getGroupById(Mockito.any());
    }

    @Test
    void should_add_post_for_group() {
        //given
        ArgumentCaptor<Post> argumentCaptor = ArgumentCaptor.forClass(Post.class);
        User user = TestUtil.buildUser("x@x.com");
        Group group = TestUtil.buildGroup(user);
        Mockito.when(authenticatedUserService.getLoggedInUser()).thenReturn(user);
        Mockito.when(groupService.getGroupById(Mockito.any()))
                .thenReturn(group);
        Post post = TestUtil.buildPost();

        //when
        postService.addPost(post, Optional.of(UUID.randomUUID()));

        //then
        Mockito.verify(postRepository, Mockito.times(1)).save(argumentCaptor.capture());
        Assertions.assertThat(argumentCaptor.getValue().getAuthor()).isEqualTo(user);
        Assertions.assertThat(argumentCaptor.getValue().getGroup()).isEqualTo(group);
    }
}