/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatapplication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.event.ActionEvent;



public class Client extends Thread{
	
	Socket Csocket;
	ObjectOutputStream outStream;
	ObjectInputStream inStream;
	Server ServerCon;
	ClientGui Scene;
	private Consumer<Serializable> callback;
	private Consumer<String> callback2;
	ArrayList<Server.ClientThread> clientArrs = new ArrayList<>();;
	Client(Consumer<Serializable> call, Consumer<String> call2){
		callback = call;
		callback2 = call2;
	}
       
	public void run() {
		
		try {
		Csocket= new Socket("127.0.0.1",2468);
                //Csocket.close();
	    outStream = new ObjectOutputStream(Csocket.getOutputStream());
	    inStream = new ObjectInputStream(Csocket.getInputStream());
	    Csocket.setTcpNoDelay(true);
		}
		catch(Exception e) {}

		while(true) {
			try {
			String message =  inStream.readObject().toString();
				callback.accept(message);
			}
			catch(Exception e) {
                        
                        }
		}
	
    }
        
	
	public void send(Data data) {
		
		try {
			outStream.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
