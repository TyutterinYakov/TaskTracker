package tracker.api.dto;

import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskStateDto {

	@JsonProperty("task_state_id")
	@NonNull
	private Long taskStateId;
	@NonNull
	private String name;
	@NonNull
	private Long ordinal;
	@JsonProperty("created_at")
	@NonNull
	private LocalDateTime createdAt;
	
	public TaskStateDto() {
		super();
	}

	public TaskStateDto(Long taskStateId, String name, Long ordinal, LocalDateTime createdAt) {
		super();
		this.taskStateId = taskStateId;
		this.name = name;
		this.ordinal = ordinal;
		this.createdAt = createdAt;
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

	public Long getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Long ordinal) {
		this.ordinal = ordinal;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
}
