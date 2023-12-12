package mk.legendi;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = -6470090944414208496L;
    public static int accounts = 0;
    private String Name;
    private String Username;
    private String Password;
    private String Email;
    private int ID;
    //private Library l;
    public User(String Name,String Username, String Password,String Email)
    {
        this.Name = Name;
        this.Username = Username;
        this.Password = Password;
        this.Email = Email;
        accounts++;
        this.ID = accounts;
    }

    public String getName()
    {
        return Name;
    }

    public static int getAccounts() {
        return accounts;
    }

    public String getEmail() {
        return Email;
    }

    public int getID() {
        return ID;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }
    /*
    public Library getL() {
        return l;
    }

    public void setL(Library l) {
        this.l = l;
    }*/
}

