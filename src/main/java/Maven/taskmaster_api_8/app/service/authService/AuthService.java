package Maven.taskmaster_api_8.app.service.authService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Maven.taskmaster_api_8.app.port.out.UserRepositoryPort;
import Maven.taskmaster_api_8.domain.model.User;
import Maven.taskmaster_api_8.infra.in.web.dto.LoginRequest;
import Maven.taskmaster_api_8.infra.in.web.dto.LoginResponse;
import Maven.taskmaster_api_8.infra.in.web.dto.RegisterRequest;
import Maven.taskmaster_api_8.infra.in.web.dto.RegisterResponse;
import Maven.taskmaster_api_8.infra.security.JwtService;

@Service
public class AuthService {
  private final UserRepositoryPort userRepositoryPort;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  AuthService(UserRepositoryPort userRepositoryPort, JwtService jwtService, PasswordEncoder passwordEncoder) {
    this.userRepositoryPort = userRepositoryPort;
    this.jwtService = jwtService;
    this.passwordEncoder = passwordEncoder;
  }

  public RegisterResponse register(RegisterRequest request) {
    if (userRepositoryPort.findByUsername(request.username()).isPresent()) {
      throw new IllegalArgumentException("User already in");
    }

    String hashedPassword = passwordEncoder.encode(request.password());
    User username = new User(request.username(), hashedPassword);
    User savedUser = userRepositoryPort.save(username);

    return new RegisterResponse(savedUser.getId(), savedUser.getUsername());
  }

  public LoginResponse login(LoginRequest request) {
    User user = userRepositoryPort.findByUsername(request.username())
        .orElseThrow(() -> new IllegalArgumentException("User doesnt exist"));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new IllegalArgumentException("Password doesnt match");
    }
    String token = jwtService.generateToken(user.getUsername());
    return new LoginResponse(token);
  }

}
