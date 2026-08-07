package Maven.taskmaster_api_8.infra.out.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Maven.taskmaster_api_8.infra.out.persistence.entity.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
  List<TaskEntity> findByProjectEntityId(Long projectId);
}
