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
		if(taskStateId==null||taskStateId==0) {
			throw new BadRequestException("Task state id is null or 0");
		}
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
		
		if(oldTaskState.getProject().getTaskStates().size()<=1||(leftTaskStateId+rigthTaskStateId)==0) {
			throw new BadRequestException("Нельзя перемещать единственный Task state");
		}
		
		Optional<TaskStateEntity> leftTaskStateOptional = taskStateDao.findById(leftTaskStateId);
		Optional<TaskStateEntity> rigthTaskStateOptional = taskStateDao.findById(rigthTaskStateId);
		Long rigthId=null;
		Long leftId=null;
		
		if(leftTaskStateOptional.isPresent()) {
			if(leftTaskStateOptional.get().getRigthTaskState().isPresent()) {
				rigthId = leftTaskStateOptional.get().getRigthTaskState().orElse(null).getTaskStateId();
			}
		}
		if(rigthTaskStateOptional.isPresent()) {
			if(rigthTaskStateOptional.get().getLeftTaskState().isPresent()) {
				leftId = rigthTaskStateOptional.get().getLeftTaskState().get().getTaskStateId();
			}
		}
		
		if(rigthId==leftId) {
			
			Optional<TaskStateEntity> taskStateOptionalLeft = oldTaskState.getLeftTaskState();
			Optional<TaskStateEntity> taskStateOptionalRigth = oldTaskState.getRigthTaskState();
			if(taskStateOptionalLeft.isPresent()) {
				taskStateOptionalLeft.get().setRigthTaskState(taskStateOptionalRigth.orElse(null));
			}
			if(taskStateOptionalRigth.isPresent()) {
				taskStateOptionalRigth.get().setLeftTaskState(taskStateOptionalLeft.orElse(null));
			}
			TaskStateEntity taskStateLeft =null; 
			taskStateLeft = taskStateDao.findByTaskStateIdAndProject(leftTaskStateId, oldTaskState.getProject()).orElse(null);
				if(taskStateLeft!=null) {
					taskStateLeft.setRigthTaskState(oldTaskState);
				}
			TaskStateEntity changeTaskState = oldTaskState; 
			changeTaskState.setLeftTaskState(taskStateLeft);
			TaskStateEntity taskStateRigth =null;
			taskStateRigth = taskStateDao.findByTaskStateIdAndProject(rigthTaskStateId, oldTaskState.getProject()).orElse(null);
				if(taskStateRigth!=null) {
					taskStateRigth.setLeftTaskState(oldTaskState);
				}
			changeTaskState.setRigthTaskState(taskStateRigth);
			
			return taskStateDtoFactory.makeTaskStateDto(changeTaskState);
		}
		
		throw new BadRequestException("Ошибка при перемещении");
		
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
		return taskStateDao.findById(taskStateId).orElseThrow(()->
		new NotFoundException(String.format(
				"Task State for id \"%s\" not found", 
				taskStateId
				)
		)
);
	}



}
