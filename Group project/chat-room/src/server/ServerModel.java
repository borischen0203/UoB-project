package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import client.ChatWindow;
import common.Message;
import common.User;

public class ServerModel {
	List<User> userTable;

	public ServerModel() {
		try {
			System.out.println("Server Listening");
			UserDao a = new UserDao();
			// //reset
			a.updateOffline("02");
			a.updateOffline("01");
			a.updateOffline("03");
			a.updateOffline("04");
			a.updateOffline("05");
			// Listening at 50000
			ServerSocket ss = new ServerSocket(50000);
			// wait for accepting
			while (true) {
				Socket s = ss.accept();
				System.out.println("-----------connection built------------");

				// receive information from client
				ObjectInputStream ear = new ObjectInputStream(s.getInputStream());
				Message requestFromClient = (Message) ear.readObject();
				User loginUser = requestFromClient.getUser();
				System.out.println("server receive request from client ");

				// send information to client
				ObjectOutputStream mouth = new ObjectOutputStream(s.getOutputStream());
				Message feedback = new Message();

				// This is about database
				// get data from database
				UserDao database = new UserDao();
				User dataUser = database.selectUserById(loginUser.getUserID());

				String command = requestFromClient.getMessageType();
				switch (command) {
					case "0": // register request
						System.out.println("Server is checking data with database....");
						if (!chekcExist(loginUser.getUserID())) {
							loginUser.setState(true);
							database.insertUser(loginUser);
							System.out.println("Server: register successful");
							feedback.setMessageType("0");
							mouth.writeObject(feedback);
							// build a thread between server and client, different client will input
							// different socket
							NewServerThread thread = new NewServerThread(s);
							ManagerServerThread.addClientThread(loginUser.getUserID(), thread);
							// start the thread
							thread.start();
							System.out.println("finish");
						} else {
							// 2 means fail
							feedback.setMessageType("2");
							System.out.println("register Fail");
							mouth.writeObject(feedback);
							// if fail,then close this connection into while loop start again
							s.close();
						}
						break;
					case "1": // login request
						System.out.println();
						// ------------------------------------ get data from
						// database------------------------
						if (loginUser.getUserID().equals(dataUser.getUserID())
								&& loginUser.getPassword().equals(dataUser.getPassword())) {
							// ------------------------------------ get data from
							// database------------------------

							System.out.println("Server is checking email and password....");
							// if(loginUser.getUserID().equals("01") &&
							// loginUser.getPassword().equals("12345")
							// || loginUser.getUserID().equals("02") &&
							// loginUser.getPassword().equals("12345")
							// || loginUser.getUserID().equals("03") &&
							// loginUser.getPassword().equals("12345")) {
							System.out.println("Server: login successful");

							// update my state on database
							database.updateOnline(loginUser.getUserID());

							// 1 means successful
							feedback = requestFromClient;
							userTable = new ArrayList<User>();
							userTable = database.selectAllBut(loginUser.getUserID());// get userTable from database
							feedback.getUser().setFriendList(userTable);
							System.out.println("This is server I send first friendList "
									+ feedback.getUser().getFriendList().toString());

							// build a thread between server and client, different client will input
							// different socket
							NewServerThread thread = new NewServerThread(s);
							ManagerServerThread.addClientThread(loginUser.getUserID(), thread);
							mouth.writeObject(feedback);
							// start the thread
							thread.start();
							NewServerThread.informUserBut(loginUser.getUserID());
							System.out.println("finish");
						} else {
							// 2 means fail
							feedback.setMessageType("2");
							System.out.println("login Fail");
							mouth.writeObject(feedback);
							// if fail,then close this connection into while loop start again
							s.close();
						}
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean chekcExist(String userID) {
		boolean exist = true;
		UserDao a = new UserDao();
		User input = a.selectUserById(userID);
		if (input == null) {
			exist = false;
		}
		return exist;
	}

	public static void main(String[] args) {
		ServerModel s = new ServerModel();
	}
}
