package mk.legendi;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path usersPath = Path.of("accounts_list.db");
        UserList.getInstance().loadUsers(usersPath);

        LogIn login = new LogIn();
        login.frmLogin.setVisible(true);
    }
}
