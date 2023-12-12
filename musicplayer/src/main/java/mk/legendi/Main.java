package mk.legendi;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of("accounts_list.db");
        boolean loaded = UserList.getInstance().loadUsers(path);
        LogIn login = new LogIn();
        login.frmLogin.setVisible(true);
    }
}
