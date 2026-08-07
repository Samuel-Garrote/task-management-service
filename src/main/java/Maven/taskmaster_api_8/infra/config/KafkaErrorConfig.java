package Maven.taskmaster_api_8.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorConfig {
  @Bean
  public DefaultErrorHandler errorHandler(KafkaOperations<String, Object> KafkaOperations) {
    DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(KafkaOperations);
    return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 3));
  }
}
