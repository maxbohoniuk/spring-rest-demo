package com.maxbohoniuk.springkotlindemo.posts.model

import com.maxbohoniuk.springkotlindemo.users.model.User
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID
@Entity
@Table(name = "posts")
data class Post(
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        val id: UUID?,
        val title: String,
        val content: String?
) {

        @JoinColumn(name = "author_id")
        @ManyToOne
        lateinit var author: User
}
