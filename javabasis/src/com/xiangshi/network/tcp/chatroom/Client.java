package com.xiangshi.network.tcp.chatroom;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Client {

  private JFrame frame;
  private JList userList;
  private JTextArea textArea;
  private JTextField textField;
  private JTextField txtPort;
  private JTextField txtHostIp;
  private JTextField txtName;
  private JButton btnStart;
  private JButton btnStop;
  private JButton btnSend;
  private JPanel northPanel;
  private JPanel southPanel;
  private JScrollPane rightScroll;
  private JScrollPane leftScroll;
  private JSplitPane centerSplit;

  private DefaultListModel listModel;
  private boolean isConnected = false;

  private Socket socket;
  private PrintWriter writer;
  private BufferedReader reader;
  // 负责接收消息的线程
  // thread used to receive and send message
  private MessageThread messageThread;
  // 所有在线用户
  // all users online
  private Map<String, User> onLineUsers = new HashMap<String, User>();

  // 主方法,程序入口
  // main method
  public static void main(String[] args) {
    new Client();
  }

  // 执行发送
  // send message
  public void send() {
    if (!isConnected) {
      JOptionPane.showMessageDialog(frame, "Disconnected to server, CANNOT send message!", "Error",
              JOptionPane.ERROR_MESSAGE);
      return;
    }
    String message = textField.getText().trim();
    if (message == null || message.equals("")) {
      JOptionPane.showMessageDialog(frame, "Message CANNOT be empty!", "Error",
              JOptionPane.ERROR_MESSAGE);
      return;
    }
    sendMessage(frame.getTitle() + "@" + "ALL" + "@" + message);
    textField.setText(null);
  }

  // 构造方法
  // constructor
  public Client() {
    textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setForeground(Color.blue);
    textField = new JTextField();
    txtPort = new JTextField("15213");
    txtHostIp = new JTextField("127.0.0.1");
    txtName = new JTextField("chris");
    btnStart = new JButton("connect");
    btnStop = new JButton("disconnect");
    btnSend = new JButton("send");
    listModel = new DefaultListModel();
    userList = new JList(listModel);

    northPanel = new JPanel();
    northPanel.setLayout(new GridLayout(1, 7));
    northPanel.add(new JLabel("Port Num"));
    northPanel.add(txtPort);
    northPanel.add(new JLabel("Server IP"));
    northPanel.add(txtHostIp);
    northPanel.add(new JLabel("Name"));
    northPanel.add(txtName);
    northPanel.add(btnStart);
    northPanel.add(btnStop);
    northPanel.setBorder(new TitledBorder("Connect Info"));

    rightScroll = new JScrollPane(textArea);
    rightScroll.setBorder(new TitledBorder("Messages"));
    leftScroll = new JScrollPane(userList);
    leftScroll.setBorder(new TitledBorder("Users Online"));
    southPanel = new JPanel(new BorderLayout());
    southPanel.add(textField, "Center");
    southPanel.add(btnSend, "East");
    southPanel.setBorder(new TitledBorder("Write Your Message"));

    centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll, rightScroll);
    centerSplit.setDividerLocation(100);

    frame = new JFrame("Client");
    // 更改JFrame的图标：
    // change JFrame icon
    frame.setIconImage(Toolkit.getDefaultToolkit().createImage(Client.class.getResource("icons/java.jpg")));
    frame.setLayout(new BorderLayout());
    frame.add(northPanel, "North");
    frame.add(centerSplit, "Center");
    frame.add(southPanel, "South");
    frame.setSize(800, 600);
    int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
    int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
    frame.setLocation((screen_width - frame.getWidth()) / 2, (screen_height - frame.getHeight()) / 2);
    frame.setVisible(true);

    // 写消息的文本框中按回车键时事件
    // press the 'enter' button to send message
    textField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        send();
      }
    });

    // 单击发送按钮时事件
    // click the 'send' button to send message
    btnSend.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        send();
      }
    });

    // 单击连接按钮时事件
    // click the 'connect' button to connect to the server
    btnStart.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int port;
        if (isConnected) {
          JOptionPane.showMessageDialog(frame, "Already connected to the server, please DO NOT connect again!",
                  "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }
        try {
          try {
            port = Integer.parseInt(txtPort.getText().trim());
          } catch (NumberFormatException e2) {
            throw new Exception("Port Num should be positive integer!");
          }
          String hostIp = txtHostIp.getText().trim();
          String name = txtName.getText().trim();
          if (name.equals("") || hostIp.equals("")) {
            throw new Exception("Name and Server IP CANNOT be empty!");
          }
          boolean flag = connectServer(port, hostIp, name);
          if (flag == false) {
            throw new Exception("Connect to server failed.");
          }
          frame.setTitle(name);
          JOptionPane.showMessageDialog(frame, "Connect to server successfully!");
        } catch (Exception exc) {
          JOptionPane.showMessageDialog(frame, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    // 单击断开按钮时事件
    // click the 'disconnect' button to disconnect to the server
    btnStop.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!isConnected) {
          JOptionPane.showMessageDialog(frame, "Already disconnected to the server, please DO NOT disconnect again!",
                  "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }
        try {
          // 断开连接
          // disconnect
          boolean flag = closeConnection();
          if (flag == false) {
            throw new Exception("Disconnect exception. Try again later!");
          }
          JOptionPane.showMessageDialog(frame, "Disconnect successfully!");
        } catch (Exception exc) {
          JOptionPane.showMessageDialog(frame, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    // 关闭窗口时事件 | close the window
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        if (isConnected) {
          // 关闭连接 | close connection
          closeConnection();
        }
        System.exit(0);
      }
    });
  }

  // connect to the server
  public boolean connectServer(int port, String hostIp, String name) {
    // 连接服务器 | connect to the server
    try {
      // socket is 'clientfd' in CS-15213
      // 根据端口号和服务器ip建立连接
      // connect to server depends on the server IP and port number
      socket = new Socket(hostIp, port);
      writer = new PrintWriter(socket.getOutputStream());
      reader = new BufferedReader(new InputStreamReader(socket
              .getInputStream()));
      // 发送客户端用户基本信息(用户名和ip地址)
      // send client side user name and client side IP address
      sendMessage(name + "@" + socket.getLocalAddress().toString());
      // 开启接收消息的线程
      // start a message thread to communicate with server
      messageThread = new MessageThread(reader, textArea);
      messageThread.start();
      isConnected = true; // connected
      return true;
    } catch (Exception e) {
      textArea.append("Connection with Port Num: " + port + ", IP address: " + hostIp + " failed" + "\r\n");
      isConnected = false; // not connected
      return false;
    }
  }

  // send message
  public void sendMessage(String message) {
    writer.println(message);
    writer.flush();
  }

  // 客户端主动关闭连接 | client side intentionally close the connection
  @SuppressWarnings("deprecation")
  public synchronized boolean closeConnection() {
    try {
      // 发送断开连接命令给服务器 | send disconnect command to server
      sendMessage("CLOSE");
      // 停止接受消息线程 | STOP this message thread
      messageThread.stop();
      // 清空用户列表 | clean the users online list
      listModel.removeAllElements();
      // 释放资源 | release resource
      if (reader != null) {
        reader.close();
      }
      if (writer != null) {
        writer.close();
      }
      if (socket != null) {
        socket.close();
      }
      isConnected = false;
      return true;
    } catch (IOException e1) {
      e1.printStackTrace();
      isConnected = true;
      return false;
    }
  }

  // 不断接收消息的线程 | a thread to keep receiving message between client and server
  class MessageThread extends Thread {
    private BufferedReader reader;
    private JTextArea textArea;

    // 接收消息线程的构造方法
    // constructor
    public MessageThread(BufferedReader reader, JTextArea textArea) {
      this.reader = reader;
      this.textArea = textArea;
    }

    // 被动的关闭连接
    // passively closed connection to server, when server is closed or down
    public synchronized void closeCon() throws Exception {
      // 清空用户列表
      // clean the users online list
      listModel.removeAllElements();
      // 被动的关闭连接释放资源
      // passively close the reader, writer and socket
      if (reader != null) {
        reader.close();
      }
      if (writer != null) {
        writer.close();
      }
      if (socket != null) {
        socket.close();
      }
      // 修改状态为断开
      // change the status to disconnect
      isConnected = false;
    }

    public void run() {
      String message = "";
      while (true) {
        try {
          message = reader.readLine();
          StringTokenizer stringTokenizer = new StringTokenizer(message, "/@");
          String command = stringTokenizer.nextToken(); // 命令 | command
          // 服务器已关闭命令 | server is closed
          if (command.equals("CLOSE")) {
            textArea.append("Server is closed!\r\n");
            // 被动的关闭连接
            // passively closed connection to server, when server is closed or down
            closeCon();
            // 结束线程 | end the thread
            return;
          } else if (command.equals("ADD")) {
            // 有用户上线更新在线列表 | new user online
            String username = "";
            String userIp = "";
            if ((username = stringTokenizer.nextToken()) != null
                    && (userIp = stringTokenizer.nextToken()) != null) {
              User user = new User(username, userIp);
              onLineUsers.put(username, user);
              listModel.addElement(username);
            }
          } else if (command.equals("DELETE")) {
            // 有用户下线更新在线列表 | online user becomes offline
            String username = stringTokenizer.nextToken();
            User user = (User) onLineUsers.get(username);
            onLineUsers.remove(user);
            listModel.removeElement(username);
          } else if (command.equals("USERLIST")) {
            // 加载在线用户列表 | load the user info list
            int size = Integer.parseInt(stringTokenizer.nextToken());
            String username = null;
            String userIp = null;
            for (int i = 0; i < size; i++) {
              username = stringTokenizer.nextToken();
              userIp = stringTokenizer.nextToken();
              User user = new User(username, userIp);
              onLineUsers.put(username, user);
              listModel.addElement(username);
            }
          } else if (command.equals("MAX")) {
            // 人数已达上限 | users num reach the maximum
            textArea.append(stringTokenizer.nextToken() + stringTokenizer.nextToken() + "\r\n");
            // 被动的关闭连接
            // passively closed connection to server, when users reach maximum allowed
            closeCon();
            JOptionPane.showMessageDialog(frame, "Server Buffer Overflow!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            // 结束线程 | end the thread
            return;
          } else {
            // 普通消息
            // normal message, show it on the 'Messages' board
            textArea.append(message + "\r\n");
          }
        } catch (IOException e) {
          e.printStackTrace();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}
