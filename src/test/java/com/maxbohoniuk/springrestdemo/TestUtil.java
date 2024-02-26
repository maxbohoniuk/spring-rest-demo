package com.maxbohoniuk.springrestdemo;

import com.maxbohoniuk.springrestdemo.groups.model.Group;
import com.maxbohoniuk.springrestdemo.posts.model.Post;
import com.maxbohoniuk.springrestdemo.users.model.User;

public class TestUtil {

    public static User buildUser(String email) {
        return User.builder()
                .name("Name")
                .email(email)
                .password("$2a$12$smqNzEbf4nekvBNDIXJ5T.F3Uh0LNRwAIC1YgVmwpisePR2SacEEa")
                .build();
    }

    public static Group buildGroup(User creator) {
        return Group.builder()
                .name("Group")
                .creator(creator)
                .build();
    }

    public static Post buildPost() {
        return Post.builder()
                .title("Post title")
                .content("Content")
                .build();
    }

    public static Post buildPostWithAuthor(User author) {
        return Post.builder()
                .title("Post title")
                .content("Content")
                .author(author)
                .build();
    }


    public static Post buildPostWithGroup(Group group) {
        return Post.builder()
                .title("Post title")
                .content("Content")
                .group(group)
                .build();
    }

    public static Post buildPostWithAuthorAndGroup(User author, Group group) {
        return Post.builder()
                .title("Post title")
                .content("Content")
                .group(group)
                .author(author)
                .build();
    }
}
