package Maven.taskmaster_api_8.infra.out.messaging;

import org.springframework.kafka.annotation.KafkaListener;

import Maven.taskmaster_api_8.infra.in.web.dto.TaskCreatedEvent;

public class TaskEventConsumer {
  @KafkaListener(topics = "task-created", groupId = "taskmaster-group")
  public void consume(TaskCreatedEvent event) {
    System.out.println("Received event" + event);
  }
}
