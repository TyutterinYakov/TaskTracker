package tracker.store.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="projects")
public class ProjectEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="project_id")
	private Long projectId;
	@Column(unique = true)
	private String name;
	private LocalDateTime createdAt = LocalDateTime.now();
	@OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
//	@JoinColumn(name="project_id", referencedColumnName = "project_id")
	private List<TaskStateEntity> states = new ArrayList<>();
	
	public ProjectEntity() {
		super();
	}
	public ProjectEntity(String name) {
		super();
		this.name = name;
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
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public List<TaskStateEntity> getStates() {
		return states;
	}
	public void setStates(List<TaskStateEntity> states) {
		this.states = states;
	}
	
	
}
