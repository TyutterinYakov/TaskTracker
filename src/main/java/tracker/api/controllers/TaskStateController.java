package tracker.api.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tracker.api.dto.TaskStateDto;
import tracker.api.services.TaskStateService;

@RestController
public class TaskStateController {

	private final TaskStateService taskStateService;

	@Autowired
	public TaskStateController(TaskStateService taskStateService) {
		super();
		this.taskStateService = taskStateService;
	}
	
	public static final String GET_TASK_STATES = "/api/projects/{project_id}/task-states";
	public static final String CREATE_TASK_STATE = "/api/projects/{project_id}/task-states";
	public static final String UPDATE_TASK_STATE = "/api/task-states/{task_state_id}";
	public static final String CHANGE_TASK_STATE_POSITION = "/api/task-states/{task_state_id}/position/change";
	public static final String DELETE_TASK_STATE = "/api/task-states/{task_state_id}";
	
	
	
	@GetMapping(GET_TASK_STATES)
	public List<TaskStateDto> fetchTaskStates(@PathVariable("project_id") Long id){
		return taskStateService.getAllTaskStatesByProjectId(id);
	}
	
	
	@PostMapping(CREATE_TASK_STATE)
	public TaskStateDto createTaskState(
			@PathVariable("project_id") Long projectId,
			@RequestParam("task_state_name") String taskStateName){
		
		
		return taskStateService.createTaskState(projectId, taskStateName);
	}
	
	@PatchMapping(UPDATE_TASK_STATE)
	public TaskStateDto updateTaskState(
			@PathVariable("task_state_id") Long taskStateId,
			@RequestParam("task_state_name") String taskStateName) {
		return taskStateService.updateTaskStateNameById(taskStateId, taskStateName);
	}
	
	@PatchMapping(CHANGE_TASK_STATE_POSITION)
	public TaskStateDto changeTaskPosition(
			@RequestParam(value="left_task_state_id", required=false) Long leftTaskStateId,
			@RequestParam(value="rigth_task_state_id", required=false) Long rigthTaskStateId,
			@PathVariable("task_state_id") Long taskStateId) {
		return taskStateService.changeTaskStatePosition(leftTaskStateId, rigthTaskStateId, taskStateId);
	}
	
	@DeleteMapping(DELETE_TASK_STATE)
	public String deleteTaskState(@PathVariable("task_state_id") Long taskStateId) {
		taskStateService.deleteTaskStateById(taskStateId);
		return "Task state deleted";
	}
	
	
}
