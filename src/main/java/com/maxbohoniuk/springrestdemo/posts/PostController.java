package com.maxbohoniuk.springkotlindemo.posts

import com.maxbohoniuk.springkotlindemo.posts.model.PostDto
import com.maxbohoniuk.springkotlindemo.posts.service.PostService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(private val postService: PostService) {

    @GetMapping
    fun getAllPosts(): List<PostDto> = postService.getAllPosts().map { PostDto.fromEntity(it) }

    @PostMapping
    fun addPost(@RequestBody postDto: PostDto): PostDto = PostDto.fromEntity(postService.addPost(postDto.toEntity()))
}