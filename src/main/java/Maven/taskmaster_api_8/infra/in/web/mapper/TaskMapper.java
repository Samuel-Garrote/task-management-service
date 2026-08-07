package Maven.taskmaster_api_8.infra.in.web.mapper;

import org.springframework.stereotype.Component;

import Maven.taskmaster_api_8.domain.model.Task;
import Maven.taskmaster_api_8.infra.in.web.dto.TaskResponse;

@Component
public class TaskMapper {
  public TaskResponse toResponse(Task task) {
    TaskResponse response = new TaskResponse(task.getId(), task.getTitle(), null);
    return response;
  }
}
