package com.maxbohoniuk.springrestdemo.posts.model;

import com.maxbohoniuk.springrestdemo.groups.model.Group;
import com.maxbohoniuk.springrestdemo.users.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "posts")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String title;
    private String content;

    @JoinColumn(name = "author_id")
    @ManyToOne
    private User author;

    @JoinColumn(name = "group_id")
    @ManyToOne
    private Group group;

    @CreatedDate
    private LocalDateTime createdAt;

    public PostDto toDto() {
        return PostDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author.toResponseDto())
                .build();
    }
}

