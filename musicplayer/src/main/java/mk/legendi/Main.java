package mk.legendi;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of("accounts_list.db");
        boolean loaded = UserList.getInstance().loadUsers(path);
        LogIn login = new LogIn();
        login.frmLogin.setVisible(true);
        /*
        Database database = new Database();
        database.loadStore(databasePath);

        Library library = new Library(database);
        AudioManager audioManager = new AudioManager();

        MusicPlayer musicPlayer = new MusicPlayer(library, audioManager);
        musicPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        musicPlayer.setResizable(false);
        musicPlayer.pack();
        musicPlayer.setVisible(true);

        musicPlayer.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                database.writeStore(databasePath);
            }
        });*/
    }
}
