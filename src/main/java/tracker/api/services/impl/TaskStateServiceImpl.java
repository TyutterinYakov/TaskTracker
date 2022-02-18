package tracker.api.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tracker.api.dto.TaskStateDto;
import tracker.api.exceptions.BadRequestException;
import tracker.api.exceptions.NotFoundException;
import tracker.api.factories.TaskStateDtoFactory;
import tracker.api.services.ProjectService;
import tracker.api.services.TaskStateService;
import tracker.store.entities.ProjectEntity;
import tracker.store.entities.TaskStateEntity;
import tracker.store.repositories.TaskStateRepository;

@Service
public class TaskStateServiceImpl implements TaskStateService {

	private static final Logger logger = LoggerFactory.getLogger(TaskStateServiceImpl.class);
	
	private final TaskStateRepository taskStateDao;
	private final TaskStateDtoFactory taskStateDtoFactory;
	private final ProjectService projectService;
	
	
	@Autowired
	public TaskStateServiceImpl(TaskStateRepository taskStateDao,
			TaskStateDtoFactory taskStateDtoFactory, ProjectService projectService) {
		super();
		this.taskStateDao = taskStateDao;
		this.taskStateDtoFactory = taskStateDtoFactory;
		this.projectService = projectService;
	}

	@Override
	public List<TaskStateDto> getAllTaskStatesByProjectId(Long projectId) {
		ProjectEntity project = projectService.findProjectById(projectId);
		return project
				.getTaskStates()
				.stream()
				.map(taskStateDtoFactory::makeTaskStateDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public TaskStateDto createTaskState(Long projectId, String taskStateName) {
		boolean checkSave=false;
		ProjectEntity project = projectService.findProjectById(projectId);
		checkTaskStateNameForProject(project, taskStateName);
		TaskStateEntity taskState = new TaskStateEntity();
		taskState.setProject(project);
		taskState.setName(taskStateName);
		Optional<TaskStateEntity> taskStateOptional = 
				taskStateDao.findByRigthTaskStateAndProject(null, project);
		if(taskStateOptional.isPresent()) {
			taskState.setLeftTaskState(taskStateOptional.get());
			taskState=taskStateDao.saveAndFlush(taskState);
			checkSave=true;
			taskStateOptional.get().setRigthTaskState(taskState);
		}
		if(!checkSave) {
			taskState = taskStateDao.saveAndFlush(taskState);
		}
		return taskStateDtoFactory
				.makeTaskStateDto(taskState);
	}

	@Override
	@Transactional
	public TaskStateDto updateTaskStateNameById(Long taskStateId, String taskStateName) {
		checkTaskStateNameEmpty(taskStateName);
		TaskStateEntity taskState = findTaskStateById(taskStateId);
		if(!taskStateName.equalsIgnoreCase(taskState.getName())) {
			checkTaskStateNameForProject(taskState.getProject(), taskStateName);
		}
		taskState.setName(taskStateName);
		return taskStateDtoFactory.makeTaskStateDto(taskState);
	}
	
	@Override
	@Transactional
	public TaskStateDto changeTaskStatePosition(
			Long leftTaskStateId,
			Long rigthTaskStateId, 
			Long taskStateId) {
		
		TaskStateEntity oldTaskState = findTaskStateById(taskStateId);
		
		if(oldTaskState.getProject().getTaskStates().size()<=1||
				leftTaskStateId==rigthTaskStateId||
				taskStateId==leftTaskStateId||
				taskStateId==rigthTaskStateId
				) {
			throw new BadRequestException("Нельзя перемещать "
					+ "единственный Task state или на это место перемещение невозможно");
		}
		Optional<TaskStateEntity> newLeftTaskStateOptional = 
				taskStateDao.findByTaskStateIdAndProject(leftTaskStateId, oldTaskState.getProject());
		Optional<TaskStateEntity> newRigthTaskStateOptional = 
				taskStateDao.findByTaskStateIdAndProject(rigthTaskStateId, oldTaskState.getProject());
		boolean check=false;
		boolean extreamCheck = false;
		
		if(newLeftTaskStateOptional.isPresent()) {
			if(newLeftTaskStateOptional.get().getRigthTaskState().isPresent()) {
				if(newLeftTaskStateOptional.get()
						.getRigthTaskState().get()
						.getTaskStateId()==rigthTaskStateId) {
					check=true;
				}
			} else {
				extreamCheck=true;
			}
		} else if(leftTaskStateId==0) {
			extreamCheck=true;
		}
		if(!check) {
			if(newRigthTaskStateOptional.isPresent()) {
				if(newRigthTaskStateOptional.get().getLeftTaskState().isPresent()) {
					if(newRigthTaskStateOptional.get()
							.getLeftTaskState().get()
							.getTaskStateId()==leftTaskStateId) {
						check=true;
					}
				} else if(extreamCheck) {
					check=true;
				}
			} else if(rigthTaskStateId==0) {
				check=true;
			}
		}
		if(check) {
			Optional<TaskStateEntity> oldLeftTaskStateOptional = oldTaskState.getLeftTaskState();
			Optional<TaskStateEntity> oldRightTaskStateOptional = oldTaskState.getRigthTaskState();
			if(oldLeftTaskStateOptional.isPresent()) {
				oldLeftTaskStateOptional.get().setRigthTaskState(oldRightTaskStateOptional.orElse(null));
			}
			if(oldRightTaskStateOptional.isPresent()) {
				oldRightTaskStateOptional.get().setLeftTaskState(oldLeftTaskStateOptional.orElse(null));
			}
				if(newLeftTaskStateOptional.isPresent()) {
					newLeftTaskStateOptional.get().setRigthTaskState(oldTaskState);
				}
			TaskStateEntity changeTaskState = oldTaskState; 
			changeTaskState.setLeftTaskState(newLeftTaskStateOptional.orElse(null));
				if(newRigthTaskStateOptional.isPresent()) {
					newRigthTaskStateOptional.get().setLeftTaskState(oldTaskState);
				}
			changeTaskState.setRigthTaskState(newRigthTaskStateOptional.orElse(null));
			return taskStateDtoFactory.makeTaskStateDto(changeTaskState);
		}
		throw new BadRequestException("Ошибка при перемещении");
	}
	
	
	@Override
	@Transactional
	public void deleteTaskStateById(Long taskStateId) {
		TaskStateEntity taskState = findTaskStateById(taskStateId);
		Optional<TaskStateEntity> optionalLeftTaskState = taskState.getLeftTaskState();
		Optional<TaskStateEntity> optionalRigthTaskState = taskState.getRigthTaskState();
		
		if (optionalLeftTaskState.isPresent()) {
			optionalLeftTaskState.get().setRigthTaskState(optionalRigthTaskState.orElse(null));
		}
		if (optionalRigthTaskState.isPresent()) {
			optionalRigthTaskState.get().setLeftTaskState(optionalLeftTaskState.orElse(null));
		}
		taskStateDao.deleteById(taskStateId);
	}
	
	
	private void checkTaskStateNameForProject(ProjectEntity project, String taskStateName) {
		checkTaskStateNameEmpty(taskStateName);
		taskStateDao.findByProjectAndNameIsIgnoreCase(project, taskStateName)
			.ifPresent((taskState)->{
				throw new BadRequestException(
						String.format(
								"TaskState name \"%s\" already exists this project", 
								taskStateName
						)
				);
			}
		);
	}
	
	private void checkTaskStateNameEmpty(String taskStateName) {
		if(taskStateName.isBlank()) {
			throw new BadRequestException("TaskState name is empty");
		}
	}
	
	private TaskStateEntity findTaskStateById(Long taskStateId) {
		if(taskStateId==null||taskStateId==0) {
			throw new BadRequestException("Task state id is null or 0");
		}
		return taskStateDao.findById(taskStateId).orElseThrow(()->
		new NotFoundException(String.format(
				"Task State for id \"%s\" not found", 
				taskStateId
				)
		)
);
	}




}
