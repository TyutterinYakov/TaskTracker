package tracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tracker.entity.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>{

}
