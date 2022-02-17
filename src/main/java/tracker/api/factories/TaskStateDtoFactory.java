package tracker.api.factories;

import org.springframework.stereotype.Component;

import tracker.api.dto.TaskStateDto;
import tracker.store.entities.TaskStateEntity;

@Component
public class TaskStateDtoFactory {

	public TaskStateDto makeTaskStateDto(TaskStateEntity entity) {
		return new TaskStateDto(
				entity.getTaskStateId(),
				entity.getName(),
				entity.getOrdinal(),
				entity.getCreatedAt());
	}
}
