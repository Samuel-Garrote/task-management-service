package Maven.taskmaster_api_8.app.port.in;

import java.util.Optional;

import Maven.taskmaster_api_8.domain.model.Task;

public interface FindTaskByIdUseCase {
  Optional<Task> findTaskById(Long id);
}
