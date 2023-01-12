package udp_test;

import udp_projet.ConnectToDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrationForm extends JDialog{
    private JTextField tfUsername;
    private JPasswordField pfPasswrod;
    private JPasswordField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;

    public RegistrationForm(JFrame parent){
        super(parent);
        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(500, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
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
    private void registerUser() {
        String username = tfUsername.getText();
        String password = String.valueOf(pfPasswrod.getPassword());
        String confirmPassword = String.valueOf(pfConfirmPassword.getPassword());

        if (username.isEmpty()  || password.isEmpty() ||confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(RegistrationForm.this,
                    "Please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword) ) {
            JOptionPane.showMessageDialog(RegistrationForm.this,
                    "Confirm Password does not match",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        ConnectToDatabase connectToDatabase = new ConnectToDatabase();
        try {
            user = connectToDatabase.addUserToDataBase(username, password);
            System.out.println("user is exist : " + connectToDatabase.userIsExist);
            if(connectToDatabase.userIsExist){
                JOptionPane.showMessageDialog(this,
                        "the user alrady exist ",
                        "Try again",
                        JOptionPane.ERROR_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(this,
                        "the user  " + user.username + "had registred",
                        "Try again",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            if (user != null) {
                dispose();
            } else if (!connectToDatabase.userIsExist) {
                JOptionPane.showMessageDialog(this,
                        "Failed to register new user",
                        "Try again",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
        e.printStackTrace();
        }
    }
    public User user;


    public static void main(String[] args) {
        RegistrationForm myForm = new RegistrationForm(null);
        User user = myForm.user;
        if (user != null) {
            System.out.println("Successful registration of: " + user.username);
        }
        else {
            System.out.println("Registration canceled");
        }

    }
}
