package Maven.taskmaster_api_8.infra.in.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Maven.taskmaster_api_8.app.service.authService.AuthService;
import Maven.taskmaster_api_8.infra.in.web.dto.LoginRequest;
import Maven.taskmaster_api_8.infra.in.web.dto.LoginResponse;
import Maven.taskmaster_api_8.infra.in.web.dto.RegisterRequest;
import Maven.taskmaster_api_8.infra.in.web.dto.RegisterResponse;

@RestController
@RequestMapping("/auth")
public class UserController {

  private final AuthService authService;

  UserController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }
}
