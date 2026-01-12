package ttan.local.ttan.modules.users.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ttan.local.ttan.services.BaseService;
import ttan.local.ttan.modules.users.requests.LoginRequest;
import ttan.local.ttan.modules.users.resources.LoginResource;
import ttan.local.ttan.modules.users.resources.UserResource;
import ttan.local.ttan.modules.users.repositories.UserRepository;
import ttan.local.ttan.modules.users.entities.User;
import ttan.local.ttan.services.JwtService;
import ttan.local.ttan.modules.users.services.interfaces.UserServiceInterface;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

@Service
public class UserService extends BaseService implements UserServiceInterface {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public LoginResource authenticate(LoginRequest request) {
        String email = request.getEmail();

        // Tìm user theo email trong database
        User user = userRepository.findByEmail(email);

        // Kiểm tra user có tồn tại không
        if (user == null) {
            logger.warn("Login failed: User not found with email: {}", email);
            throw new BadCredentialsException("Email hoặc mật khẩu không đúng");
        }

        // Kiểm tra password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Login failed: Invalid password for email: {}", email);
            throw new BadCredentialsException("Email hoặc mật khẩu không đúng");
        }

        // Tạo token và trả về
        UserResource userResource = new UserResource(user.getId(), user.getEmail());
        String token = jwtService.generateToken(user.getId(), user.getEmail());

        logger.info("Login successful for email: {}", email);
        return new LoginResource(token, userResource);
    }
}
