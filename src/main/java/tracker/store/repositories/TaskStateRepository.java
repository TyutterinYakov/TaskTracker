package tracker.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tracker.store.entities.TaskStateEntity;

@Repository
public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long>{

}
