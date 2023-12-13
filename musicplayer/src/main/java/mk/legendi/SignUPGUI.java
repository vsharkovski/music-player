package mk.legendi;

import javax.swing.*;
import java.awt.event.*;

public class SignUPGUI extends JFrame {
    public JFrame frame;
    private final JTextField name;
    private final JTextField email;
    private final JTextField user;
    private final JTextField password;
    private final JLabel failLabel;

    public SignUPGUI() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 380);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener Exit = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                LogIn login = new LogIn();
                login.frmLogin.setVisible(true);
            }
        };
        frame.addWindowListener(Exit);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Full Name:");
        lblNewLabel.setBounds(56, 53, 78, 14);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(56, 90, 78, 14);
        frame.getContentPane().add(lblEmail);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(56, 133, 78, 14);
        frame.getContentPane().add(lblUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(56, 175, 78, 14);
        frame.getContentPane().add(lblPassword);

        name = new JTextField();
        name.setBounds(177, 50, 186, 20);
        frame.getContentPane().add(name);
        name.setColumns(10);

        email = new JTextField();
        email.setColumns(10);
        email.setBounds(177, 87, 186, 20);
        frame.getContentPane().add(email);

        user = new JTextField();
        user.setColumns(10);
        user.setBounds(177, 130, 186, 20);
        frame.getContentPane().add(user);

        password = new JTextField();
        password.setColumns(10);
        password.setBounds(177, 172, 186, 20);
        frame.getContentPane().add(password);

        JButton btnNewButton = new JButton("Create Account");
        btnNewButton.addActionListener(e -> {
            boolean s = signup();
            if (s) {
                frame.dispose();
                LogIn login = new LogIn();
                login.frmLogin.setVisible(true);
            }
        });
        btnNewButton.setBounds(234, 229, 130, 23);
        frame.getContentPane().add(btnNewButton);

        failLabel = new JLabel("");
        failLabel.setBounds(30, 280, 390, 14);
        frame.getContentPane().add(failLabel);
    }

    public boolean signup() {
        String name = this.name.getText();
        String username = user.getText();
        String email = this.email.getText();
        String password = this.password.getText();

        if (name.isBlank() || username.isBlank() || email.isBlank() || password.isBlank()) {
            failLabel.setText("Please enter all required information.");
            return false;
        }

        User newUser = new User(name, username, password, email);
        int present = 0;

        for (int i = 0; i < UserList.getInstance().getAccounts().size(); i++) {
            if (UserList.getInstance().getAccounts().get(i).getUsername().equals(username)) {
                present = 1;
                break;
            }
        }
        if (present == 0) {
            UserList.getInstance().getAccounts().add(newUser);
            return true;
        } else {
            failLabel.setText("User name already in use! Please Try again!");
            return false;
        }

    }
}
