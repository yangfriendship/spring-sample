package me.youzheng.springsecurity.user.repository;

import java.util.Optional;
import me.youzheng.springsecurity.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserId(String userId);

    Optional<User> findByUserId(String userId);
}
