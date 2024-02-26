package com.maxbohoniuk.springrestdemo.posts.repo;

import com.maxbohoniuk.springrestdemo.TestUtil;
import com.maxbohoniuk.springrestdemo.groups.model.Group;
import com.maxbohoniuk.springrestdemo.groups.repo.GroupRepository;
import com.maxbohoniuk.springrestdemo.posts.model.Post;
import com.maxbohoniuk.springrestdemo.users.model.User;
import com.maxbohoniuk.springrestdemo.users.repo.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;


    @Test
    void shouldGetPostByAuthorOrGroup() {
        //given
        User user = userRepository.save(TestUtil.buildUser("a@x.com"));
        Group group = groupRepository.save(TestUtil.buildGroup(user));
        List<Post> postsToSave = List.of(
                TestUtil.buildPost(), TestUtil.buildPost(),
                TestUtil.buildPostWithAuthor(user), TestUtil.buildPostWithAuthor(user),
                TestUtil.buildPostWithGroup(group), TestUtil.buildPostWithGroup(group),
                TestUtil.buildPostWithAuthorAndGroup(user, group),
                TestUtil.buildPostWithAuthorAndGroup(user, group)
        );
        postRepository.saveAll(postsToSave);

        //when
        List<Post> allPosts = postRepository.getPostByAuthorOrGroup(null, null);
        List<Post> filteredByAuthor = postRepository.getPostByAuthorOrGroup(null, user.getId());
        List<Post> filteredByGroup = postRepository.getPostByAuthorOrGroup(group.getId(), null);
        List<Post> filteredByAuthorAndGroup = postRepository.getPostByAuthorOrGroup(group.getId(), user.getId());

        //then
        Assertions.assertThat(allPosts.size()).isEqualTo(postsToSave.size());
        Assertions.assertThat(filteredByAuthor.size()).isEqualTo(4);
        Assertions.assertThat(filteredByGroup.size()).isEqualTo(4);
        Assertions.assertThat(filteredByAuthorAndGroup.size()).isEqualTo(2);
    }
}