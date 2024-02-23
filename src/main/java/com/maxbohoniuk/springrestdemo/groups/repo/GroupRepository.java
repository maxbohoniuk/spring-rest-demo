package com.maxbohoniuk.springrestdemo.groups.repo;

import com.maxbohoniuk.springrestdemo.groups.model.Group;
import com.maxbohoniuk.springrestdemo.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {

    Optional<Group>  findByIdAndMembersContaining(UUID id, User member);

    @Modifying
    @Query("UPDATE Group SET membersCount = membersCount + 1 WHERE id = :groupId")
    void incrementMembersCount(UUID groupId);
}
