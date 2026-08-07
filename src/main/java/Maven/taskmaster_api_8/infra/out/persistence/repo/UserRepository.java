package Maven.taskmaster_api_8.infra.out.persistence.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Maven.taskmaster_api_8.infra.out.persistence.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);
}
