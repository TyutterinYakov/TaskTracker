package tracker.api.dto;

import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectDto {
	@NonNull
	private Long projectId;
	@NonNull
	private String name;
	@NonNull
	@JsonProperty("created_at")
	private LocalDateTime createdAt;
	
	public ProjectDto() {
		super();
	}
	public ProjectDto(Long projectId, String name, LocalDateTime createdAt) {
		super();
		this.projectId = projectId;
		this.name = name;
		this.createdAt = createdAt;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
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
	public void setCreatedAt(LocalDateTime createAt) {
		this.createdAt = createAt;
	}
	
	
}
