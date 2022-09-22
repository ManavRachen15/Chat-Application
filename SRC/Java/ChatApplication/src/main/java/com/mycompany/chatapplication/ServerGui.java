/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatapplication;

import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ServerGui implements Initializable {

 
    public ListView listItems;
    public Server serverConnection;
    ArrayList<Integer> clientArr ;

    
    public ServerGui() {
        clientArr = new ArrayList<>();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}