package me.youzheng.core.domain.sessionlog.repository;

import me.youzheng.core.domain.sessionlog.entity.SessionLog;
import me.youzheng.core.domain.sessionlog.repository.support.SessionLogRepositorySupport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionLogRepository extends CrudRepository<SessionLog, Long>, SessionLogRepositorySupport {

}
