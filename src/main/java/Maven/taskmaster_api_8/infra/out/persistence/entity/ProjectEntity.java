package Maven.taskmaster_api_8.infra.out.persistence.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity

public class ProjectEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  String name;

  @OneToMany
  List<TaskEntity> tasks;

  public ProjectEntity() {
  }

  public ProjectEntity(Long id, String name, List<TaskEntity> tasks) {
    this.id = id;
    this.name = name;
    this.tasks = tasks;
  }

  public ProjectEntity(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<TaskEntity> getTasks() {
    return tasks;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setTasks(List<TaskEntity> tasks) {
    this.tasks = tasks;
  }

}
