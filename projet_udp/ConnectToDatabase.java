package udp_projet;

import udp_test.User;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

public class ConnectToDatabase {
    public static User user = null;
    static final String DB_URL = "jdbc:mysql://localhost/socket?serverTimezone=UTC";
    static final String USERNAME = "root";
    static final String PASSWORD = "";
    static Connection connection;
    static Statement statement;
    static PreparedStatement preparedStatement;
    static ResultSet resultSet;
    public boolean userIsExist;


    public static User getAuthenticatedUser(String username, String password) {
        try {

            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            String sql = "SELECT * FROM users WHERE username=? AND  isConnected=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, "NO");

//            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.username = resultSet.getString("username");
                user.password = resultSet.getString("password");
                user.isConnected = resultSet.getString("isConnected");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    public ResultSet getAllUsers() {
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            String sql = "select * from users";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;

    }
    public User addUserToDataBase(String username, String password) throws SQLException {
        userIsExist = false;
        User user = null;
        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            ConnectToDatabase connectToDatabase = new ConnectToDatabase();
            ResultSet resultSet = connectToDatabase.getAllUsers();
            while (resultSet.next()) {
                if (resultSet.getString("username").equals(username)) {
                    userIsExist = true;
                    System.out.println(resultSet.getString("username"));
                }
            }
            if (!userIsExist) {
                System.out.println("we will add user " + username);
                String sql = "INSERT INTO users (username, password) " + "VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                int addedRows = preparedStatement.executeUpdate();
                if (addedRows > 0) {
                    //Insert row into the table
                    user = new User();
                    user.username = username;
                    user.password = password;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }
    public ArrayList getUsersConnected() {
        ArrayList<String> usersConnected = new ArrayList<String>();

        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            String sql = "select * from users where isConnected=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "YES");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                usersConnected.add(resultSet.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersConnected;
    }




    public ArrayList getUsersFriends(String username) {
        ArrayList<String> usersFriends = new ArrayList<String>();

        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            String sql = "(SELECT username_friend from friends WHERE username=?) union (SELECT username from friends where username_friend=?)";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                usersFriends.add(resultSet.getString("username_friend"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersFriends;
    }


    public ArrayList getListeUsersNotFriend(String username){
        ArrayList usersNotFriend = new ArrayList();
        ArrayList userFriend = getUsersFriends(username);
        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            String sql = "select * from users where username!=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String res = resultSet.getString("username");
                if(!userFriend.contains(res)){
                        usersNotFriend.add(res);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersNotFriend;
    }


    public void addNewFriend(String username,String username_friend){
        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            String sql = "INSERT INTO friends (username, username_friend) VALUES (?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username_friend);
            int addedRows = preparedStatement.executeUpdate();
            System.out.println(addedRows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void deleteFriend(String name,String friend) {
        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME,PASSWORD);
            String sql = "DELETE FROM friends WHERE username =? and username_friend=? " ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,friend);
            preparedStatement.executeUpdate();

            String sql1 = "DELETE FROM friends WHERE username_friend =? and username=? " ;
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement1.setString(1,name);
            preparedStatement1.setString(2,friend);
            preparedStatement1.executeUpdate();
        } catch(Exception e) {
            System.out.println(e);
        }
    }








    public String userIsConnected(String username) {
        String isconnected = null;
        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            String sql = "SELECT isConnected FROM users WHERE username=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                isconnected = resultSet.getString("isConnected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isconnected;
    }

    public void setUserConnected(String username) {
        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            String sql = "UPDATE users  SET isConnected=?  WHERE username=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "YES");
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setUserNotConnected(String username) {
        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            String sql = "UPDATE users  SET isConnected=?  WHERE username=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "NO");
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setAllUseerDisconnected() {
        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            String sql = "UPDATE users  SET isConnected=? ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "NO");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    public static void main(String[] args) throws SQLException {

        ConnectToDatabase connectToDatabase = new ConnectToDatabase();
        connectToDatabase.deleteFriend("te","yassine");

//        User user = connectToDatabase.getAuthenticatedUser("tes","123");
//        if(user!=null){
//            System.out.println("teh user: " + user.username);
//        }else{
//            System.out.println("error client");
//        }
//        String username="yssine";
//        String password = "123";
//        int  addRows = connectToDatabase.addUserToDataBase(username,password);
//        System.out.println("addRows : " + addRows);
//        if(addRows>0){
//            user = new User();
//            user.username = username;
//            user.password = password;
//            System.out.println("the user name " + username + " added");
//        }else{
//            System.out.println("the user already exist");
//        }

//        ResultSet resultSet = connectToDatabase.getAllUsers();
//        while (resultSet.next()){
//                System.out.println(resultSet.getString("username"));
//            }

//        User user = connectToDatabase.addUserToDataBase("aaaaaaaaaaa","123");
//        System.out.println(user.password);
//        connectToDatabase.getAuthenticatedUser("2","1");
//        System.out.println("userIsconnected : " );
//        if(user!=null){
//            System.out.println(user.isConnected);
//        }
//        connectToDatabase.getUsersConnected();


//        ArrayList users = connectToDatabase.getUsersConnected();
//        for (Object i : users) {
//            System.out.println(i);
//        }

//        ArrayList usersConnected = connectToDatabase.getListeUsersNotFriend("yassine");
//        for (Object i : usersConnected) {
//            System.out.println(i);
//        }


    }
}

