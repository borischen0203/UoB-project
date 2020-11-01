
# Java Workshop Team Project - Chat room

This is a group project of team Wadi Rum, part of the curriculum in the Computer Science MSc in the 
University of Birmingham. Team Wadi Rum members: Yi-Ming Chen, Shinjo Sato, Zhengnan Sun, Saba 
Akhlagh-Nejat and Ibiyemi Ogunyemi.

This projects idea was to build a multi-threaded, instant messaging service where each user can create 
chat rooms for group or one-to-one chatting, with technologies that we have learnt during the course. 
Thus, Java is mainly used with PostgreSQL.

The application is based on a three-tier-architecture, with Java being used for the whole app. For the 
front-end JavaFX is used, with Java in the back-end and PostgreSQL in the Data layer. JDBC driver 
for the PostgreSQL, custom protocol for communication.

Every client connects to the server, which listens for new messages over TCP/IP. Several workers in the 
server take care of the processes running, each new message gets stored in the database for archiving, 
thus a user can "scroll up" to find older messages even when recconecting to the service through another 
machine.


The rough function that the user should be able to:
1. Login to the server using a username and password.
2. Create a new account.
3. View the list of other users who are currently online.
4. Initiate chat with any user who is online.
5. Engage in multiple chats with different users
6. Allow a user to view previous chats of which they have been a part of.

## Login screen
<p align="left">
    <img src="https://i.imgur.com/rrLBFl9.png" alt="Sample"  width="217" height="328">
    <p align="left">
</p>
