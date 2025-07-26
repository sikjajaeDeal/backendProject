package likelion.beanBa.backendProject.log.repository;

import likelion.beanBa.backendProject.log.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog,Long> {

}
