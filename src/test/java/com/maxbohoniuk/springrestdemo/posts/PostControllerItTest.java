package com.maxbohoniuk.springrestdemo.posts;

import com.maxbohoniuk.springrestdemo.BaseItTest;
import com.maxbohoniuk.springrestdemo.TestUtil;
import com.maxbohoniuk.springrestdemo.groups.model.Group;
import com.maxbohoniuk.springrestdemo.groups.repo.GroupRepository;
import com.maxbohoniuk.springrestdemo.posts.model.Post;
import com.maxbohoniuk.springrestdemo.posts.model.PostDto;
import com.maxbohoniuk.springrestdemo.posts.repo.PostRepository;
import com.maxbohoniuk.springrestdemo.users.model.User;
import com.maxbohoniuk.springrestdemo.users.repo.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;

class PostControllerItTest extends BaseItTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void should_get_all_posts() {
        //given
        List<Post> savedPosts = postRepository.saveAll(List.of(
                TestUtil.buildPostWithAuthor(authenticationUser),
                TestUtil.buildPostWithAuthor(authenticationUser)
        ));

        //when
        ResponseEntity<PostDto[]> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .getForEntity("/posts", PostDto[].class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).length).isEqualTo(savedPosts.size());

        //cleanup
        postRepository.deleteAll();
    }

    @Test
    void should_get_all_posts_by_author() {
        //given
        User author = userRepository.save(TestUtil.buildUser("author2@x.com"));
        postRepository.saveAll(List.of(
           TestUtil.buildPostWithAuthor(author),
           TestUtil.buildPostWithAuthor(author),
           TestUtil.buildPostWithAuthor(authenticationUser),
           TestUtil.buildPostWithAuthor(authenticationUser)
        ));

        //when
        ResponseEntity<PostDto[]> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .getForEntity("/posts?user={user}", PostDto[].class, Map.of("user", author.getId().toString()));
        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).length).isEqualTo(2);
        Assertions.assertThat(postRepository.findAll().size()).isGreaterThan(2);

        //cleanup
        postRepository.deleteAll();
        userRepository.delete(author);
    }

    @Test
    void should_get_all_posts_by_groups() {
        //given
        Group group = groupRepository.save(TestUtil.buildGroup(authenticationUser));
        postRepository.saveAll(List.of(
                TestUtil.buildPostWithAuthor(authenticationUser),
                TestUtil.buildPostWithAuthor(authenticationUser),
                TestUtil.buildPostWithAuthorAndGroup(authenticationUser, group),
                TestUtil.buildPostWithAuthorAndGroup(authenticationUser, group)
        ));

        //when
        ResponseEntity<PostDto[]> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .getForEntity("/posts?group={group}", PostDto[].class, Map.of("group", group.getId().toString()));
        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).length).isEqualTo(2);
        Assertions.assertThat(postRepository.findAll().size()).isGreaterThan(2);

        //cleanup
        postRepository.deleteAll();
        groupRepository.delete(group);
    }

    @Test
    void should_add_post() {
        //given
        PostDto postDto = PostDto.builder()
                .title("post")
                .content("content")
                .build();

        //when
        ResponseEntity<PostDto> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .postForEntity("/posts", postDto, PostDto.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).getTitle()).isEqualTo(postDto.getTitle());
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).getContent()).isEqualTo(postDto.getContent());
        Assertions.assertThat(postRepository.findAll().size()).isEqualTo(1);

        //cleanup
        postRepository.deleteAll();
    }

    @Test
    void should_add_post_for_group() {
        //given
        Group group = groupRepository.save(TestUtil.buildGroup(authenticationUser));
        PostDto postDto = PostDto.builder()
                .title("post")
                .content("content")
                .build();

        //when
        ResponseEntity<PostDto> responseEntity = testRestTemplate.withBasicAuth(authenticationUser.getUsername(), "pass")
                .postForEntity("/posts?group={group}", postDto, PostDto.class, Map.of("group", group.getId().toString()));

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).getTitle()).isEqualTo(postDto.getTitle());
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).getContent()).isEqualTo(postDto.getContent());
        Assertions.assertThat(postRepository.findAll().size()).isEqualTo(1);
        Assertions.assertThat(postRepository.findById(responseEntity.getBody().getId()).get().getGroup().getId()).isEqualTo(group.getId());
    }

}