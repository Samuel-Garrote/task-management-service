package Maven.taskmaster_api_8.app.port.in;

import java.util.List;

import Maven.taskmaster_api_8.domain.model.Task;

public interface FindByProjectIdUseCase {
  List<Task> findByProjectId(Long projectId);
}
