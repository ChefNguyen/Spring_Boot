package ttan.local.ttan.modules.users.services.impl;
import org.springframework.stereotype.Service;
import ttan.local.ttan.services.BaseService;
import ttan.local.ttan.modules.users.dtos.LoginResponse;
import ttan.local.ttan.modules.users.dtos.LoginRequest;
import ttan.local.ttan.modules.users.dtos.UserDTO;
import ttan.local.ttan.modules.users.services.interfaces.UserServiceInterface;

@Service    
public class UserService extends BaseService implements UserServiceInterface
{
    @Override
    public LoginResponse login( LoginRequest request )
    {
        try
        {
            String token = "random_token";
            UserDTO user = new UserDTO( 7777L , "admin@gmail.com" );
            return new LoginResponse( token, user );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Có lỗi xảy ra" );
        }
    }
        
}
