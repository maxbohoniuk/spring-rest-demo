package com.maxbohoniuk.springkotlindemo.posts.repo

import com.maxbohoniuk.springkotlindemo.posts.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostRepository : JpaRepository<Post, UUID> {

    override fun findById(id: UUID): Optional<Post>
}