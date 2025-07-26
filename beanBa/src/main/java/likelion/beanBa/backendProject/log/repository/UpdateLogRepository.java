package likelion.beanBa.backendProject.log.repository;

import likelion.beanBa.backendProject.log.entity.UpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateLogRepository extends JpaRepository<UpdateLog,Long> {


}
