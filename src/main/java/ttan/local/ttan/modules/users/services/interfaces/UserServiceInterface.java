package ttan.local.ttan.modules.users.services.interfaces;

import ttan.local.ttan.modules.users.requests.LoginRequest;
import ttan.local.ttan.modules.users.resources.LoginResource;

public interface UserServiceInterface {
    // Định nghĩa các method mà UserService phải implement
    LoginResource authenticate(LoginRequest request);
}
