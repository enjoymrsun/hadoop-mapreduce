package com.xiangshi.network.udp.screenbroadcast;

import javax.swing.*;

/**
 * StudentUI
 */
public class StudentUI extends JFrame {

  private JLabel imageLabel;

  public StudentUI() {
    init();
  }

  // 开启学生端
  private void init() {
    this.setTitle("学生端");
    this.setBounds(0, 0, 1450, 910);
    this.setLayout(null);

    imageLabel = new JLabel();
    imageLabel.setBounds(0, 0, 1450, 910);

    ImageIcon icon = new ImageIcon("input/welcome.jpg");
    imageLabel.setIcon(icon);
    this.add(imageLabel);
    this.setVisible(true);
  }

  // 更新学生端图片
  public void updateIcon(byte[] dataBytes) {
    ImageIcon icon = new ImageIcon(dataBytes);
    imageLabel.setIcon(icon);
  }
}
