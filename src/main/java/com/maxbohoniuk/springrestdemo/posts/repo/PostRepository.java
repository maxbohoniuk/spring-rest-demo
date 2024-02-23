package com.maxbohoniuk.springrestdemo.posts.repo;


import com.maxbohoniuk.springrestdemo.posts.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

}