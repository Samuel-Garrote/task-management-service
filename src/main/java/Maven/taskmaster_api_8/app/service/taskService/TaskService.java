package Maven.taskmaster_api_8.app.service.taskService;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import Maven.taskmaster_api_8.app.port.in.CreateTaskUseCase;
import Maven.taskmaster_api_8.app.port.in.DeleteTaskUseCase;
import Maven.taskmaster_api_8.app.port.in.FindByProjectIdUseCase;
import Maven.taskmaster_api_8.app.port.in.FindTaskByIdUseCase;
import Maven.taskmaster_api_8.app.port.in.UpdateTaskUseCase;
import Maven.taskmaster_api_8.app.port.out.TaskRepositoryPort;
import Maven.taskmaster_api_8.domain.model.Task;
import Maven.taskmaster_api_8.infra.in.web.dto.TaskCreatedEvent;
import Maven.taskmaster_api_8.infra.out.messaging.TaskEventProducer;

@Service
public class TaskService
    implements CreateTaskUseCase, FindByProjectIdUseCase, FindTaskByIdUseCase, DeleteTaskUseCase,
    UpdateTaskUseCase {

  private final TaskRepositoryPort taskRepositoryPort;
  private final TaskEventProducer taskEventProducer;

  TaskService(TaskRepositoryPort taskRepositoryPort, TaskEventProducer taskEventProducer) {
    this.taskRepositoryPort = taskRepositoryPort;
    this.taskEventProducer = taskEventProducer;
  }

  @Override
  public Task createTask(Task task) {
    Task savedTask = taskRepositoryPort.save(task);
    taskEventProducer.publishTaskCreated(new TaskCreatedEvent(savedTask.getId(), savedTask.getTitle()));
    return savedTask;
  }

  @Override
  public Task updateTask(Long id, Task task) {
    return taskRepositoryPort.updateTask(id, task);
  }

  @Override
  public Optional<Task> findTaskById(Long id) {
    return taskRepositoryPort.findTaskById(id);
  }

  @Override
  public void deleteTask(Long id) {
    taskRepositoryPort.deleteTask(id);
  }

  @Override
  public List<Task> findByProjectId(Long id) {
    return taskRepositoryPort.findByProjectId(id);
  }
}
