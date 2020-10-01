package common;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
	private String userID;
	private String userName;
	private String password;
	private String email;
	private boolean state;
	private List<User> friendList;
	
	
	/**
    * This constructor creates a User from the five parts: userID,
    * userName, password, email, and state, which are a String, a String, a String,a String and a boolean,
    * respectively.
    * @param userID The userID of the User.
    * @param userName The userName of  the  User.
    * @param password The password of the User.
    * @param email  The email of the User.
    * @param state  The state of the User.
    */  
	public User(String userID, String userName, String password, String email, boolean state) {
		this.userID = userID;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.state = state;
	}
	public User() {
	}

	/**
	    * This constructor creates a User from the three parts: userID,
	    * userName and state, which are a String, a String  and a boolean,
	    * respectively.
	    * @param userID The userID of the User.
	    * @param userName The userName of  the  User.
	    * @param state  The state of the User.
	    */  
	public User(String userID, String userName, boolean state) {
		this.userID = userID;
		this.userName = userName;
		this.state = state;
	}

	
	/**
	 * Getter for the
	 * @return the userId
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * Getter for the
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Getter for the
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Getter for the
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Getter for the
	 * @return the state
	 */
	public boolean isState() {
		return state;
	}

	/**
	 * 
	 * @param state the state to set
	 */
	public void setState(boolean state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "User [userID=" + userID + ", userName=" + userName + ", state=" + state + "]";
	}

	/**
	 * Getter for the
	 * @return the friendList
	 */
	public List<User> getFriendList() {
		return friendList;
	}

	/**
	 * @param friendList the friendList to set
	 */
	public void setFriendList(List<User> friendList) {
		this.friendList = friendList;
	}

}
