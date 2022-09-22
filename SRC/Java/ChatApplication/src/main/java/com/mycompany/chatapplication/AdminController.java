/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatapplication;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



public class AdminController implements Initializable {

        Server ServerCon;
	Client ClientCon;
	ServerGui serverControl;

	ArrayList<Integer> clientArr;
	static Stage mainStage = new Stage();

	public AdminController() throws IOException {
	}


        
        
        
	public void ServerButton (ActionEvent actionEvent) throws IOException {
            

		FXMLLoader newRoot = new FXMLLoader(getClass().getResource("Server.fxml"));

		Parent root = newRoot.load();
		serverControl =newRoot.getController();

		ServerCon = new Server(data->{
			Platform.runLater(()->{
				serverControl.listItems.getItems().add(data.toString());

			});
		}, data2 -> {

			});
		Scene scene = new Scene(root);

		mainStage.setScene(scene);
		mainStage.show();
	}


	public void ClientButton(ActionEvent actionEvent) throws IOException {
		FXMLLoader newRoot = new FXMLLoader(getClass().getResource("Client.fxml"));

		Parent root = newRoot.load();
		ClientGui clientControl =newRoot.getController();


		ClientCon = new Client(data->{
			Platform.runLater(()->{
				String sanitized = data.toString();
				if(sanitized.contains("Online:")) {
					sanitized = sanitized.substring(0, sanitized.indexOf("Online:"));
				}

				clientControl.listItems2.getItems().add(sanitized);

				final String substring = data.toString().substring(0,1);
				final String substring2 =Character.toString(data.toString().charAt(1));
				System.out.println(substring2);
				clientControl.onlineClientListView.getItems().clear();
				String online = data.toString();
	   			online = online.substring(online.indexOf("[")+1);
	   			online = online.substring(0,online.indexOf("]"));
				clientControl.onlineClientListView.getItems().add(online);
				clientControl.selectClients.getItems().clear();
				online = online.replaceAll(",","");
				for(int i = 0 ;i < online.length() ; ++i)
				{
					CheckMenuItem menuItem = new CheckMenuItem(Character.toString(online.charAt(i)));
					clientControl.selectClients.getItems().add(menuItem);
				}
				clientControl.selectClients.getItems().stream().forEach((MenuItem menuItem) -> menuItem.setOnAction(e -> {
					clientControl.sendToClients = clientControl.selectClients.getItems().stream()
							.filter(item -> CheckMenuItem.class.isInstance(item) && CheckMenuItem.class.cast(item).isSelected())
							.map(MenuItem::getText)
							.collect(Collectors.toList());
				}));

			});
		},data2->{
			Platform.runLater(()->{

			});

		});
		ClientCon.start();
		clientControl.setClientConnection(ClientCon);
		Scene newScene = new Scene(root);
		mainStage.setScene(newScene);
		mainStage.show();
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		clientArr = new ArrayList<>();
	}
}

