package tracker.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskStateDto {

	@JsonProperty("task_state_id")
	@NonNull
	private Long taskStateId;
	@NonNull
	private String name;
	@JsonProperty("left_task_state_id")
	@NonNull
	private Long leftTaskStateId;
	@JsonProperty("rigth_task_state_id")
	@NonNull
	private Long rigthTaskStateId;
	@JsonProperty("created_at")
	@NonNull
	private LocalDateTime createdAt;
	@NonNull
	private List<TaskDto> tasks;
	
	public TaskStateDto() {
		super();
	}
	
	public TaskStateDto(Long taskStateId, String name, Long leftTaskStateId, Long rigthTaskStateId,
			LocalDateTime createdAt, List<TaskDto> tasks) {
		super();
		this.taskStateId = taskStateId;
		this.name = name;
		this.leftTaskStateId = leftTaskStateId;
		this.rigthTaskStateId = rigthTaskStateId;
		this.createdAt = createdAt;
		this.tasks = tasks;
	}

	public Long getTaskStateId() {
		return taskStateId;
	}

	public void setTaskStateId(Long taskStateId) {
		this.taskStateId = taskStateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<TaskDto> getTasks() {
		return tasks;
	}


	public void setTasks(List<TaskDto> tasks) {
		this.tasks = tasks;
	}

	public Long getLeftTaskStateId() {
		return leftTaskStateId;
	}

	public void setLeftTaskStateId(Long leftTaskStateId) {
		this.leftTaskStateId = leftTaskStateId;
	}

	public Long getRigthTaskStateId() {
		return rigthTaskStateId;
	}

	public void setRigthTaskStateId(Long rigthTaskStateId) {
		this.rigthTaskStateId = rigthTaskStateId;
	}
	
	
	
	
}
