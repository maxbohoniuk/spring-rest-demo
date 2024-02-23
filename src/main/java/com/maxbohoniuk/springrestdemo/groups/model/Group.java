package com.maxbohoniuk.springrestdemo.groups.model;

import com.maxbohoniuk.springrestdemo.users.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "groups")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @JoinColumn(name = "creator_id", updatable = false, nullable = false)
    @ManyToOne
    private User creator;

    @JoinTable(name = "group_members", joinColumns = @JoinColumn(name = "group_id", table = "groups") ,
            inverseJoinColumns = @JoinColumn(name = "user_id", table = "users"))
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> members;

    @Column(nullable = false)
    @Builder.Default
    private Integer membersCount = 1;

    @CreatedDate
    private LocalDateTime createdAt;

    public void addMember(User member) {
        if (members == null) {
            members = Set.of(member);
        }
        else {
            members.add(member);
        }
    }

    public GroupResponseDto toResponseDto() {
        return GroupResponseDto.builder()
                .id(id)
                .name(name)
                .creator(creator.getEmail())
                .members(Optional.ofNullable(members).orElse(Set.of()).stream().map(User::getEmail).collect(Collectors.toSet()))
                .membersCount(membersCount)
                .createdAt(createdAt)
                .build();
    }
}
