package ttan.local.ttan.modules.users.resources;

public class UserResource {
    private final Long id;
    private final String email;
    private final String name;

    // final là immutable và phải assign trong constructor => không có setter
    public UserResource(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

}
