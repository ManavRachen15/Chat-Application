/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatapplication;

import static com.mycompany.chatapplication.Selection.mainStage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;
//import java.sql.*;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class LoginController  {
    private static Scene scene;
    
    @FXML
    private TextField emailIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;
    
    String filepath = "Login.txt";
    
   
     @FXML
    public void onCick_btn_Login(ActionEvent b) throws IOException{
        Window owner = submitButton.getScene().getWindow();
        
        try{ 
          Class.forName("com.mysql.cj.jdbc.Driver");
          Connection con = (Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/DatabaseChatServer", "admin", "4VFggpYQSuh5");
          String emailId = emailIdField.getText();
          String password = passwordField.getText();
          
          Statement stm = con.createStatement();
          String sql = "select * from users where username='"+emailId+"' and password='"+password+"'";
          ResultSet rs = stm.executeQuery(sql);
          
          if (rs.next()){
              
              FXMLLoader newRoot = new FXMLLoader(getClass().getResource("Selection.fxml"));
              Parent root = newRoot.load();
              Scene scene = new Scene(root);
              mainStage.setScene(scene);
              mainStage.show();
              
          }else if (emailId.contains("Admin") && password.contains("Admin")){
              FXMLLoader newRoot = new FXMLLoader(getClass().getResource("AdminSelection.fxml"));
              Parent root = newRoot.load();
              Scene scene2 = new Scene(root);
              mainStage.setScene(scene2);
              mainStage.show();
              //Selection sele = new Selection();
              //sele.show();
          
          }else{
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!", 
                "Please enter your email id");
           return; 
          }
          
        }catch (Exception e){
            System.out.println("not working");
        }
    }
    @FXML
    private void SwitchToRegister() throws IOException {
        App.setRoot("Register");
    }
    
    
    
    private static Parent loadFXML(String fxml) throws IOException {
       FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
       return fxmlLoader.load();
   }
     
   static void setRoot(String fxml) throws IOException {
       scene.setRoot(loadFXML(fxml));
    }
  
   private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

}
