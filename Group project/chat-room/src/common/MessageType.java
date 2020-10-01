package common;

public interface MessageType {
	String message_register = "0";// register
	String message_login = "1";// Login successful/send login request
	String message_login_fail = "2";// login fail
	String message_comm_mes = "3";// chat message
	String message_get_onLineFriend = "4";// get friend on-line
	String message_offLineFriend = "5";// return friend off-line
	String message_askHistory = "6";// return chat history

}
