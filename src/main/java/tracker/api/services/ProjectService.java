package tracker.api.services;

import java.util.List;
import java.util.Optional;

import tracker.api.dto.ProjectDto;

public interface ProjectService {

	public ProjectDto createProject(String name);

	public ProjectDto updateProject(String name, Long id);

	public List<ProjectDto> getAllProjectsByFilter(Optional<String> optionalPrefixName);

	public void deleteProject(Long id);
	
}
