package com.github.chaunguyentruongan.warehouse_cdnsg.modules.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findBySchoolID(String schoolID);

    void deleteByEmail(String email);

    Optional<User> findByUsername(String username);
}
