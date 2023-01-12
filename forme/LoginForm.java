package udp_test;

import udp_projet.ConnectToDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JDialog{
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JButton btnOK;
    private JButton btnCancel;
    private JPanel loginPanel;
    private JButton btnRegister;

    public User user;

    public LoginForm(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(500, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = tfUsername.getText();
                String password = String.valueOf(pfPassword.getPassword());

                ConnectToDatabase connectToDatabase = new ConnectToDatabase();
                user = ConnectToDatabase.getAuthenticatedUser(username,password);

//                user = getAuthenticatedUser(username, password);


                if (user != null) {
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or Password Invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //registration of client of database
                RegistrationForm myForm = new RegistrationForm(null);
                User user = myForm.user;
                if (user != null) {
                    System.out.println("Successful registration of: " + user.username);
                }
                else {
                    System.out.println("Registration canceled");
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }



    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;
        if (user != null){
            System.out.println("Successful Authentication of:" +user.username);
            System.out.println("passowrd : " + user.password);
            System.out.println("connected : " + user.isConnected);
            System.out.println("passowrd : " +user.password);
        }else{
            System.out.println("authentication faild");
        }

    }
}
