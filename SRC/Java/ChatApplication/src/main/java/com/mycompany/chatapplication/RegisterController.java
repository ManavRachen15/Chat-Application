/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatapplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.Connection;


/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class RegisterController {

   @FXML
    private TextField emailIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;
    

    
    
   
     @FXML
    public void onCick_btn_Login() throws IOException {
        
       String emailId = emailIdField.getText();
       String password = passwordField.getText();
       Window owner = submitButton.getScene().getWindow();
       StringBuilder sb = new StringBuilder();
        sb.append(emailIdField.getText().toString()+ "\n");
        sb.append(passwordField.getText().toString()+ "\n");
        
        
        File file = new File("//home//ntu-user//eclipse-workspace//Login.txt");
        FileWriter W = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(W);
        
        if(emailId.isEmpty() || password.isEmpty()) {
        
        showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
        "Please enter your email id or password");
        return;
        }
        //bw.write(sb.toString());
        //bw.close();
        
        
        
            
                 
                 
                  
              
           
            
        
    }
    
    
    public static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
        
    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
    @FXML
    private void SwitchToLogin() throws IOException {
        App.setRoot("Login");
    }
    
}
