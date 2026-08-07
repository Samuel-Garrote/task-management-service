package Maven.taskmaster_api_8.infra.out.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import Maven.taskmaster_api_8.infra.in.web.dto.TaskCreatedEvent;

@Component
public class TaskEventProducer {
  private final KafkaTemplate<String, TaskCreatedEvent> kafkaTemplate;

  TaskEventProducer(KafkaTemplate<String, TaskCreatedEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void publishTaskCreated(TaskCreatedEvent event) {
    kafkaTemplate.send("task-created", event);
  }
}
