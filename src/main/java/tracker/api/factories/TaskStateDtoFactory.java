package tracker.api.factories;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tracker.api.dto.TaskStateDto;
import tracker.store.entities.TaskStateEntity;

@Component
public class TaskStateDtoFactory {

	private final TaskDtoFactory taskDtoFactory;
	
	@Autowired
	public TaskStateDtoFactory(TaskDtoFactory taskDtoFactory) {
		super();
		this.taskDtoFactory = taskDtoFactory;
	}



	public TaskStateDto makeTaskStateDto(TaskStateEntity entity) {
		return new TaskStateDto(
				entity.getTaskStateId(),
				entity.getName(),
				entity.getLeftTaskState().map(TaskStateEntity::getTaskStateId).orElse(null),
				entity.getRigthTaskState().map(TaskStateEntity::getTaskStateId).orElse(null),
				entity.getCreatedAt(),
				entity
					.getTasks()
						.stream()
						.map(taskDtoFactory::makeTaskDto)
						.collect(Collectors.toList())
				);
	}
}
