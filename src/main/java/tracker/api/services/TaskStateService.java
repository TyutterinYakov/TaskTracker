package tracker.api.services;

import java.util.List;

import tracker.api.dto.TaskStateDto;

public interface TaskStateService {

	List<TaskStateDto> getAllTaskStatesByProjectId(Long id);

	TaskStateDto createTaskState(Long projectId, String name);

	TaskStateDto updateTaskStateNameById(Long taskStateId, String taskStateName);

	TaskStateDto changeTaskStatePosition(Long leftTaskStateId, Long rigthTaskStateId, Long taskStateId);

}
