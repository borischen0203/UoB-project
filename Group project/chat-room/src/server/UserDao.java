package server;

import common.User;
import common.Message;
import java.util.Date;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public void insertUser(User user) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk/wadirum", "wadirum",
                    "977fggzmir");
            String sql = "INSERT INTO user_1(userid,username,password,email,state) VALUES(?,?,?,?,?);";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUserID());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.setBoolean(5, user.isState());
            // System.out.print(ps.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateOnline(String userid) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk/wadirum", "wadirum",
                    "977fggzmir");
            String sql = "update user_1 set state = ? where userid = ?";
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, true);
            ps.setString(2, userid);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateOffline(String userid) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk/wadirum", "wadirum",
                    "977fggzmir");
            String sql = "update user_1 set state = ? where userid = ?";
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, false);
            ps.setString(2, userid);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteUser(String userid) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk/wadirum", "wadirum",
                    "977fggzmir");
            String sql = "delete from user_1 where userid = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userid);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public User selectUserById(String userid) {
        User user = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from user_1 where userid = ?";
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk/wadirum", "wadirum",
                    "977fggzmir");
            ps = conn.prepareStatement(sql);
            ps.setString(1, userid);

            rs = ps.executeQuery();

            while (rs.next()) {
                user = new User();
                user.setUserID(rs.getString("userid"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

    public List<User> selectAll() {
        List<User> list = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk/wadirum", "wadirum",
                    "977fggzmir");
            sql = "SELECT * FROM user_1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getString("userid"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setState(rs.getBoolean("state"));
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public List<User> selectAllBut(String userID) {
        List<User> list1 = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk/wadirum", "wadirum",
                    "977fggzmir");
            sql = "SELECT * FROM user_1 WHERE NOT (userid= '" + userID + "')";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getString("userid"));
                user.setUserName(rs.getString("username"));
                user.setState(rs.getBoolean("state"));
                list1.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list1;
    }

    public void saveChatHistory(Message message) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk/wadirum", "wadirum",
                    "977fggzmir");
            String sql = "INSERT INTO chathistory(sender,recipient,contain,date) VALUES(?,?,?,?);";
            ps = conn.prepareStatement(sql);
            ps.setString(1, message.getSender());
            ps.setString(2, message.getRecipient());
            ps.setString(3, message.getContain());
            ps.setString(4, message.getSendTime());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Message> selectAllHistory() {
        List<Message> list = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk/wadirum", "wadirum",
                    "977fggzmir");
            sql = "SELECT * FROM chatHistory";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                message.setSender(rs.getString("sender"));
                message.setRecipient(rs.getString("recipient"));
                message.setContain(rs.getString("contain"));
                message.setSendTime(rs.getString("date"));

                list.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;

    }

    public ArrayList<Message> selectHistory(String sender, String recipient) {
        ArrayList<Message> chatHistory = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk/wadirum", "wadirum",
                    "977fggzmir");
            sql = "SELECT * FROM chatHistory WHERE sender IN ('" + sender + "','" + recipient + "') and "
                    + "recipient IN ('" + sender + "','" + recipient + "')";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                message.setSender(rs.getString("sender"));
                message.setRecipient(rs.getString("recipient"));
                message.setContain(rs.getString("contain"));
                message.setSendTime(rs.getString("date"));
                message.setUser(selectUserById(sender));
                message.setReceiveUser(selectUserById(recipient));
                chatHistory.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return chatHistory;
    }

    public void deleteChatHistory(String sender, String recipient) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk/wadirum", "wadirum",
                    "977fggzmir");
            String sql = "DELETE FROM chatHistory  WHERE sender = ? and recipient = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sender);
            preparedStatement.setString(2, recipient);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void deleteAllChatHistory() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk/wadirum", "wadirum",
                    "977fggzmir");
            String sql = "DELETE FROM chatHistory";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        UserDao a = new UserDao();
        // System.out.println(a.selectAll());
        // Message m = new Message();
        // m.setSender("01");
        // m.setRecipient("05");
        // m.setContain("Im 01");
        // m.setSendTime();
        // a.saveChatHistory(m);

        // System.out.println(a.selectHistory("03", "07"));
        // List<Message> list = a.selectHistory("03","07");
        // a.deleteChatHistory("02", "01");
        // a.deleteAllChatHistory();
        // List<Message> list = a.selectAllHistory();
        // for (Message b: list){
        // System.out.println(b);
        // }
        // System.out.println("---------------------------------------------------");
        List<Message> l = a.selectHistory("01", "02");
        for (Message c : l) {
            System.out.println(c.getUser());
            System.out.println(c.getReceiveUser());
            System.out.println(c);
        }
    }
}
