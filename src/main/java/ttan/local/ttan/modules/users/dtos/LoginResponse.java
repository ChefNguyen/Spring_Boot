package ttan.local.ttan.modules.users.dtos;

public class LoginResponse 
{
    private final String token;
    private final UserDTO user;

    public LoginResponse( String token, UserDTO user )
    {
        this.token = token;
        this.user = user;
    }
    public String getToken()
    {
        return this.token;
    }
    public UserDTO getUser()
    {
        return this.user;   
    }
}
