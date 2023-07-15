package me.youzheng.core.domain.user.repository;

import me.youzheng.core.domain.user.entity.User;
import me.youzheng.core.domain.user.repository.support.UserRepositorySupport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, Long>, UserRepositorySupport {

}
