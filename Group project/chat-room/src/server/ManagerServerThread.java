package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

import common.Message;
import common.MessageType;
import common.User;

/**
 * 
 * To manage Server connect Client thread
 *
 */

// this is ManageClientThread
public class ManagerServerThread {
	// public static HashMap ServerTable = new HashMap<String,NewServerThread>();
	// if on-line the email ID should be in the nap

	public static HashMap ServerTable = new HashMap<String, NewServerThread>();

	// add client thread in the table
	public static void addClientThread(String userID, NewServerThread thread) {
		ServerTable.put(userID, thread);
	}

	// to get the thread
	public static NewServerThread getClientThread(String userID) {
		return (NewServerThread) ServerTable.get(userID);
	}

	// to delete the thread
	public static NewServerThread deleteClientThread(String userID) {
		return (NewServerThread) ServerTable.remove(userID);
	}

}
