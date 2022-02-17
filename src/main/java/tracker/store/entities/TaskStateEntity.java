package tracker.store.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.lang.Nullable;


@Entity
@Table(name="task_states")
public class TaskStateEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="task_state_id")
	private Long taskStateId;
	private String name;
	@OneToOne
	@JoinColumn(nullable = true)
	private TaskStateEntity leftTaskState=null;
	@OneToOne
	@JoinColumn(nullable = true)
	private TaskStateEntity rigthTaskState=null;
	private LocalDateTime createdAt = LocalDateTime.now();
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	private ProjectEntity project;
	@OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
//	@JoinColumn(name="task_state_id", referencedColumnName = "task_state_id")
	private List<TaskEntity> tasks = new ArrayList<>();
	

	
	public Long getTaskStateId() {
		return taskStateId;
	}
	public void setTaskStateId(Long taskStateId) {
		this.taskStateId = taskStateId;
	}
	
	public Optional<TaskStateEntity> getLeftTaskState() {
		return Optional.ofNullable(leftTaskState);
	}
	public void setLeftTaskState(TaskStateEntity leftTaskState) {
		this.leftTaskState = leftTaskState;
	}
	public Optional<TaskStateEntity> getRigthTaskState() {
		return Optional.ofNullable(rigthTaskState);
	}
	public void setRigthTaskState(TaskStateEntity rigthTaskState) {
		this.rigthTaskState = rigthTaskState;
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
	public List<TaskEntity> getTasks() {
		return tasks;
	}
	public void setTasks(List<TaskEntity> tasks) {
		this.tasks = tasks;
	}
	public ProjectEntity getProject() {
		return project;
	}
	public void setProject(ProjectEntity project) {
		this.project = project;
	}
	
	
	
	
	
}
