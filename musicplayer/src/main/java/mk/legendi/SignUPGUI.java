package mk.legendi;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;

public class SignUPGUI extends JFrame{
    public JFrame frame;
    private JTextField Name;
    private JTextField Email;
    private JTextField User;
    private JTextField Password;

    public SignUPGUI()
    {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener Exit = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                LogIn login = new LogIn();
                login.frmLogin.setVisible(true);
                //super.windowClosing(e);
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

        Name = new JTextField();
        Name.setBounds(177, 50, 186, 20);
        frame.getContentPane().add(Name);
        Name.setColumns(10);

        Email = new JTextField();
        Email.setColumns(10);
        Email.setBounds(177, 87, 186, 20);
        frame.getContentPane().add(Email);

        User = new JTextField();
        User.setColumns(10);
        User.setBounds(177, 130, 186, 20);
        frame.getContentPane().add(User);

        Password = new JTextField();
        Password.setColumns(10);
        Password.setBounds(177, 172, 186, 20);
        frame.getContentPane().add(Password);

        JButton btnNewButton = new JButton("Create Account");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.setVisible(false);
                boolean s = signup();
                if(s==true)
                {
                    frame.dispose();
                    LogIn login = new LogIn();
                    login.frmLogin.setVisible(true);
                }
            }
        });
        btnNewButton.setBounds(234, 229, 130, 23);
        frame.getContentPane().add(btnNewButton);
    }

    public boolean signup()
    {
        String name = Name.getText();
        String username = User.getText();
        String email = Email.getText();
        String password = Password.getText();

        User newUser = new User(name,username,password,email);
        int present = 0;

        for(int i=0;i<UserList.getInstance().getAccounts().size();i++)
        {
            if(UserList.getInstance().getAccounts().get(i).getUsername().equals(username)) {
                present = 1;
                break;
            }
        }
        if(present == 0)
        {
            UserList.getInstance().getAccounts().add(newUser);
            return true;
            //System.out.println(UserList.getInstance().getAccounts().size());
            /*
            int in= UserList.getInstance().getAccounts().size();
            String pth="database"+ Integer.toString(in)+".db";
            Path databasePath = Path.of(pth);
            Database database = new Database();
            database.loadStore(databasePath);
            Library library = new Library(database,username);
            UserList.getInstance().getAccounts().get(in-1).setL(library);*/
        }else
        {
            System.out.println("User name already in use! Please Try again! \n");
            return false;
        }

    }
}
