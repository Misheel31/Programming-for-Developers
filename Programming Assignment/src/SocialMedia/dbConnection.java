package SocialMedia;
import javax.swing.*;
import java.sql.*;
 
public class dbConnection {
 
    public Connection connection;
 
    Statement statement;
 
    ResultSet resultSet;
 
    int value;
 
 
 
    public dbConnection(){
 
        try {
 
            String username = "root";
 
            String password = "misheel123";
 
            Class.forName("com.mysql.cj.jdbc.Driver");
 
            connection = DriverManager.getConnection(
 
                    "jdbc:mysql://localhost:3306/socialMedia",username,password);
 

            if(connection!=null){
 
                System.out.println("Connected to database");
 
            }else{
 
                System.out.println("Error connecting to database");
 
            }
 
            statement = connection.createStatement();
 
        }catch (Exception e){
 
            e.printStackTrace();
 
        }
 
    }
 
    public int manipulate(String query){
 
        try {
 
            value = statement.executeUpdate(query);
 
            connection.close();
 
        }catch (SQLIntegrityConstraintViolationException ex){
 
            JOptionPane.showMessageDialog(null, "These details already exist!");
 
        }catch (SQLException e){
 
            e.printStackTrace();
 
        }
 
        return value;
 
    }
 
 
 
    public ResultSet retrieve(String query){
 
        try {
 
            resultSet = statement.executeQuery(query);
 
        }catch (SQLException e){
 
            e.printStackTrace();
 
        }
 
        return resultSet;
 
    }
 
 
 
    public static void main(String[] args) {
 
        new dbConnection();
 
    }
 
}
 