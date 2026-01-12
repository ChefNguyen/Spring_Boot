package ttan.local.ttan.modules.users.controllers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import ttan.local.ttan.modules.users.requests.LoginRequest;
import ttan.local.ttan.modules.users.resources.LoginResource;
import ttan.local.ttan.modules.users.services.interfaces.UserServiceInterface;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController 
{
    private final UserServiceInterface userService;

    public AuthController( UserServiceInterface userService )
    {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResource> authenticate( @Valid @RequestBody LoginRequest request )
    {
        LoginResource auth = userService.authenticate(request);
        return ResponseEntity.ok(auth);
    }
}
