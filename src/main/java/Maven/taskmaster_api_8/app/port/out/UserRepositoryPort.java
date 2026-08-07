package Maven.taskmaster_api_8.app.port.out;

import java.util.Optional;

import Maven.taskmaster_api_8.domain.model.User;

public interface UserRepositoryPort {
  User save(User user);

  Optional<User> findByUsername(String username);
}
