package tracker.api.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tracker.api.factories.TaskDtoFactory;
import tracker.api.services.TaskService;
import tracker.store.repositories.TaskRepository;


@Service
public class TaskServiceImpl implements TaskService{
	
	private final TaskRepository taskDao;
	private final TaskDtoFactory taskDtoFactory;
	
	@Autowired
	public TaskServiceImpl(TaskRepository taskDao, TaskDtoFactory taskDtoFactory) {
		super();
		this.taskDao = taskDao;
		this.taskDtoFactory = taskDtoFactory;
	}

	
	
	

}
