package Maven.taskmaster_api_8.domain.model;

import jakarta.validation.constraints.NotBlank;

public class Task {
  @NotBlank
  String title;
  Long id;
  private Project project;

  public Task() {
  }

  public Task(Long id, String title, Project project) {
    this.id = id;
    this.title = title;
    this.project = project;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public Project getProject() {
    return project;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}
