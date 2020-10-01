package common;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Message implements Serializable {
	private String messageType;
	private String sender;
	private String recipient;
	private String contain;
	private String sendTime;
	private User user;
	private User ReceiveUser;
	private ArrayList<Message> chatHistory;

	/**
	 * Getter for the messageType
	 * @return The messageType is returned
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * Setter for the messageType
	 * @param The new messageType updated
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	/**
	 * Getter for the sender.
	 * @return The sender is returned
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Setter for the sender
	 * @param The new sender updated.
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	/**
	 * Getter for the recipient
	 * @return The recipient is returned.
	 */
	public String getRecipient() {
		return recipient;
	}

	/**
	 * Setter for the recipient
	 * @param The new recipient updated.
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	/**
	 * Getter for the contain
	 * @return The contain is returned.
	 */
	public String getContain() {
		return contain;
	}

	/**
	 * Setter for the contain
	 * @param The new contain updated.
	 */
	public void setContain(String contain) {
		this.contain = contain;
	}
	
	/**
	 * Getter for the sendTime
	 * @return The sendTime is returned
	 */
	public String getSendTime() {
		return sendTime;
	}

	/**
	 * Setter for the sendTime
	 * @param The new currently sendTime updated
	 */
	public void setSendTime() {
		Date a = new Date();
		String time = String.valueOf(a.getTime());
		this.sendTime = time;
	}

	/**
	 * Setter for the sendTime
	 * @param The new sendTime updated
	 */
	public void setSendTime(String time) {
		this.sendTime = time;
	}
	
	/**
	 * Getter for the User
	 * @return The user is returned.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Setter for the User
	 * @param The new User updated.
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Getter for the receiveUser
	 * @return The receiveUser is returned.
	 */
	public User getReceiveUser() {
		return ReceiveUser;
	}

	/**
	 * Setter for the receiveUser
	 * @param The new receiveUser updated.
	 */
	public void setReceiveUser(User receiveUser) {
		ReceiveUser = receiveUser;
	}
	
	/**
	 * Getter for Chat history
	 * @return The ArrayList of ChatHistory is returned
	 */
	public ArrayList<Message> getChatHistory() {
		return chatHistory;
	}

	/**
	 * Setter for the chatHistory.
	 * @param The new chatHistory updated.
	 */
	public void setChatHistory(ArrayList<Message> chatHistory) {
		this.chatHistory = chatHistory;
	}

	/**
	 * @return A human readable description of the message in form
	 * of the four field variables specifying it.
	 */
	@Override
	public String toString() {
		return "Message [sender=" + sender + ", recipient=" + recipient + ", contain=" + contain + ", sendTime="
				+ sendTime + "]";
	}
}
