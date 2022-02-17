package tracker.store.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tracker.store.entities.ProjectEntity;
import tracker.store.entities.TaskStateEntity;

@Repository
public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long>{


	Optional<TaskStateEntity> findByProjectAndNameIsIgnoreCase(ProjectEntity project, String taskStateName);
	
	Optional<TaskStateEntity> findByRigthTaskStateAndProject(TaskStateEntity entity, ProjectEntity project);

	Optional<TaskStateEntity> findByLeftTaskState(TaskStateEntity taskLeft);

	Optional<TaskStateEntity> findByTaskStateIdAndProject(Long leftTaskStateId, ProjectEntity project);



}
