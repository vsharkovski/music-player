package mk.legendi;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -6470090944414208496L;
    public static int accounts = 0;
    private final String name;
    private final String username;
    private final String password;
    private final String email;
    private final int id;

    public User(String name, String username, String password, String email) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        accounts++;
        this.id = accounts;
    }

    public String getName() {
        return name;
    }

    public static int getAccounts() {
        return accounts;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

