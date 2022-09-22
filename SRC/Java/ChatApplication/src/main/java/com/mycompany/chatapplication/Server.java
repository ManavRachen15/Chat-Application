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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;


public class Server{

	int count = 1;
	public  ArrayList<ClientThread> clients = new ArrayList<>();
	public  ArrayList<Integer> onlineClients = new ArrayList<>();
	TheServer server;
	private Consumer<Serializable> callback;
	private Consumer<ArrayList<ClientThread>> callback2;
        
	Server(Consumer<Serializable> call,Consumer<ArrayList<ClientThread>> call2) {
		synchronized (this) {
			clients = new ArrayList<>();
			callback = call;
			callback2 = call2;
			server = new TheServer();
			server.start();
		}
	}
        
	public class clientMessage{
		String message;
		ArrayList<ClientThread> client;
	}
	
        
	public class TheServer extends Thread{
		public void run() {
			try(ServerSocket mysocket = new ServerSocket(2468);){
		    System.out.println("Server is waiting");
		    while(true) {
				ClientThread c = new ClientThread(mysocket.accept(), count);
				clients.add(c);
				onlineClients.clear();
				for(int i = 0 ; i < clients.size() ; ++i)
				{
					onlineClients.add(clients.get(i).count);
				}
				callback.accept(count + " connected to server: " + "online: " + onlineClients.toString() );
				c.start();
				count++;
			    }
			}
				catch(Exception e) {
					callback.accept("Server socket error");
				}
			}
		}

		 class  ClientThread extends Thread{
			Socket connection;
			int count;
			ObjectInputStream inStream;
			ObjectOutputStream outstream;
			clientMessage wo=new clientMessage();
			ArrayList<clientMessage> woi=new ArrayList<>();
                        
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}
                        
                      
			public void updateClients(String message,ArrayList<ClientThread> clients, int left) {

				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
						if(left ==1 )
						{
							t.outstream.writeObject(count + " left! Online: "+ onlineClients.toString());
						}
						else if(left == 2)
						{
							t.outstream.writeObject(message + " Online: " + onlineClients.toString());
						}
						else {
							t.outstream.writeObject(count + " said: " + message + " Online: " + onlineClients.toString());
						}

					}
					catch(Exception e) {}
				}

			}
                       
			public void updateClients2(String message,Data receive, int sender) throws IOException {

				int leftIdx = 0;
				int rightIdx = 0 ;
				int i =0;
				while(true)
				{
					if(clients.get(i).count == sender) {
						clients.get(i).outstream.writeObject(sender + " said: " + message + " Online: " + onlineClients.toString());
						break;
					}
					i++;
				}
				while(leftIdx < receive.clientNum.size() )
				{
					if (clients.get(rightIdx).count != receive.clientNum.get(leftIdx))
					{
						rightIdx++;
					}
					if (clients.get(rightIdx).count == receive.clientNum.get(leftIdx))
					{
						ClientThread t = clients.get(rightIdx);
						t.outstream.writeObject ( sender + " said: " + message +" Online: "+ onlineClients.toString() );
						leftIdx++;
					}
				}

			}

                        /**
                         * This section writes to the server if the client joins or leaves the application
                         */
			public void run() {
				synchronized (this) {
					try {
						inStream = new ObjectInputStream(connection.getInputStream());
						outstream = new ObjectOutputStream(connection.getOutputStream());
						connection.setTcpNoDelay(true);
					} catch (Exception e) {
						System.out.println("Streams not open");
					}
					System.out.println("new client on server: client #" + count);
					updateClients(count + " on server",clients,2);

					while (true) {
						try {
                                                    
                                                        Data receive = (Data) inStream.readObject();
								callback.accept("client: " + count + " sent: " + receive.message);
								if(!receive.sendAll) {
									updateClients2( receive.message, receive,count);
								}
								if(receive.sendAll)
								{
									updateClients( receive.message,clients,0);
								}
//

						} catch (Exception e) {
                                                        
							callback.accept("client left: " + count + "....closing down!");

							clients.remove(this);
							for(int i = 0 ; i < onlineClients.size() ; ++i)
							{
								if(onlineClients.get(i) == count){
									onlineClients.remove(i);
								}

							}
							updateClients( count + "left the server" ,clients,1);
							break;
						}
					}
				}
			}
			public void send(ArrayList<ClientThread> data){
				try{ outstream.writeObject(data);}
				catch(IOException e) {e.printStackTrace();}
			}
		}


}


	
	
