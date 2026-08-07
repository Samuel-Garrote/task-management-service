package Maven.taskmaster_api_8.app.port.in;

import Maven.taskmaster_api_8.domain.model.Task;

public interface CreateTaskUseCase {
  Task createTask(Task task);
}
