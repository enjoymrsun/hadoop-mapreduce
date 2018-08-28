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
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Server {

  private JFrame frame;
  private JTextArea contentArea;
  private JTextField txtMessage;
  private JTextField txtMax;
  private JTextField txtPort;
  private JButton btnStart;
  private JButton btnStop;
  private JButton btnSend;
  private JPanel northPanel;
  private JPanel southPanel;
  private JScrollPane rightPanel;
  private JScrollPane leftPanel;
  private JSplitPane centerSplit;
  private JList userList;
  private DefaultListModel listModel;

  private ServerSocket serverSocket;
  private ServerThread serverThread;
  private ArrayList<ClientThread> clients;

  private boolean isStart = false;

  // 主方法, 程序执行入口
  // Main method, start a server.
  public static void main(String[] args) {
    new Server();
  }

  // 执行消息发送
  // Send Message.
  public void send() {
    if (!isStart) {
      JOptionPane.showMessageDialog(frame, "Server has not been started, CANNOT send message!", "Error",
              JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (clients.size() == 0) {
      JOptionPane.showMessageDialog(frame, "No users online, CANNOT send message!", "Error",
              JOptionPane.ERROR_MESSAGE);
      return;
    }
    String message = txtMessage.getText().trim();
    if (message == null || message.equals("")) {
      JOptionPane.showMessageDialog(frame, "Message CANNOT be empty!", "Error",
              JOptionPane.ERROR_MESSAGE);
      return;
    }
    // 群发服务器消息
    // Send Message from Server to all Clients.
    sendServerMessage(message);
    contentArea.append("SERVER: " + txtMessage.getText() + "\r\n");
    txtMessage.setText(null);
  }

  // 构造放法
  // Constructor
  public Server() {
    frame = new JFrame("Server");
    // 更改JFrame的图标：
    // change JFrame icon
    frame.setIconImage(Toolkit.getDefaultToolkit().createImage(Server.class.getResource("icons/java.jpg")));
    contentArea = new JTextArea();
    contentArea.setEditable(false);
    contentArea.setForeground(Color.blue);
    txtMessage = new JTextField();
    txtMax = new JTextField("30");
    txtPort = new JTextField("15213");
    btnStart = new JButton("start");
    btnStop = new JButton("stop");
    btnSend = new JButton("send");
    btnStop.setEnabled(false);
    listModel = new DefaultListModel();
    userList = new JList(listModel);

    southPanel = new JPanel(new BorderLayout());
    southPanel.setBorder(new TitledBorder("Write Your Message"));
    southPanel.add(txtMessage, "Center");
    southPanel.add(btnSend, "East");
    leftPanel = new JScrollPane(userList);
    leftPanel.setBorder(new TitledBorder("Users Online"));

    rightPanel = new JScrollPane(contentArea);
    rightPanel.setBorder(new TitledBorder("Messages"));

    centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
    centerSplit.setDividerLocation(100);
    northPanel = new JPanel();
    northPanel.setLayout(new GridLayout(1, 6));
    northPanel.add(new JLabel("User Limit Num"));
    northPanel.add(txtMax);
    northPanel.add(new JLabel("Port Num"));
    northPanel.add(txtPort);
    northPanel.add(btnStart);
    northPanel.add(btnStop);
    northPanel.setBorder(new TitledBorder("Setup Info"));

    frame.setLayout(new BorderLayout());
    frame.add(northPanel, "North");
    frame.add(centerSplit, "Center");
    frame.add(southPanel, "South");
    frame.setSize(800, 600);
    // 设置全屏
    // set to all screen
    // frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
    int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
    frame.setLocation((screen_width - frame.getWidth()) / 2, (screen_height - frame.getHeight()) / 2);
    frame.setVisible(true);

    // 关闭窗口时事件
    // close the window
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        if (isStart) {
          closeServer();// 关闭服务器
        }
        System.exit(0);// 退出程序
      }
    });

    // 文本框按回车键时事件
    // press the 'enter' button to send message
    txtMessage.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        send();
      }
    });

    // 单击发送按钮时事件
    // click the 'send' button to send message
    btnSend.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        send();
      }
    });

    // 单击启动服务器按钮时事件
    // click the 'start' button to start the server
    btnStart.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (isStart) {
          JOptionPane.showMessageDialog(frame, "Server has already been started, DO NOT start it again!",
                  "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }
        int max;
        int port;
        try {
          try {
            max = Integer.parseInt(txtMax.getText());
          } catch (Exception e1) {
            throw new Exception("User Limit Num should be positive integer!");
          }
          if (max <= 0) {
            throw new Exception("User Limit Num should be positive integer!");
          }
          try {
            port = Integer.parseInt(txtPort.getText());
          } catch (Exception e1) {
            throw new Exception("Port Num should be positive integer!");
          }
          if (port <= 0) {
            throw new Exception("Port Num should be positive integer!");
          }
          serverStart(max, port);
          contentArea.append("Server started successfully! Users Limit: " + max + ", Port: " + port + "\r\n");
          JOptionPane.showMessageDialog(frame, "Server started successfully!");
          btnStart.setEnabled(false);
          txtMax.setEnabled(false);
          txtPort.setEnabled(false);
          btnStop.setEnabled(true);
        } catch (Exception exc) {
          JOptionPane.showMessageDialog(frame, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    // 单击停止服务器按钮时事件
    // click the 'stop' button to stop the server
    btnStop.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!isStart) {
          JOptionPane.showMessageDialog(frame, "Server has NOT been started, CANNOT stop it!", "Error",
                  JOptionPane.ERROR_MESSAGE);
          return;
        }
        try {
          closeServer();
          btnStart.setEnabled(true);
          txtMax.setEnabled(true);
          txtPort.setEnabled(true);
          btnStop.setEnabled(false);
          contentArea.append("Server stopped successfully!\r\n");
          JOptionPane.showMessageDialog(frame, "Server stopped successfully!");
        } catch (Exception exc) {
          JOptionPane.showMessageDialog(frame, "Server stop exception. Try again later!", "Error",
                  JOptionPane.ERROR_MESSAGE);
        }
      }
    });
  }

  // 启动服务器
  // start the server
  public void serverStart(int max, int port) throws BindException {
    try {
      clients = new ArrayList<ClientThread>();
      // serverSocket is like 'listenfd' in CS-15213
      serverSocket = new ServerSocket(port);
      serverThread = new ServerThread(serverSocket, max);
      serverThread.start();
      isStart = true;
    } catch (BindException e) {
      isStart = false;
      throw new BindException("Port Num is in use, please try another one.");
    } catch (Exception e1) {
      e1.printStackTrace();
      isStart = false;
      throw new BindException("Server start exception. Try again later!");
    }
  }

  // 关闭服务器
  // stop the server
  @SuppressWarnings("deprecation")
  public void closeServer() {
    try {
      // 停止服务器线程
      // stop the server thread
      if (serverThread != null)
        serverThread.stop();

      for (int i = clients.size() - 1; i >= 0; i--) {
        // 给所有在线用户发送关闭命令
        // send 'close' message to all users online
        clients.get(i).getWriter().println("CLOSE");
        clients.get(i).getWriter().flush();
        // 释放资源 Release Resources
        // 停止此条为客户端服务的线程
        // stop this client thread
        clients.get(i).stop();
        clients.get(i).reader.close();
        clients.get(i).writer.close();
        clients.get(i).socket.close();
        clients.remove(i);
      }
      // 关闭服务器端连接
      // close the server connection
      if (serverSocket != null) {
        serverSocket.close();
      }
      // 清空用户列表
      // clean the users list
      listModel.removeAllElements();
      isStart = false;
    } catch (IOException e) {
      e.printStackTrace();
      isStart = true;
    }
  }

  // 群发服务器消息
  // send group message from server
  public void sendServerMessage(String message) {
    for (int i = clients.size() - 1; i >= 0; i--) {
      clients.get(i).getWriter().println("SERVER: " + message);
      clients.get(i).getWriter().flush();
    }
  }

  // 服务器线程
  // server thread
  class ServerThread extends Thread {
    private ServerSocket serverSocket;
    // 人数上限 User Limit Num
    private int max;

    // 服务器线程的构造方法
    // constructor
    public ServerThread(ServerSocket serverSocket, int max) {
      this.serverSocket = serverSocket;
      this.max = max;
    }

    public void run() {
      // 不停的等待客户端的链接
      // keep waiting for the client connection
      while (true) {
        try {
          // socket is 'connfd' in CS-15213
          Socket socket = serverSocket.accept();
          // 如果已达人数上限
          // achieve user limit maximum
          if (clients.size() == max) {
            BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter w = new PrintWriter(socket.getOutputStream());
            // 接收客户端的基本用户信息 | get client's user info
            String inf = r.readLine();
            StringTokenizer st = new StringTokenizer(inf, "@");
            User user = new User(st.nextToken(), st.nextToken());
            // 反馈连接成功信息 | response the connection message
            w.println("MAX@Server: sorry, " + user.getName()
                    + user.getIp() + ", users online reach the maximum allowed, please connect again later.");
            w.flush();
            // 释放资源 | Release Resource
            r.close();
            w.close();
            socket.close();
            continue;
          }
          // much like the 'connfd' in CS-15213
          ClientThread client = new ClientThread(socket);
          // 开启对此客户端服务的线程
          // start the thread for service this client
          client.start();
          clients.add(client);
          // 更新在线列表 | update online users list
          listModel.addElement(client.getUser().getName());
          contentArea.append(client.getUser().getName() + client.getUser().getIp() + " online!\r\n");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  // 为一个客户端服务的线程
  // a thread used to provide services for the client
  class ClientThread extends Thread {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private User user;

    public BufferedReader getReader() {
      return reader;
    }

    public PrintWriter getWriter() {
      return writer;
    }

    public User getUser() {
      return user;
    }

    // 客户端线程的构造方法
    // constructor
    public ClientThread(Socket socket) {
      try {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
        // 接收客户端的基本用户信息
        // get this user info from the client side
        String inf = reader.readLine();
        StringTokenizer st = new StringTokenizer(inf, "@");
        user = new User(st.nextToken(), st.nextToken());
        // 反馈连接成功信息
        // response with the connection success message
        writer.println(user.getName() + user.getIp() + " connect to server successfully!");
        writer.flush();
        // 反馈当前在线用户信息
        // response with the users online info
        if (clients.size() > 0) {
          String temp = "";
          for (int i = clients.size() - 1; i >= 0; i--) {
            temp += (clients.get(i).getUser().getName() + "/" + clients.get(i).getUser().getIp()) + "@";
          }
          writer.println("USERLIST@" + clients.size() + "@" + temp);
          writer.flush();
        }
        // 向所有在线用户发送该用户上线命令
        // send notification to all users online about this new user who has been offline
        for (int i = clients.size() - 1; i >= 0; i--) {
          clients.get(i).getWriter().println("ADD@" + user.getName() + user.getIp());
          clients.get(i).getWriter().flush();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // 不断接收客户端的消息，进行处理
    // keep receiving message from client side
    @SuppressWarnings("deprecation")
    public void run() {
      String message = null;
      while (true) {
        try {
          // 接收客户端消息
          // receive message from client side
          message = reader.readLine();
          // 下线命令
          // client user offline command
          if (message.equals("CLOSE")) {
            contentArea.append(this.getUser().getName() + this.getUser().getIp() + "offline!\r\n");
            // 断开连接释放资源
            // close the connection and release resource
            reader.close();
            writer.close();
            socket.close();

            // 向所有在线用户发送该用户的下线命令
            // send notification to all users online about this new user who has been offline
            for (int i = clients.size() - 1; i >= 0; i--) {
              clients.get(i).getWriter().println("DELETE@" + user.getName());
              clients.get(i).getWriter().flush();
            }

            // 更新在线列表
            // update users online list
            listModel.removeElement(user.getName());

            // 删除此条客户端服务线程
            // DELETE this 'connfd'
            for (int i = clients.size() - 1; i >= 0; i--) {
              if (clients.get(i).getUser() == user) {
                ClientThread temp = clients.get(i);
                // 删除此用户的服务线程
                // DELETE this thread from the client threads we support
                clients.remove(i);
                // 停止这条服务线程
                // STOP this client thread
                temp.stop();
                return;
              }
            }
          } else {
            // 转发消息
            // send this message to client users in the chat room
            dispatcherMessage(message);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    // 转发消息
    // send this message to client users in the chat room
    public void dispatcherMessage(String message) {
      StringTokenizer stringTokenizer = new StringTokenizer(message, "@");
      String source = stringTokenizer.nextToken();
      String owner = stringTokenizer.nextToken();
      String content = stringTokenizer.nextToken();
      message = source + ": " + content;
      contentArea.append(message + "\r\n");
      // 群发, 默认选项
      // default owner is 'ALL' in client message thread
      if (owner.equals("ALL")) {
        for (int i = clients.size() - 1; i >= 0; i--) {
          clients.get(i).getWriter().println(message);
          clients.get(i).getWriter().flush();
        }
      }
    }
  }
}
