package Maven.taskmaster_api_8;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import Maven.taskmaster_api_8.app.port.in.CreateTaskUseCase;
import Maven.taskmaster_api_8.app.port.in.DeleteTaskUseCase;
import Maven.taskmaster_api_8.app.port.in.FindByProjectIdUseCase;
import Maven.taskmaster_api_8.app.port.in.FindTaskByIdUseCase;
import Maven.taskmaster_api_8.app.port.in.UpdateTaskUseCase;
import Maven.taskmaster_api_8.domain.model.Project;
import Maven.taskmaster_api_8.domain.model.Task;
import Maven.taskmaster_api_8.infra.in.web.controller.TaskController;
import Maven.taskmaster_api_8.infra.in.web.dto.TaskResponse;
import Maven.taskmaster_api_8.infra.in.web.mapper.TaskMapper;
import Maven.taskmaster_api_8.infra.security.JwtService;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)

public class TaskControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  JwtService jwtService;

  @MockitoBean
  private CreateTaskUseCase createTaskUseCase;

  @MockitoBean
  private FindTaskByIdUseCase findTaskByIdUseCase;

  @MockitoBean
  private UpdateTaskUseCase updateTaskUseCase;

  @MockitoBean
  private DeleteTaskUseCase deleteTaskUseCase;

  @MockitoBean
  private FindByProjectIdUseCase findByProjectIdUseCase;

  @MockitoBean
  private TaskMapper taskMapper;

  @Test

  void shouldCreateTaskAndReturn200() throws Exception {

    // Arrange
    Project project = new Project(1L, "project", new ArrayList<>());
    Task savedTask = new Task(1L, "task1", project);
    TaskResponse response = new TaskResponse(1L, "task1", 1L);

    when(createTaskUseCase.createTask(any(Task.class))).thenReturn(savedTask);
    when(taskMapper.toResponse(savedTask)).thenReturn(response);

    String requestBody = """
        {
        "title":"task1"
        }
        """;

    // Act and Assert
    mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(requestBody))

        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.title").value("task1"));
  }

  @Test

  void shouldReturn400WhenTitleIsBlank() throws Exception {
    String requestBody = """
            {
        "title":""
            }
            """;
    mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(requestBody))

        .andExpect(status().isBadRequest());
  }
}
