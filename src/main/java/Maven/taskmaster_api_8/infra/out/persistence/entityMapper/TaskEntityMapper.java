package Maven.taskmaster_api_8.infra.out.persistence.entityMapper;

import org.springframework.stereotype.Component;

import Maven.taskmaster_api_8.domain.model.Project;
import Maven.taskmaster_api_8.domain.model.Task;
import Maven.taskmaster_api_8.infra.out.persistence.entity.ProjectEntity;
import Maven.taskmaster_api_8.infra.out.persistence.entity.TaskEntity;

@Component
public class TaskEntityMapper {
  public TaskEntity toEntity(Task task) {
    TaskEntity entity = new TaskEntity(task.getId(), task.getTitle());

    if (task.getProject() != null) {
      entity.setProjectEntity(new ProjectEntity(task.getProject().getId(), task.getProject().getName()));
    }

    return entity;
  }

  public Task toDomain(TaskEntity entity) {
    Task task = new Task(entity.getId(), entity.getTitle(), null);
    if (entity.getProjectEntity() != null) {
      task.setProject(new Project(entity.getProjectEntity().getId(), entity.getProjectEntity().getName(), null));
    }
    return task;
  }
}
