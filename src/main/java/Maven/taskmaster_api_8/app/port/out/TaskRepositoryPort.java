package Maven.taskmaster_api_8.app.port.out;

import java.util.List;
import java.util.Optional;

import Maven.taskmaster_api_8.domain.model.Task;

public interface TaskRepositoryPort {
  Task save(Task task);

  Task createTask(Task task);

  Optional<Task> findTaskById(Long id);

  void deleteTask(Long id);

  Task updateTask(Long id, Task task);

  List<Task> findByProjectId(Long projectId);

}
