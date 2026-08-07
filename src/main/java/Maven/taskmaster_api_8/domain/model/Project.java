package Maven.taskmaster_api_8.domain.model;

import java.util.List;

public class Project {
  private Long id;
  private String name;
  private List<Task> tasks;

  public Project() {
  }

  public Project(Long id, String name, List<Task> tasks) {
    this.id = id;
    this.name = name;
    this.tasks = tasks;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<Task> getTasks() {
    return tasks;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setTasks(List<Task> tasks) {
    this.tasks = tasks;
  }

}
