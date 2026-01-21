package ttan.local.ttan.resources;

public class SuccessResource<T> 
{
    private String message;
    private T data;

    public SuccessResource( String message, T data )
    {
        this.message = message;
        this.data = data;
    }

    public String getMessage()  
    {
        return this.message;
    }

    public T getData()
    {
        return this.data;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    public void setData( T data )
    {
        this.data = data;
    }
}