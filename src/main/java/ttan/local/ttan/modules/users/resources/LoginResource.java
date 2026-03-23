package ttan.local.ttan.modules.users.resources;
import ttan.local.ttan.modules.users.resources.UserResource;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResource {
    private String token;
    private UserResource user;
}
