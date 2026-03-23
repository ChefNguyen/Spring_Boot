package ttan.local.ttan.modules.users.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import ttan.local.ttan.modules.users.resources.UserResource;
import ttan.local.ttan.modules.users.entities.User;
import ttan.local.ttan.modules.users.repositories.UserRepository;
import ttan.local.ttan.resources.SuccessResource;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1")
public class UserController 
{
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> me(org.springframework.security.core.Authentication authentication) 
    {
        // Lấy thông tin user đăng nhập từ SecurityContext (được JwtAuthFilter đưa vào)
        org.springframework.security.core.userdetails.UserDetails userDetails = 
                (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
        
        String email = userDetails.getUsername(); // Lấy email từ token thực tế
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        UserResource userResource = UserResource.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .build();
        return ResponseEntity.ok( new SuccessResource<>("success", userResource ) );
    }
}
