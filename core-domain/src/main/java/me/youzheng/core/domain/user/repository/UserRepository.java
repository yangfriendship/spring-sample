package me.youzheng.core.domain.user.repository;

import me.youzheng.core.domain.user.User;
import me.youzheng.core.domain.user.repository.support.UserRepositorySupporter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, UserRepositorySupporter {

}
