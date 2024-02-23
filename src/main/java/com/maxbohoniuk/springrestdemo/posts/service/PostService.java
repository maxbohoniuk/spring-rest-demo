package com.maxbohoniuk.springkotlindemo.posts.service

import com.maxbohoniuk.springkotlindemo.posts.model.Post
import com.maxbohoniuk.springkotlindemo.posts.repo.PostRepository
import com.maxbohoniuk.springkotlindemo.users.service.UserService
import org.springframework.stereotype.Service

@Service
class PostService(private val postRepository: PostRepository, private val userService: UserService) {

    fun getAllPosts(): List<Post> = postRepository.findAll()

    fun addPost(post: Post): Post {
        post.author = userService.getLoggedInUser()
        return postRepository.save(post)
    }

}