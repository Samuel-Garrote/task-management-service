package Maven.taskmaster_api_8;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import Maven.taskmaster_api_8.app.port.out.TaskRepositoryPort;
import Maven.taskmaster_api_8.app.service.taskService.TaskService;
import Maven.taskmaster_api_8.domain.model.Project;
import Maven.taskmaster_api_8.domain.model.Task;
import Maven.taskmaster_api_8.infra.in.web.dto.TaskCreatedEvent;
import Maven.taskmaster_api_8.infra.out.messaging.TaskEventProducer;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
  @Mock
  private TaskRepositoryPort taskRepositoryPort;
  @Mock
  private TaskEventProducer taskEventProducer;
  @InjectMocks
  private TaskService taskService;

  @Test
  void shouldCreateTask() {
    Project project = new Project(1L, "Groceries", new ArrayList<>());
    Task task = new Task(null, "Buy milk", project);
    Task savedTask = new Task(1L, "Buy milk", project);

    when(taskRepositoryPort.save(task)).thenReturn(savedTask);

    Task result = taskService.createTask(task);

    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getTitle()).isEqualTo("Buy milk");
    verify(taskRepositoryPort).save(task);
    verify(taskEventProducer).publishTaskCreated(new TaskCreatedEvent(1L, "Buy milk"));
  }

  @Test
  void shouldReturnTaskFound() {
    Project project = new Project(1L, "project1", new ArrayList<>());
    Task task = new Task(1L, "task1", project);

    when(taskRepositoryPort.findTaskById(1L)).thenReturn(Optional.of(task));

    Optional<Task> result = taskService.findTaskById(1L);

    assertThat(result).isPresent();
    assertThat(result.get().getTitle()).isEqualTo("task1");
    verify(taskRepositoryPort).findTaskById(1L);
  }

  @Test
  void shouldReturnTaskNotFound() {
    when(taskRepositoryPort.findTaskById(99L)).thenReturn(Optional.empty());

    Optional<Task> result = taskService.findTaskById(99L);

    assertThat(result).isEmpty();
    verify(taskRepositoryPort).findTaskById(99L);
  }

  @Test
  void shouldUpdateTask() {
    Project project = new Project(1L, "project1", new ArrayList<>());
    Task task = new Task(1L, "task1", project);
    Task updatedTask = new Task(1L, "Task1", project);

    when(taskRepositoryPort.updateTask(1L, task)).thenReturn(updatedTask);

    Task result = taskService.updateTask(1L, task);

    assertThat(result.getTitle()).isEqualTo("Task1");
    verify(taskRepositoryPort).updateTask(1L, task);
  }

  @Test
  void shouldDeleteTask() {
    taskService.deleteTask(1L);
    verify(taskRepositoryPort).deleteTask(1L);
  }

  @Test
  void shouldReturnTasksWhenProjectHasTasks() {
    Project project = new Project(1L, "project1", new ArrayList<>());
    Task task = new Task(1L, "hello", project);
    Task task2 = new Task(1L, "bye", project);

    when(taskRepositoryPort.findByProjectId(1L)).thenReturn(List.of(task, task2));

    List<Task> result = taskService.findByProjectId(1L);

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getTitle()).isEqualTo("hello");
    assertThat(result.get(1).getTitle()).isEqualTo("bye");
    verify(taskRepositoryPort).findByProjectId(1L);
  }
}
