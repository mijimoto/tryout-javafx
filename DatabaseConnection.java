/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication;
/**
 *
 * @author RandyMaximize
 */
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {

    
 
public Connection DatabaseConnection(){
        
 
     String url = "jdbc:sqlserver://localhost:1433;" +
     "databaseName=Project;integratedSecurity=true;" +
     "encrypt=true;trustServerCertificate=true";


        String user="sa";
        String password="123456";
     try{
            Connection connect = DriverManager.getConnection(url,user,password);
            return connect;
        }catch(Exception e){e.printStackTrace();
        
          return null;
    }
}

}

