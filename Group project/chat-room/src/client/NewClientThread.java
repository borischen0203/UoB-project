package client;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import common.Message;
import common.MessageType;
import common.User;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.io.*;

/**
 * 
 * This part builds the thread between clients and server.Thus, clients can keep receiving the message from server.
 * 
 * [client] <-----(keep receiving message from server )-------[server]
 * 
 * @author yxc1016
 * @date   2020-03-14 
 *
 */
public class NewClientThread extends Thread {
	private Socket s;
	public HashMap<String, ChatWindow> chatRooms;

	/**
     * This constructor creates a NewClientThread from the two parts: s, chatRooms
     * which are a Socket, and a HashMap, respectively.
     * @param s The s as a Socket.
     * @param chatRooms  The chaRooms as a HashMap which store <friend ID, ChatWindow>.
     */    
	public NewClientThread(Socket s, HashMap<String, ChatWindow> chatRooms) {
		this.s = s;
		this.chatRooms = chatRooms;
	}

	/**
     * Getter for the socket.
     * @return The socket of the thread is returned.
	 */
	public Socket getS() {
		return s;
	}

	/**
	 * Setter for the socket.
	 * @param the new socket of the updated NewClientThread.
	 */
	public void setS(Socket s) {
		this.s = s;
	}

	
	/**
	 * deal with different message from server.
	 */
	public void run() {
		while (true) {
			try {
				ObjectInputStream ear = new ObjectInputStream(s.getInputStream());
				Message m = (Message) ear.readObject();

				/**
				 * This part shows the receive chat message on the chat window.
				 */
				if (m.getMessageType().equals(MessageType.message_comm_mes)) {
					User friend = m.getUser();
					System.out.println("line59 " + friend.toString());
					User ClientUser = m.getReceiveUser();
					System.out.println("Line61 " + ClientUser.toString());
					ChatWindow friendWindow = chatRooms.get(friend.getUserID());

					if (friendWindow == null) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								System.out.println("pop the window!!");
								ChatWindow chatwindow = new ChatWindow(ClientUser, friend);
								System.out.println("line66 " + chatwindow.toString());
								chatRooms.put(friend.getUserID(), chatwindow);
								Stage stage2 = chatwindow.getStage();
								stage2.show();
								chatRooms.get(friend.getUserID()).receiveMessage(m.getContain());
								/*
								 * closing the chat window.
								 */
								stage2.showingProperty().addListener((observable, oldValue, newValue) -> {
									if (oldValue == true && newValue == false) {
										chatRooms.remove(friend.getUserID());
									}
								});
							}
						});
					} else {
						friendWindow.receiveMessage(m.getContain());
					}
				}

				/**
				 * This part update the friend list when friends go online.
				 */
				else if (m.getMessageType().equals(MessageType.message_get_onLineFriend)) {
					System.out.println("receive inform request");
					List<User> updateFriendList = new ArrayList<User>();
					updateFriendList = m.getUser().getFriendList();
					if (updateFriendList != null) {
						System.out.println("recevie new list from server " + updateFriendList.toString());
						ClientController.updateFriendList(updateFriendList);
					}
				} 
				
				/**
				 * This part reloads the previous chat history.
				 */
				else if (m.getMessageType().equals(MessageType.message_askHistory)) {
					ArrayList<Message> messagelist = (ArrayList<Message>) m.getChatHistory();
					User friend = messagelist.get(0).getReceiveUser();
					chatRooms.get(friend.getUserID()).reloadPreviousTalk(messagelist);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
