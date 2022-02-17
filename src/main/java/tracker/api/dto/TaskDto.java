package tracker.api.dto;

import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskDto {
	
	@NonNull
	@JsonProperty("task_id")
	private Long taskId;
	@NonNull
	private String name;
	@NonNull
	@JsonProperty("created_at")
	private LocalDateTime createdAt;
	@NonNull
	private String description;
	
	public TaskDto() {
		super();
	}

	public TaskDto(Long taskId, String name, LocalDateTime createdAt,
			String description) {
		super();
		this.taskId = taskId;
		this.name = name;
		this.createdAt = createdAt;
		this.description = description;
	}


	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
}
