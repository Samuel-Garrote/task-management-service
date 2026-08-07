package Maven.taskmaster_api_8.infra.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import Maven.taskmaster_api_8.app.port.out.TaskRepositoryPort;
import Maven.taskmaster_api_8.domain.model.Task;
import Maven.taskmaster_api_8.infra.out.persistence.entity.TaskEntity;
import Maven.taskmaster_api_8.infra.out.persistence.entityMapper.TaskEntityMapper;
import Maven.taskmaster_api_8.infra.out.persistence.repo.TaskRepository;

@Component
public class TaskRepositoryAdapter implements TaskRepositoryPort {
  private final TaskRepository taskRepository;
  private final TaskEntityMapper taskEntityMapper;

  public TaskRepositoryAdapter(TaskRepository taskRepository, TaskEntityMapper taskEntityMapper) {
    this.taskRepository = taskRepository;
    this.taskEntityMapper = taskEntityMapper;
  }

  @Override
  public Task save(Task task) {
    TaskEntity entity = taskEntityMapper.toEntity(task);
    TaskEntity savedEntity = taskRepository.save(entity);
    return taskEntityMapper.toDomain(savedEntity);
  }

  @Override
  public Task createTask(Task task) {
    TaskEntity entity = taskEntityMapper.toEntity(task);
    TaskEntity savedEntity = taskRepository.save(entity);

    return taskEntityMapper.toDomain(savedEntity);
  }

  @Override
  public Task updateTask(Long id, Task task) {
    task.setId(id);

    TaskEntity entity = taskEntityMapper.toEntity(task);
    TaskEntity savedEntity = taskRepository.save(entity);

    return taskEntityMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Task> findTaskById(Long id) {
    return taskRepository.findById(id)
        .map(entity -> taskEntityMapper.toDomain(entity));
  }

  @Override
  public List<Task> findByProjectId(Long projectId) {

    return taskRepository.findByProjectEntityId(projectId).stream()
        .map(e -> taskEntityMapper.toDomain(e))
        .toList();
  }

  @Override
  public void deleteTask(Long id) {
    taskRepository.deleteById(id);
  }
}
