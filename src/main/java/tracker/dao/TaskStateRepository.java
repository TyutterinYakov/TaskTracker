package tracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tracker.entity.TaskStateEntity;

@Repository
public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long>{

}
