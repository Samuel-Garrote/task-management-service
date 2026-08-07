package Maven.taskmaster_api_8.infra.out.persistence.entityMapper;

import Maven.taskmaster_api_8.domain.model.Project;
import Maven.taskmaster_api_8.infra.out.persistence.entity.ProjectEntity;

public class ProjectEntityMapper {
  public ProjectEntity toEntity(Project project) {
    return new ProjectEntity(project.getId(), project.getName());
  }

  public Project toDomain(ProjectEntity projectEntity) {
    return new Project(projectEntity.getId(), projectEntity.getName(), null);
  }
}
