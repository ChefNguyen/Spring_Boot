package ttan.local.ttan.modules.users.resources;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResource {
    private Long id;
    private String email;
    private String name;
    private String phone;
}
