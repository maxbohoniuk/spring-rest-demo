package com.maxbohoniuk.springrestdemo.posts.repo;


import com.maxbohoniuk.springrestdemo.posts.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query("SELECT p FROM Post p WHERE (:groupId IS NULL OR p.group.id = :groupId) " +
            "AND (:authorId IS NULL OR p.author.id = :authorId)")
    List<Post> getPostByAuthorOrGroup(UUID groupId, UUID authorId);

}