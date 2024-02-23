package com.maxbohoniuk.springrestdemo.users.repo;

import com.maxbohoniuk.springkotlindemo.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface UserReposiroty extends JpaRepository<User, UUID> {

    Optional<User> findByEmailIgnoreCase(String email);
}