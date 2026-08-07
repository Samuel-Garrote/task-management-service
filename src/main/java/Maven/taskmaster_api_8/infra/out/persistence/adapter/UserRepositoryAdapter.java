package Maven.taskmaster_api_8.infra.out.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import Maven.taskmaster_api_8.app.port.out.UserRepositoryPort;
import Maven.taskmaster_api_8.domain.model.User;
import Maven.taskmaster_api_8.infra.out.persistence.entity.UserEntity;
import Maven.taskmaster_api_8.infra.out.persistence.entityMapper.UserEntityMapper;
import Maven.taskmaster_api_8.infra.out.persistence.repo.UserRepository;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
  private UserRepository userRepository;
  private UserEntityMapper userEntityMapper;

  UserRepositoryAdapter(UserRepository userRepository, UserEntityMapper userEntityMapper) {
    this.userRepository = userRepository;
    this.userEntityMapper = userEntityMapper;
  }

  @Override
  public User save(User user) {
    UserEntity entity = userEntityMapper.toEntity(user);
    UserEntity savedEntity = userRepository.save(entity);
    return userEntityMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username).map(entity -> userEntityMapper.toDomain(entity));
  }
}
