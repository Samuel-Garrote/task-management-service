package Maven.taskmaster_api_8.infra.in.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Maven.taskmaster_api_8.app.port.in.CreateTaskUseCase;
import Maven.taskmaster_api_8.app.port.in.DeleteTaskUseCase;
import Maven.taskmaster_api_8.app.port.in.FindByProjectIdUseCase;
import Maven.taskmaster_api_8.app.port.in.FindTaskByIdUseCase;
import Maven.taskmaster_api_8.app.port.in.UpdateTaskUseCase;
import Maven.taskmaster_api_8.domain.model.Task;
import Maven.taskmaster_api_8.infra.in.web.dto.TaskResponse;
import Maven.taskmaster_api_8.infra.in.web.mapper.TaskMapper;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {
  private final CreateTaskUseCase createTaskUseCase;
  private final FindTaskByIdUseCase findTaskByIdUseCase;
  private final UpdateTaskUseCase updateTaskUseCase;
  private final DeleteTaskUseCase deleteTaskUseCase;
  private final FindByProjectIdUseCase findByProjectIdUseCase;
  private final TaskMapper taskMapper;

  TaskController(CreateTaskUseCase createTaskUseCase, FindTaskByIdUseCase findTaskByIdUseCase,
      UpdateTaskUseCase updateTaskUseCase, DeleteTaskUseCase deleteTaskUseCase,
      FindByProjectIdUseCase findByProjectIdUseCase, TaskMapper taskMapper) {
    this.createTaskUseCase = createTaskUseCase;
    this.findTaskByIdUseCase = findTaskByIdUseCase;
    this.updateTaskUseCase = updateTaskUseCase;
    this.deleteTaskUseCase = deleteTaskUseCase;
    this.findByProjectIdUseCase = findByProjectIdUseCase;
    this.taskMapper = taskMapper;
  }

  @PostMapping()
  public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody Task task) {
    Task createTask = createTaskUseCase.createTask(task);
    return ResponseEntity.ok(taskMapper.toResponse(createTask));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskResponse> findById(@PathVariable Long id) {
    Optional<Task> taskFound = findTaskByIdUseCase.findTaskById(id);

    if (taskFound.isPresent()) {
      return ResponseEntity.ok(taskMapper.toResponse(taskFound.get()));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody Task task) {
    Task updatedTask = updateTaskUseCase.updateTask(id, task);
    return ResponseEntity.ok(taskMapper.toResponse(updatedTask));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    deleteTaskUseCase.deleteTask(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/project/{projectId}")
  public ResponseEntity<List<TaskResponse>> findByProjectId(@PathVariable Long projectId) {
    List<TaskResponse> tasks = findByProjectIdUseCase.findByProjectId(projectId).stream()
        .map(task -> taskMapper.toResponse(task))
        .toList();
    return ResponseEntity.ok(tasks);
  }
}
