package ttan.local.ttan.modules.users.services.interfaces;

import ttan.local.ttan.modules.users.dtos.LoginRequest;
import ttan.local.ttan.modules.users.dtos.LoginResponse;

public interface UserServiceInterface {
    // Định nghĩa các method mà UserService phải implement
    LoginResponse login(LoginRequest request);
}
