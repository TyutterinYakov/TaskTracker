package tracker.api.services.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tracker.api.dto.ProjectDto;
import tracker.api.exceptions.BadRequestException;
import tracker.api.exceptions.NotFoundException;
import tracker.api.factories.ProjectDtoFactory;
import tracker.api.services.ProjectService;
import tracker.store.entities.ProjectEntity;
import tracker.store.repositories.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

	private final ProjectDtoFactory projectDtoFactory;
	private final ProjectRepository projectDao;
	
	@Autowired
	public ProjectServiceImpl(ProjectDtoFactory projectDtoFactory, ProjectRepository projectDao) {
		super();
		this.projectDtoFactory = projectDtoFactory;
		this.projectDao = projectDao;
	}

	@Override
	public ProjectDto createProject(String name) {
		checkAlreadyProjectName(name);
		return projectDtoFactory
				.makeProjectDto(projectDao
						.saveAndFlush(
								new ProjectEntity(name)));
	}

	@Override
	@Transactional
	public ProjectDto updateProject(String name, Long id) {
		checkNameIsEmpty(name);
		
		ProjectEntity project = projectDao
				.findById(id)
				.orElseThrow(()->
					new NotFoundException(
							String.format(
									"Project \"%s\" not found", 
									name
							)
					)
				);
		projectDao
			.findByName(name)
			.filter(anotherProject -> !Objects.equals(anotherProject.getProjectId(), project.getProjectId()))
			.ifPresent(anotherProject ->{
				throw new BadRequestException(
						String.format(
								"Project \"%s\" already exists", 
								name
						)
				);
			});
		
		project.setName(name);
		return projectDtoFactory
				.makeProjectDto(project);
	}
	
	@Override
	@Transactional
	public List<ProjectDto> getAllProjectsByFilter(Optional<String> optionalPrefixName) {
		
		optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());
//		System.out.println(optionalPrefixName.get());
		
		Stream<ProjectEntity> projectStream = optionalPrefixName
				.map(projectDao::streamAllByNameStartsWithIgnoreCase)
				.orElseGet((projectDao::streamAllBy));
		
//		if(optionalPrefixName.isPresent()) {
//			projectStream = projectDao.streamAllByNameStartsWithIgnoreCase(optionalPrefixName.get());
//		} else {
//			projectStream = projectDao.streamAll();
//		}
		return projectStream.map(
				projectDtoFactory::makeProjectDto)
				.collect(Collectors.toList());
	}
	
	@Override
	public void deleteProject(Long id) {
		findProjectById(id);
		projectDao.findById(id);
	}
	
	@Override
	public ProjectEntity findProjectById(Long id) {
		if(id==0||id==null) {
			throw new BadRequestException("Id is empty or 0");
		}
		return projectDao
				.findById(id)
				.orElseThrow(()->
					new NotFoundException(
							String.format(
								"Project id: \"%s\" not found",
								id
							)
					)
				);
	}
	
	private void checkAlreadyProjectName(String name) {
		checkNameIsEmpty(name);
		projectDao
		.findByName(name)
		.ifPresent((project)->{
			throw new BadRequestException(String.format("Project \"%s\" already exists", name));
		});
	}
	
	private void checkNameIsEmpty(String name) {
		if(name.isBlank()) {
			throw new BadRequestException("Project name is empty");
		}
	}
	



}
