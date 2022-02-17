package tracker.api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tracker.api.dto.ProjectDto;
import tracker.api.services.ProjectService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

	private final ProjectService projectService;

	@Autowired
	public ProjectController(ProjectService projectService) {
		super();
		this.projectService = projectService;
	}
	
	@GetMapping("/")
	public List<ProjectDto> fetchProjects() {
		return projectService.getAllProjectsByFilter(Optional.empty());
		
	}
	@GetMapping("/{prefix_name}")
	public List<ProjectDto> fetchProjectsByName(
			@PathVariable(value="prefix_name", required = false) 
			Optional<String> optionalPrefixName) {
		return projectService.getAllProjectsByFilter(optionalPrefixName);
		
	}
	
	
	@PostMapping("/")
	public ProjectDto createProject(@RequestParam String name){
		
		return projectService.createProject(name);
	}
	
	@PatchMapping("/{projectId}")
	public ProjectDto updateProject(
			@RequestParam String name, 
			@PathVariable("projectId") Long id) {
		
		return projectService.updateProject(name, id);
	}
	
	@DeleteMapping("/{projectId}")
	public String deleteProject(@PathVariable("projectId") Long id){
		projectService.deleteProject(id);
		return "It's okey. Project deleted";
	}
	
	
	
}
