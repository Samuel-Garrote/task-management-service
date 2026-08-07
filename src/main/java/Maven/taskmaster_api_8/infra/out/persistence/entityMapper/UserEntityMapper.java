package Maven.taskmaster_api_8.infra.out.persistence.entityMapper;

import org.springframework.stereotype.Component;

import Maven.taskmaster_api_8.domain.model.User;
import Maven.taskmaster_api_8.infra.out.persistence.entity.UserEntity;

@Component
public class UserEntityMapper {
  public UserEntity toEntity(User user) {
    UserEntity entity = new UserEntity(user.getId(), user.getUsername(), user.getPassword());
    return entity;
  }

  public User toDomain(UserEntity entity) {
    User user = new User(entity.getId(), entity.getUsername(), entity.getPassword());
    return user;
  }
}
