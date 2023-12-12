package mk.legendi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class LogIn extends JFrame {
    public JFrame frmLogin;
    private JTextField UserName;
    private JTextField Password;
    ArrayList<User> Accounts = new ArrayList<User>();

    public int verify_login()
    {
        String username = UserName.getText();
        String password = Password.getText();

        System.out.println(username);
        System.out.println(password);
        int present = 0;

        for(int i=0;i<UserList.getInstance().getAccounts().size();i++)
        {
            if(UserList.getInstance().getAccounts().get(i).getUsername().equals(username) && UserList.getInstance().getAccounts().get(i).getPassword().equals(password)) {
                return i;
            }
        }
        return -1;
    }

    public LogIn()
    {
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

        UserName = new JTextField();
        UserName.setBounds(115, 41, 217, 20);
        frmLogin.getContentPane().add(UserName);
        UserName.setColumns(10);

        Password = new JTextField();
        Password.setBounds(115, 103, 217, 20);
        frmLogin.getContentPane().add(Password);
        Password.setColumns(10);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int ver=verify_login();
                if(ver!=-1) {
                    //Path databasePath = Path.of("database.db");
                    //Database database = new Database();
                    //database.loadStore(databasePath);

                    //Library library = new Library(database);
                    String pth="database_"+ Integer.toString(ver+1)+".db";
                    Path databasePath = Path.of(pth);
                    Database database = new Database();
                    database.loadStore(databasePath);
                    Library library = new Library(database,UserList.getInstance().getAccounts().get(ver).getUsername());
                    //Library library = new Library(database,UserList.getInstance().getAccounts().get(ver).getUsername());
                    //Path databasePath=Path.of("database"+ Integer.toString(ver+1)+".db");
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
                }else
                {
                    System.out.println("Invalid Login! Please Try Again!\n");
                }
            }
        });
        frmLogin.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                UserList.getInstance().StoreUsers();
                frmLogin.dispose();
                System.exit(0);
            }
        });
        btnLogin.setBounds(89, 187, 89, 23);
        frmLogin.getContentPane().add(btnLogin);

        JButton btnSignup = new JButton("Sign Up");
        btnSignup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SignUPGUI SignUp = new SignUPGUI();
                SignUp.frame.setVisible(true);
                frmLogin.dispose();
            }
        });
        btnSignup.setBounds(273, 187, 89, 23);
        frmLogin.getContentPane().add(btnSignup);
    }

}
