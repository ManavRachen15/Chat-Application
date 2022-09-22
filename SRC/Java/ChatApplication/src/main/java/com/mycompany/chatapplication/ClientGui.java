/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClientGui implements Initializable {

     
    public TextField message;
    public  MenuButton selectClients;
    public ListView listItems2;
    public CheckBox sendAll;
    public Label displayClientNum;
    public ListView onlineClientListView;
    ObservableList<Integer> onlineClients = FXCollections.observableArrayList();;

    
    Client ClientCon;
    List<String> sendToClients  ;
    public ServerGui serverScene;
    public  ArrayList<Integer> clientArrs;
    public ObservableList<Integer> items = FXCollections.observableArrayList();
    public ClientGui()
    {
        clientArrs = new ArrayList<>();
    }
    
    
    public void sendMessage(ActionEvent actionEvent) {

        Data c = new Data();
		c.message = message.getText();
		c.clientlist = sendToClients; 


		if(c.clientlist != null ) {
            for (int i = 0; i < c.clientlist.size(); ++i) {
                c.clientNum.add(Integer.parseInt(c.clientlist.get(i)));
            }
        }
		if(sendAll.isSelected())
            c.sendAll = true;
		ClientCon.send(c);
		message.clear();

    }
    
    
    
    public void setClientConnection( Client connect){
        ClientCon=connect;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

