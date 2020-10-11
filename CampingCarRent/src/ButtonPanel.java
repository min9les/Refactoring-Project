import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ButtonPanel extends JPanel {
   JButton btnInput, btnDelete, btnUpdate, btnReturn,btnRequest, btnRent;

   ButtonPanel() {
      btnInput = new JButton("�Է�");
      btnDelete = new JButton("����");
      btnUpdate = new JButton("����");
      btnReturn = new JButton("��ȯ");
      btnRent = new JButton("�뿩");
      btnRequest = new JButton("������û");
   }

   void update() {
      removeAll();
      add(btnInput);
      add(btnDelete);
      add(btnUpdate);
   }
   
   void carReturn() {
      removeAll();
      add(btnReturn);
   }
   
   void repairRequest() {
      removeAll();
      add(btnRequest);
   }
   void repairList() {
      removeAll();
      add(btnDelete);
      add(btnUpdate);
   }
   
   void carRent() {
      removeAll();
      add(btnRent);
   }
   
}