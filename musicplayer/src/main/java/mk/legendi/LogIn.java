package mk.legendi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;

public class LogIn extends JFrame {
    public JFrame frmLogin;
    private final JTextField username;
    private final JTextField password;
    private final JLabel failLabel;

    public int verify_login() {
        String username = this.username.getText();
        String password = this.password.getText();

        System.out.println(username);
        System.out.println(password);

        for (int i = 0; i < UserList.getInstance().getAccounts().size(); i++) {
            if (UserList.getInstance().getAccounts().get(i).getUsername().equals(username) && UserList.getInstance().getAccounts().get(i).getPassword().equals(password)) {
                return i;
            }
        }

        failLabel.setText("Invalid username or password! Please try again.");
        return -1;
    }

    public LogIn() {
        frmLogin = new JFrame();
        frmLogin.setBounds(100, 100, 450, 300);
        frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmLogin.getContentPane().setLayout(null);
        frmLogin.setTitle("Login Window");

        JLabel lblNewLabel = new JLabel("Username");
        lblNewLabel.setBounds(29, 44, 95, 14);
        frmLogin.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Password");
        lblNewLabel_1.setBounds(29, 106, 76, 14);
        frmLogin.getContentPane().add(lblNewLabel_1);

        username = new JTextField();
        username.setBounds(115, 41, 217, 20);
        frmLogin.getContentPane().add(username);
        username.setColumns(10);

        password = new JTextField();
        password.setBounds(115, 103, 217, 20);
        frmLogin.getContentPane().add(password);
        password.setColumns(10);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int ver = verify_login();
                if (ver != -1) {
                    String pth = "database_" + (ver + 1) + ".db";
                    Path databasePath = Path.of(pth);
                    Database database = new Database();
                    database.loadStore(databasePath);
                    Library library = new Library(database, UserList.getInstance().getAccounts().get(ver).getUsername());
                    AudioManager audioManager = new AudioManager();

                    MusicPlayer musicPlayer = new MusicPlayer(library, audioManager);
                    musicPlayer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    musicPlayer.setResizable(false);
                    musicPlayer.pack();
                    musicPlayer.setVisible(true);

                    musicPlayer.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent windowEvent) {
                            database.writeStore(databasePath);
                            audioManager.pause();
                            musicPlayer.dispose();
                        }
                    });
                    musicPlayer.settingsPanel.logoutButton.addActionListener(ee -> {
                        database.writeStore(databasePath);
                        audioManager.pause();
                        musicPlayer.dispose();
                        LogIn logIn = new LogIn();
                        logIn.frmLogin.setVisible(true);
                    });
                    frmLogin.dispose();
                } else {
                    failLabel.setText("Invalid username or password! Please try again.");
                }
            }
        });
        frmLogin.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                UserList.getInstance().writeUsers();
                frmLogin.dispose();
                System.exit(0);
            }
        });
        btnLogin.setBounds(89, 187, 89, 23);
        frmLogin.getContentPane().add(btnLogin);

        JButton btnSignup = new JButton("Sign Up");
        btnSignup.addActionListener(e -> {
            SignUPGUI SignUp = new SignUPGUI();
            SignUp.frame.setVisible(true);
            frmLogin.dispose();
        });
        btnSignup.setBounds(273, 187, 89, 23);
        frmLogin.getContentPane().add(btnSignup);

        failLabel = new JLabel("");
        failLabel.setBounds(30, 230, 390, 14);
        frmLogin.getContentPane().add(failLabel);
    }

}
