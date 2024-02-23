package com.maxbohoniuk.springkotlindemo.posts.model

import com.maxbohoniuk.springkotlindemo.users.model.UserResponseDto
import java.util.UUID

data class PostDto(
    val id: UUID?,
    val title: String,
    val content: String?,
    val author: UserResponseDto?
) {
    fun toEntity() = Post(null, title, content)

    companion object {
        fun fromEntity(postEntity: Post) =
            PostDto(postEntity.id, postEntity.title, postEntity.content, UserResponseDto(postEntity.author))
    }

}
