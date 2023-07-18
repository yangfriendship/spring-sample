package me.youzheng.core.domain.userrole.repository;

import me.youzheng.core.domain.userrole.entity.UserRole;
import me.youzheng.core.domain.userrole.entity.UserRolePk;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRole, UserRolePk> {

}
