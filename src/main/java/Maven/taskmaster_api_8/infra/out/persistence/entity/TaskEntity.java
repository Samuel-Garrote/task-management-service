package Maven.taskmaster_api_8.infra.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class TaskEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)

  Long id;
  @NotBlank
  String title;

  @ManyToOne
  private ProjectEntity projectEntity;

  public TaskEntity() {
  }

  public TaskEntity(Long id, String title) {
    this.id = id;
    this.title = title;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public ProjectEntity getProjectEntity() {
    return projectEntity;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setProjectEntity(ProjectEntity projectEntity) {
    this.projectEntity = projectEntity;
  }

}
