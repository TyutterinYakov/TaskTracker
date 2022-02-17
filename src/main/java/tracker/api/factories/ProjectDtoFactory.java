package tracker.api.factories;

import org.springframework.stereotype.Component;

import tracker.api.dto.ProjectDto;
import tracker.store.entities.ProjectEntity;

@Component
public class ProjectDtoFactory {

	public ProjectDto makeProjectDto(ProjectEntity entity) {
		return new ProjectDto(
				entity.getProjectId(), 
				entity.getName(), 
				entity.getCreatedAt());
	}
	
}
