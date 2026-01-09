package ttan.local.ttan.modules.users.dtos;

public class UserDTO 
{
    private final Long id;
    private final String email;

    // final là immutable và phải assign trong constructor => không có setter
    public UserDTO( Long id, String email )
    {
        this.id = id;
        this.email = email;
    }
    public Long getId()
    {
        return this.id;
    }
    public String getEmail()
    {
        return this.email;  
    }
    
}
