package server;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import client.*;
import common.Message;
import common.MessageType;
import common.User;

import java.io.*;

/**
 * This class is Server Connect Client Thread, to build a thread between server
 * and client
 * 
 */
// ServerConClientThread

public class NewServerThread extends Thread {
	Socket s;

	public NewServerThread(Socket s) {
		// to get the socket of thread
		this.s = s;
	}

	public static void informUserBut(String myself) {
		HashMap ServerTable = ManagerServerThread.ServerTable;
		Iterator<String> iterator = ServerTable.keySet().iterator();
		while (iterator.hasNext()) {
			String userID = iterator.next();
			if (!userID.equals(myself)) {
				System.out.println("I am handle " + userID + " friendList");
				Message m = new Message();
				User informer = new User();
				UserDao database = new UserDao();
				m.setMessageType(MessageType.message_get_onLineFriend);
				informer.setFriendList(database.selectAllBut(userID));
				m.setUser(informer);
				try {
					NewServerThread FindInfromer = ManagerServerThread.getClientThread(userID);
					ObjectOutputStream mouth = new ObjectOutputStream(FindInfromer.s.getOutputStream());
					mouth.writeObject(m);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void run() {
		while (true) {

			try {
				// this thread can receive message from client
				ObjectInputStream ear = new ObjectInputStream(s.getInputStream());
				Message m = (Message) ear.readObject();
				UserDao database = new UserDao();
				// System.out.println("-----\nFrom:"+m.getSender()+"\nTo:
				// "+m.getRecipient()+"\nMessage: "+m.getContain()+"\n-----");

				// handle different type of message

				// server ------- text ---------------->recipient
				if (m.getMessageType().equals(MessageType.message_comm_mes)) {
					database.saveChatHistory(m);
					// To get the thread of recipient
					NewServerThread FindRecipent = ManagerServerThread.getClientThread(m.getRecipient());
					ObjectOutputStream mouth = new ObjectOutputStream(FindRecipent.s.getOutputStream());
					mouth.writeObject(m);
					System.out.println("transfer message successful");
				}

				else if (m.getMessageType().equals(MessageType.message_get_onLineFriend)) {
					// UserDao database= new UserDao();
					database.updateOffline(m.getSender());
					System.out.println("the database offline succesful");
					informUserBut(m.getSender());
					ManagerServerThread.deleteClientThread(m.getSender());
					System.out.println("the thread delete succesful");
				}

				else if (m.getMessageType().equals(MessageType.message_askHistory)) {
					System.out.println("receive ask chat history request from client");
					ArrayList<Message> chatHistory = database.selectHistory(m.getSender(), m.getRecipient());
					m.setChatHistory(chatHistory);
					// Message feedback = new Message();
					// feedback.
					// feedback.setChatHistory(chatHistory);
					NewServerThread FindSender = ManagerServerThread.getClientThread(m.getSender());
					ObjectOutputStream mouth = new ObjectOutputStream(FindSender.s.getOutputStream());
					mouth.writeObject(m);
					System.out.println("send chat history request from client");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
