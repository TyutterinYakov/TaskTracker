package tracker.api.factories;

import org.springframework.stereotype.Component;

import tracker.api.dto.TaskDto;
import tracker.store.entities.TaskEntity;

@Component
public class TaskDtoFactory {

	public TaskDto makeTaskDto(TaskEntity entity) {
		return new TaskDto(
				entity.getTaskId(),
				entity.getName(),
				entity.getCreatedAt(),
				entity.getDescription());
				
	}
}
