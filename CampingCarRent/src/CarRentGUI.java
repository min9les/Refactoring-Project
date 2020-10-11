import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class CarRentGUI extends JFrame implements ActionListener, MouseListener {

	JButton btnCampComp, btnCustomer, btnCampCar, btnRepairshop, btnRentCar, btnCarCheck, btnRepairList, btnRentList,
			btnSearch1, btnSearch2, btnSearch3, btnSearch4;
	JButton btnUser_Search1, btnUser_Search2, btnUser_Search3;
	JButton btnInput, btnReset, btnUser;
	JTextArea txtResult;
	DefaultTableModel model;
	JTable dbResult;
	JPanel pn1, pn2, userPanel;
	UpdatePanel updatePanel;
	ButtonPanel buttonPanel;
	int table = 0;
	int user = 0; // 0�� ������ 1�� �����
	int curRow = -1, curCol = -1;

	Connection conn;
	Statement stmt; // select
	PreparedStatement pstmt; // insert, delete
	ResultSet rs;

	String Driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul&useSSL=false";
	String userid = "madang";
	String pwd = "madang";

	public CarRentGUI() {
		super("18011583/�輺��/");
		init();
		connDB();
		initDB();
		setVisible(true);
		setBounds(500, 200, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() {
		super.setLayout(new FlowLayout());

		/* ����� ������ ��ȯ panel */
		btnUser = new JButton("������");
		userPanel = new JPanel();
		userPanel.add(btnUser);
		add(userPanel);

		/* panel 1 */
		btnCampComp = new JButton("Camping_Company");
		btnCampCar = new JButton("Camping_Car");
		btnCustomer = new JButton("Rent_Customer");
		btnRepairshop = new JButton("Repairshop");
		btnRentCar = new JButton("Rent_Car");
		btnCarCheck = new JButton("Car_Check");
		btnRepairList = new JButton("Repair_List");
		btnSearch1 = new JButton("�˻�1");
		btnSearch2 = new JButton("�˻�2");
		btnSearch3 = new JButton("�˻�3");
		btnSearch4 = new JButton("�˻�4");

		btnInput = new JButton("�Է�");
		btnReset = new JButton("�ʱ�ȭ");

		txtResult = new JTextArea();
		txtResult.setEditable(false);

		model = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		dbResult = new JTable(model);

		pn1 = new JPanel();
		pn1.add(btnCampComp);
		pn1.add(btnCampCar);
		pn1.add(btnCustomer);
		pn1.add(btnRepairshop);
		pn1.add(btnRentCar);
		pn1.add(btnCarCheck);
		pn1.add(btnRepairList);
		pn1.add(btnSearch1);
		pn1.add(btnSearch2);
		pn1.add(btnSearch3);
		pn1.add(btnSearch4);
		pn1.add(btnReset);

		btnRentList = new JButton("Camping_Car(�뿩����)");
		btnUser_Search1 = new JButton("�˻�1");
		btnUser_Search2 = new JButton("�˻�2");
		btnUser_Search3 = new JButton("�˻�3");
		pn2 = new JPanel();
		pn2.add(btnRentList);
		pn2.add(btnUser_Search1);
		pn2.add(btnUser_Search2);
		pn2.add(btnUser_Search3);

		updatePanel = new UpdatePanel();
		buttonPanel = new ButtonPanel();

		/* Frame - add */
		JScrollPane scrollPane = new JScrollPane(dbResult);

		add(pn1);
		add(scrollPane);
		add(updatePanel);
		add(buttonPanel);

		/* btn ActionListener */
		btnUser.addActionListener(this);

		btnCampComp.addActionListener(this);
		btnCampCar.addActionListener(this);
		btnCustomer.addActionListener(this);
		btnRepairshop.addActionListener(this);
		btnRentCar.addActionListener(this);
		btnCarCheck.addActionListener(this);
		btnRepairList.addActionListener(this);
		btnSearch1.addActionListener(this);
		btnSearch2.addActionListener(this);
		btnSearch3.addActionListener(this);
		btnSearch4.addActionListener(this);
		btnReset.addActionListener(this);
		btnRentList.addActionListener(this);
		btnUser_Search1.addActionListener(this);
		btnUser_Search2.addActionListener(this);
		btnUser_Search3.addActionListener(this);
		buttonPanel.btnInput.addActionListener(this);
		buttonPanel.btnDelete.addActionListener(this);
		buttonPanel.btnUpdate.addActionListener(this);
		buttonPanel.btnReturn.addActionListener(this);
		buttonPanel.btnRent.addActionListener(this);
		buttonPanel.btnRequest.addActionListener(this);

		/*** panel size ****/
		userPanel.setPreferredSize(new Dimension(780, 50));
		pn1.setPreferredSize(new Dimension(780, 80));
		pn2.setPreferredSize(new Dimension(780, 80));
		scrollPane.setPreferredSize(new Dimension(780, 300));
		updatePanel.setPreferredSize(new Dimension(780, 60));
		buttonPanel.setPreferredSize(new Dimension(780, 50));

		dbResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dbResult.addMouseListener(this);
	}

	public void resetDB() {
		String sql = "DROP TABLE IF EXISTS Repair_List";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		sql = "DROP TABLE IF EXISTS RepairShop";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		sql = "DROP TABLE IF EXISTS Car_Check";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		sql = "DROP TABLE IF EXISTS Car_Rent";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		sql = "DROP TABLE IF EXISTS Rent_Customer";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		sql = "DROP TABLE IF EXISTS Camping_Car";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		sql = "DROP TABLE IF EXISTS Camping_Company";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	public void createTable() {
		ArrayList<String> sql = new ArrayList<String>();

		sql.add("CREATE TABLE Camping_Company (\r\n" + "  compid      INTEGER PRIMARY KEY,\r\n"
				+ "  compname    VARCHAR(20),\r\n" + "  address   VARCHAR(40),\r\n" + "  phone       VARCHAR(20),\r\n"
				+ "  manager_name VARCHAR(10),\r\n" + "  manager_email VARCHAR(30)\r\n" + ");");
		sql.add("CREATE TABLE Camping_Car (\r\n" + "  carid      INTEGER,\r\n" + "  carname VARCHAR(20),\r\n"
				+ "  carno INTEGER,\r\n" + "  seat INTEGER,\r\n" + "  manufacturer VARCHAR(20),\r\n"
				+ "  manu_year INTEGER,\r\n" + "  drivingdistance INTEGER,\r\n" + "  rentcost INTEGER,\r\n"
				+ "  compid INTEGER,\r\n" + "  registdate date,\r\n"
				+ "  FOREIGN KEY (compid) REFERENCES Camping_Company(compid) ON DELETE CASCADE,\r\n"
				+ "  PRIMARY KEY(carid)\r\n" + ");\r\n" + "");
		sql.add("CREATE TABLE  Rent_Customer (\r\n" + "  license_no      INTEGER PRIMARY KEY,  \r\n"
				+ "  name        VARCHAR(20),\r\n" + "  address     VARCHAR(40),\r\n" + "  phone       VARCHAR(20),\r\n"
				+ "  email VARCHAR(30)\r\n" + ");");
		sql.add("CREATE TABLE Car_Rent (\r\n" + "  rentno INTEGER PRIMARY KEY,\r\n" + "  carid  INTEGER ,\r\n"
				+ "  license_no  INTEGER,\r\n" + "  compid INTEGER,\r\n" + "  rentdate DATE ,\r\n"
				+ "  rentalperiod INTEGER,\r\n" + "  charge INTEGER,\r\n" + "  paymentdeadline DATE,\r\n"
				+ "  billhistory VARCHAR(40),\r\n" + "  billhistorycost INTEGER,\r\n"
				+ "  FOREIGN KEY (carid) REFERENCES Camping_Car(carid) ON DELETE CASCADE,\r\n"
				+ "  FOREIGN KEY (compid) REFERENCES Camping_Company(compid) ON DELETE CASCADE,\r\n"
				+ "  FOREIGN KEY (license_no) REFERENCES Rent_Customer(license_no) ON DELETE CASCADE\r\n" + ");");
		sql.add("CREATE TABLE Car_Check (\r\n" + "  rentno INTEGER PRIMARY KEY,\r\n" + "  carid  INTEGER ,\r\n"
				+ "  explan_front VARCHAR(40),\r\n" + "  explan_left VARCHAR(40),\r\n"
				+ "  explan_right VARCHAR(40),\r\n" + "  explan_back VARCHAR(40),\r\n"
				+ "  repair_required VARCHAR(1),\r\n"
				+ "  FOREIGN KEY (rentno) REFERENCES Car_Rent(rentno) ON DELETE CASCADE,\r\n"
				+ "  FOREIGN KEY (carid) REFERENCES Camping_Car(carid) ON DELETE CASCADE\r\n" + ");");
		sql.add("CREATE TABLE Repairshop (\r\n" + "  shopid INTEGER PRIMARY KEY,\r\n" + "  shopname  VARCHAR(20),\r\n"
				+ "  address VARCHAR(40),\r\n" + "  phone VARCHAR(20),\r\n" + "  manager_name VARCHAR(10),\r\n"
				+ "  manager_email VARCHAR(30)\r\n" + ");");
		sql.add("CREATE TABLE Repair_List (\r\n" + "  repairno INTEGER PRIMARY KEY,\r\n" + "  carid  INTEGER,\r\n"
				+ "  shopid INTEGER,\r\n" + "  compid INTEGER,\r\n" + "  license_no INTEGER,\r\n"
				+ "  repairdetails VARCHAR(40),\r\n" + "  repairdate DATE,\r\n" + "  repaircost INTEGER,\r\n"
				+ "  paymentdeadline DATE,\r\n" + "  repairhistory VARCHAR(40),\r\n"
				+ "  FOREIGN KEY (carid) REFERENCES Camping_Car(carid) ON DELETE CASCADE,\r\n"
				+ "  FOREIGN KEY (compid) REFERENCES Camping_Company(compid) ON DELETE CASCADE,\r\n"
				+ "  FOREIGN KEY (shopid) REFERENCES Repairshop(shopid) ON DELETE CASCADE,\r\n"
				+ "  FOREIGN KEY (license_no) REFERENCES Rent_Customer(license_no) ON DELETE CASCADE\r\n" + ");");

		try {
			stmt = conn.createStatement();
			for (String s : sql) {
				stmt.execute(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void initDB() {
		resetDB();
		createTable();

		ArrayList<String> sql = new ArrayList<String>();

		sql.add("insert into camping_company value(1,\"abc���۴�\",\"����Ư���� ������ ��â��\",\"010-1234-0987\",\"������\",\"jina@naver.com\");");
		sql.add("insert into camping_company value(2,\"��������\",\"��õ������ ������ �۵���\",\"010-4322-2354\",\"�̿���\",\"woojin@naver.com\");");
		sql.add("insert into camping_company value(3,\"paper���۴�\",\"��⵵ �����Ͻ�\",\"010-2134-2343\",\"���̳�\",\"inna@naver.com\");");
		sql.add("insert into camping_company value(4,\"�ݹ����۴�\",\"��⵵ ������\",\"010-3426-8754\",\"���ֿ�\",\"juwo@naver.com\");");
		sql.add("insert into camping_company value(5,\"�������۴�\",\"����Ư���� ���ֵ�\",\"010-1237-4367\",\"������\",\"jia@naver.com\");");
		sql.add("insert into camping_company value(6,\"��������۴�\",\"���������� ���뵿\",\"010-2148-6534\",\"���Ƴ�\",\"ana@naver.com\");");
		sql.add("insert into camping_company value(7,\"������۴�\",\"������ ��ô�� ���ᵿ\",\"010-2363-3126\",\"�ֺ�\",\"rain@yahoo.com\");");
		sql.add("insert into camping_company value(8,\"���ַ�Ʈ\",\"������ ��õ�� ��ǵ�\",\"010-2354-2342\",\"������\",\"sungmin@gmail.com\");");
		sql.add("insert into camping_company value(9,\"�������۴�\",\"����Ư���� ������ ������\",\"010-2137-3478\",\"������\",\"jia123@naver.com\");");
		sql.add("insert into camping_company value(10,\"��ȣ��Ʈ\",\"����Ư���� ������ �Ǵ뵿\",\"010-2138-4326\",\"���ϸ�\",\"hari@naver.com\");");
		sql.add("insert into camping_company value(11,\"�������۴�\",\"�λ걤���� �ؿ�뱸\",\"010-8738-4347\",\"�ֿ���\",\"blackhole@naver.com\");");
		sql.add("insert into camping_company value(12,\"�ƿ���Ʈ\",\"�λ걤���� ������\",\"010-1238-5643\",\"������\",\"jinna@gmail.com\");");
		sql.add("insert into camping_company value(13,\"���̷�Ʈ\",\"����Ư���� ���屸\",\"010-6574-2346\",\"�輺��\",\"sunghun1998@naver.com\");");
		sql.add("insert into camping_company value(14,\"����Ʈ\",\"����Ư���� Ķ�����Ͼ���\",\"010-3246-2745\",\"��ȿ��\",\"hyo@naver.com\");");
		sql.add("insert into camping_company value(15,\"�߳뷻Ʈ\",\"��õ������ �����ε�\",\"010-2316-1235\",\"�̿��\",\"sik@naver.com\");");

		for (String s : sql) {
			try {
				pstmt = conn.prepareStatement(s);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		sql.clear();

		sql.add("insert into camping_car values(1,\"�غ���\",3241,6,\"����\",2010,18000,70000,3,\"2012-12-02\");");
		sql.add("insert into camping_car values(2,\"�ž���\",2312,4,\"����\",2011,20000,60000,1,\"2012-11-21\");");
		sql.add("insert into camping_car values(3,\"�Ͽ��\",7653,2,\"���\",2012,10000,60000,2,\"2013-03-02\");");
		sql.add("insert into camping_car values(4,\"Ƽ������\",1234,8,\"������\",2010,12000,80000,6,\"2014-9-2\");");
		sql.add("insert into camping_car values(5,\"������\",9384,8,\"���\",2014,6000,60000,7,\"2015-2-3\");");
		sql.add("insert into camping_car values(6,\"ȣ����\",4673,6,\"����\",2012,12000,120000,5,\"2015-4-5\");");
		sql.add("insert into camping_car values(7,\"�ڳ���\",3924,4,\"�����ٰ�\",2013,20000,90000,9,\"2014-7-4\");");
		sql.add("insert into camping_car values(8,\"���\",9863,4,\"����\",2011,3000,70000,11,\"2012-4-3\");");
		sql.add("insert into camping_car values(9,\"�⸰\",1324,6,\"���������\",2009,7000,60000,1,\"2012-12-1\");");
		sql.add("insert into camping_car values(10,\"���������\",4353,7,\"������\",2009,8000,80000,2,\"2011-2-3\");");
		sql.add("insert into camping_car values(11,\"������\",1235,9,\"����\",2014,20000,70000,3,\"2017-2-5\");");
		sql.add("insert into camping_car values(12,\"�ڽ���\",4732,6,\"�ֻ�\",2016,3000,80000,1,\"2018-12-3\");");
		sql.add("insert into camping_car values(13,\"����\",3627,4,\"ȥ��\",2010,21000,90000,14,\"2011-12-3\");");
		sql.add("insert into camping_car values(14,\"�ƹݶ�\",3843,2,\"BMW\",2010,13000,110000,2,\"2011-4-7\");");
		sql.add("insert into camping_car values(15,\"�ҳ�Ÿ\",4273,7,\"���\",2008,14000,90000,3,\"2010-4-5\");");

		try {
			stmt = conn.createStatement();
			for (String s : sql) {
				stmt.execute(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.clear();

		sql.add("insert into Rent_Customer value(1,\"�輺��\",\"��õ������ ������ �۵���\",\"010-9657-8412\",\"sunghun1998@naver.com\");");
		sql.add("insert into Rent_Customer value(2,\"��ȿ��\",\"����Ư���� ������ ���嵿\",\"010-1234-1234\",\"hyobbang@naver.com\");");
		sql.add("insert into Rent_Customer value(3,\"�̿��\",\"������ ��ô��\",\"010-3246-4321\",\"sik@naver.com\");");
		sql.add("insert into Rent_Customer value(4,\"������\",\"����Ư���� ��������\",\"010-1237-4363\",\"jinna@naver.com\");");
		sql.add("insert into Rent_Customer value(5,\"�츮��\",\"��õ������ �����̿���\",\"010-2365-4364\",\"rina@naver.com\");");
		sql.add("insert into Rent_Customer value(6,\"Ȳ����\",\"��õ������ �̽ð���\",\"010-2374-3765\",\"sojin@naver.com\");");
		sql.add("insert into Rent_Customer value(7,\"��Ƴ�\",\"����Ư���� ���챸\",\"010-2378-3263\",\"anaa@naver.com\");");
		sql.add("insert into Rent_Customer value(8,\"�ϳ���\",\"����Ư���� ������\",\"010-4263-4364\",\"nakyung@yahoo.com\");");
		sql.add("insert into Rent_Customer value(9,\"�̹���\",\"���������� ������\",\"010-4236-3263\",\"minji@naver.com\");");
		sql.add("insert into Rent_Customer value(10,\"�̼���\",\"�λ걤���� �ؿ�뱸\",\"010-4327-3425\",\"sojin@gmail.com\");");
		sql.add("insert into Rent_Customer value(11,\"�層��\",\"���ֱ����� ��Ƶ�\",\"010-3248-3474\",\"gang@naver.com\");");
		sql.add("insert into Rent_Customer value(12,\"���ھ�\",\"���ϵ� â����\",\"010-2348-7543\",\"jaa@gmail.com\");");
		sql.add("insert into Rent_Customer value(13,\"����\",\"��⵵ ������\",\"010-4328-5465\",\"bibibik@naver.com\");");
		sql.add("insert into Rent_Customer value(14,\"������\",\"��⵵ ��õ��\",\"010-3248-4374\",\"jinjin@naver.com\");");
		sql.add("insert into Rent_Customer value(15,\"������\",\"������ ���ؽ�\",\"010-3428-4537\",\"jingu@yahoo.com\");");

		try {
			stmt = conn.createStatement();
			for (String s : sql) {
				stmt.execute(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.clear();

		sql.add("insert into Car_Rent values(1,1,4,3,\"2019-1-23\",3,180000,\"2019-2-1\", \"X\",0);");
		sql.add("insert into Car_Rent values(2,15,3,3,\"2019-8-2\",4,220000,\"2019-8-11\",\"������\",20000);");
		sql.add("insert into Car_Rent values(3,14,5,2,\"2019-4-3\",2,120000,\"2019-4-16\",\"X\",0);");
		sql.add("insert into Car_Rent values(4,14,11,2,\"2019-5-2\",3,150000,\"2019-5-17\",\"���\",200000);");
		sql.add("insert into Car_Rent values(5,8,1,11,\"2019-3-1\",1,70000,\"2019-3-11\",\"������\",30000);");
		sql.add("insert into Car_Rent values(6,7,4,9,\"2019-2-22\",2,120000,\"2019-3-2\",\"X\",0);");
		sql.add("insert into Car_Rent values(7,14,10,2,\"2019-7-5\",7,420000,\"2019-7-17\",\"����\",30000);");
		sql.add("insert into Car_Rent values(8,2,12,1,\"2018-12-22\",4,200000,\"2019-1-1\",\"X\",0);");
		sql.add("insert into Car_Rent values(9,12,13,1,\"2019-12-20\",3,180000,\"2020-1-3\",\"���\",6000);");
		sql.add("insert into Car_Rent values(10,3,14,2,\"2019-11-22\",3,180000,\"2019-12-1\",\"X\",0);");
		sql.add("insert into Car_Rent values(11,15,15,3,\"2019-6-3\",3,210000,\"2019-6-16\",\"������\",40000);");
		sql.add("insert into Car_Rent values(12,7,2,9,\"2020-1-22\",3,180000,\"2020-2-1\",\"X\",0);");
		sql.add("insert into Car_Rent values(13,8,6,11,\"2019-8-21\",5,250000,\"2019-9-1\",\"���̽�ũ��\",10000);");
		sql.add("insert into Car_Rent values(14,11,8,3,\"2019-12-2\",3,180000,\"2019-12-11\",\"X\",0);");
		sql.add("insert into Car_Rent values(15,5,7,2,\"2019-6-24\",2,120000,\"2019-7-1\",\"X\",0);");
		sql.add("insert into Car_Rent values(16,6,1,5,\"2019-7-04\",3,125000,\"2019-7-10\",\"X\",0);");
		sql.add("insert into Car_Rent values(17,3,2,2,\"2019-5-20\",5,280000,\"2019-6-1\",\"����\",50000);");
		sql.add("insert into Car_Rent values(18,13,8,14,\"2019-1-3\",2,100000,\"2019-1-15\",\"X\",0);");
		sql.add("insert into Car_Rent values(19,11,3,3,\"2020-6-14\",6,250000,\"2020-6-30\",\"X\",0);");
		sql.add("insert into Car_Rent values(20,9,2,1,\"2019-9-15\",8,170000,\"2019-9-21\",\"������\",8000);");
		sql.add("insert into Car_Rent values(21,2,10,1,\"2020-3-17\",4,60000,\"2020-3-25\",\"X\",0);");

		try {
			stmt = conn.createStatement();
			for (String s : sql) {
				stmt.execute(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.clear();

		sql.add("insert into Car_Check values(1,1,\"Clear\",\"Clear\",\"Clear\",\"Clear\",\"Y\");");
		sql.add("insert into Car_Check values(2,15,\"Clear\",\"Door scratch\",\"Clear\",\"Clear\",\"Y\");");
		sql.add("insert into Car_Check values(3,14,\"Bumber scratch\",\"Door scratch\",\"Clear\",\"Clear\",\"Y\");");
		sql.add("insert into Car_Check values(4,14,\"Clear\",\"Clear\",\"Clear\",\"Clear\",\"N\");");
		sql.add("insert into Car_Check values(5,8,\"Clear\",\"Clear\",\"Clear\",\"Clear\",\"N\");");
		sql.add("insert into Car_Check values(6,7,\"Clear\",\"Clear\",\"Clear\",\"Clear\",\"Y\");");
		sql.add("insert into Car_Check values(7,14,\"Clear\",\"Window scratch\",\"Clear\",\"Clear\",\"Y\");");
		sql.add("insert into Car_Check values(8,2,\"Clear\",\"Clear\",\"Clear\",\"Clear\",\"N\");");
		sql.add("insert into Car_Check values(9,12,\"Clear\",\"Clear\",\"Door scratch\",\"Clear\",\"Y\");");
		sql.add("insert into Car_Check values(10,3,\"Clear\",\"Clear\",\"Clear\",\"Clear\",\"N\");");
		sql.add("insert into Car_Check values(15,5,\"Clear\",\"Clear\",\"Clear\",\"Clear\",\"N\");");
		sql.add("insert into Car_Check values(12,7,\"Clear\",\"Clear\",\"Clear\",\"Clear\",\"N\");");
		sql.add("insert into Car_Check values(13,8,\"Bumber Broken\",\"Window scratch\",\"Clear\",\"Clear\",\"Y\");");
		sql.add("insert into Car_Check values(14,11,\"Clear\",\"Clear\",\"Clear\",\"Clear\",\"N\");");
		sql.add("insert into Car_Check values(16,6,\"Clear\",\"Clear\",\"Clear\",\"Clear\",\"N\");");

		try {
			stmt = conn.createStatement();
			for (String s : sql) {
				stmt.execute(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.clear();

		sql.add("insert into Repairshop values(1,\"�����̳�\",\"��õ������ ������ �۵���\",\"02-3665-8412\",\"�輺��\",\"sunghun1998@naver.com\");");
		sql.add("insert into Repairshop values(2,\"����ī\",\"����Ư���� ������ ������\",\"02-3242-3241\",\"������\",\"hihi@naver.com\");");
		sql.add("insert into Repairshop values(3,\"ȿ���̳�\",\"����Ư���� ������ ���ڵ�\",\"02-3124-2435\",\"��ȿ��\",\"hyo@naver.com\");");
		sql.add("insert into Repairshop values(4,\"�ȳ��\",\"���������� ������\",\"033-2343-3124\",\"�Ⱦȳ�\",\"hi@naver.com\");");
		sql.add("insert into Repairshop values(5,\"��ī\",\"����Ư���� ������\",\"02-3245-7652\",\"������\",\"jihuuun@naver.com\");");
		sql.add("insert into Repairshop values(6,\"�巴����Ʈ\",\"��õ������ �Ժθ�ũ��\",\"032-2146-3859\",\"���Ʈ\",\"raper@naver.com\");");
		sql.add("insert into Repairshop values(7,\"�Ｚ��\",\"���ֱ����� �д籸\",\"023-4223-3584\",\"�̻Ｚ\",\"samsung@naver.com\");");
		sql.add("insert into Repairshop values(8,\"��ū�̳�\",\"����Ư���� �㺣��ũ\",\"02-2343-7659\",\"�̷縶\",\"luma@naver.com\");");
		sql.add("insert into Repairshop values(9,\"�����ػ�\",\"����Ư���� ���̵�����ũ\",\"02-3246-2355\",\"������\",\"jihun@naver.com\");");
		sql.add("insert into Repairshop values(10,\"���̵��\",\"������ ��ô�� ����\",\"033-2378-4537\",\"����\",\"dibi@naver.com\");");
		sql.add("insert into Repairshop values(11,\"����ī\",\"������ ���ʽ� �ڰ�ġ\",\"033-4326-7444\",\"�̼���\",\"sejong@naver.com\");");
		sql.add("insert into Repairshop values(12,\"��ε�\",\"�λ걤���� �߸����θ�ũ\",\"030-3246-4326\",\"����\",\"bro@naver.com\");");
		sql.add("insert into Repairshop values(13,\"�����Ѻ��\",\"���󳲵� �����Ͻ�\",\"031-2345-6532\",\"���̷�\",\"hiroo@naver.com\");");
		sql.add("insert into Repairshop values(14,\"�̵�߳�\",\"���ϵ� ������\",\"021-4324-4533\",\"�̵��\",\"dinya@yahoo.com\");");
		sql.add("insert into Repairshop values(15,\"���ó���\",\"��󳲵� �극��\",\"055-3324-3423\",\"��������\",\"tomorrow@naver.com\");");

		try {
			stmt = conn.createStatement();
			for (String s : sql) {
				stmt.execute(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.clear();

		sql.add("insert into Repair_List values(1,15,2,3,3,\"Window change\",\"2019-2-3\",120000,\"2019-5-1\",\"Nothing\");");
		sql.add("insert into Repair_List values(2,14,6,2,5,\"Bumper change\",\"2019-3-1\",400000,\"2019-6-1\",\"Nothing\");");
		sql.add("insert into Repair_List values(3,14,8,2,10,\"Window change\",\"2019-4-3\",200000,\"2019-7-1\",\"Nothing\");");
		sql.add("insert into Repair_List values(4,12,3,1,13,\"Window change\",\"2019-5-5\",100000,\"2019-8-1\",\"Nothing\");");

		sql.add("insert into Repair_List values(5,2,5,1,3,\"Window change\",\"2019-6-16\",300000,\"2019-5-11\",\"Nothing\");");
		sql.add("insert into Repair_List values(6,12,7,1,1,\"Bumper change\",\"2019-5-15\",100000,\"2019-8-14\",\"Nothing\");");
		sql.add("insert into Repair_List values(7,4,9,6,10,\"Bumper change\",\"2019-5-3\",130000,\"2019-7-13\",\"Nothing\");");

		sql.add("insert into Repair_List values(8,7,10,9,14,\"Window change\",\"2019-4-16\",400000,\"2019-8-21\",\"Nothing\");");
		sql.add("insert into Repair_List values(9,2,11,1,8,\"Window change\",\"2019-7-5\",300000,\"2019-9-11\",\"Nothing\");");
		sql.add("insert into Repair_List values(10,4,3,6,7,\"Window change\",\"2019-9-15\",200000,\"2019-7-21\",\"Nothing\");");

		sql.add("insert into Repair_List values(11,9,8,1,4,\"Window change\",\"2020-1-6\",120000,\"2019-8-4\",\"Nothing\");");
		sql.add("insert into Repair_List values(12,12,10,1,3,\"Bumper change\",\"2019-8-5\",1000000,\"2019-8-6\",\"Nothing\");");
		sql.add("insert into Repair_List values(13,9,1,1,12,\"Bumper change\",\"2019-7-18\",300000,\"2019-8-9\",\"Nothing\");");

		sql.add("insert into Repair_List values(14,13,2,14,4,\"Window change\",\"2019-9-6\",300000,\"2020-1-1\",\"Nothing\");");
		sql.add("insert into Repair_List values(15,11,3,3,11,\"Window change\",\"2019-4-15\",100000,\"2020-1-2\",\"Nothing\");");

		try {
			stmt = conn.createStatement();
			for (String s : sql) {
				stmt.execute(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.clear();

	}

	public void connDB() {
		try { // ����̹� �ε�
			Class.forName(Driver);
			System.out.println("����̹� �ε� �Ϸ�");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try { // �����ͺ��̽� ����
			conn = DriverManager.getConnection(url, userid, pwd);
			System.out.println("�����ͺ��̽� ���� �Ϸ�");
			updatePanel.conn = conn;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	public void closeDB() {
		try { // �����ͺ��̽� ���� ����
			if (conn != null) {
				conn.close();
				System.out.println("�����ͺ��̽� ���� ����");
			}
		} catch (Exception e3) {
			e3.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnUser) {
			curRow = -1;
			curCol = -1;
			if (user == 0) {
				btnUser.setText("�����");
				remove(pn1);
				add(pn2, 1);
				user = 1;
				model.setColumnCount(0);
				model.setRowCount(0);
				updatePanel.removeAll();
				buttonPanel.removeAll();
			} else if (user == 1) {
				btnUser.setText("������");
				remove(pn2);
				add(pn1, 1);
				user = 0;
				model.setColumnCount(0);
				model.setRowCount(0);
				updatePanel.removeAll();
				buttonPanel.removeAll();
			}
			repaint();
		} else if (e.getSource() == btnCampComp) {
			curRow = -1;
			curCol = -1;
			try {
				String sql = "SELECT * FROM Camping_Company";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				Object column[] = { "COMPID", "COMPNAME", "ADDRESS", "PHONE", "MANAGER NAME", "MANAGER EMAIL" };
				model.setDataVector(null, column);

				while (rs.next()) {
					Object[] data = { rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
							rs.getString(6) };
					model.addRow(data);
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			updatePanel.setVisible(true);
			buttonPanel.setVisible(true);
			updatePanel.campingComp();
			buttonPanel.update();
			table = 1;
			revalidate();
			repaint();
		} else if (e.getSource() == btnCampCar) {
			curRow = -1;
			curCol = -1;
			try {
				String sql = "SELECT * FROM Camping_Car";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				Object column[] = { "CARID", "CARNAME", "CARNO", "SEAT", "MANUFACTURER", "MANU YEAR",
						"DRIVING DISTANCE", "RENTCOST", "COMPID", "REGISTDATE" };
				model.setDataVector(null, column);

				while (rs.next()) {
					Object[] data = { rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
							rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getDate(10) };
					model.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			updatePanel.setVisible(true);
			buttonPanel.setVisible(true);
			updatePanel.campingCar();
			buttonPanel.update();
			table = 2;
			revalidate();
			repaint();
		} else if (e.getSource() == btnCustomer) {
			curRow = -1;
			curCol = -1;
			try {
				String sql = "SELECT * FROM Rent_Customer";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				Object column[] = { "LICENSE_NO", "NAME", "ADDRESS", "PHONE", "EMAIL" };
				model.setDataVector(null, column);

				while (rs.next()) {
					Object[] data = { rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5) };
					model.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			updatePanel.customer();
			buttonPanel.update();
			table = 3;
			revalidate();
			repaint();
		} else if (e.getSource() == btnRepairshop) {
			curRow = -1;
			curCol = -1;
			try {
				String sql = "SELECT * FROM Repairshop";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				Object column[] = { "SHOP ID", "SHOP NAME", "ADDRESS", "PHONE", "MANAGER NAME", "MANAGER EMAIL" };
				model.setDataVector(null, column);

				while (rs.next()) {
					Object[] data = { rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
							rs.getString(6) };
					model.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			updatePanel.repairshop();
			buttonPanel.update();
			table = 4;
			revalidate();
			repaint();
		} else if (e.getSource() == btnRentCar) {
			curRow = -1;
			curCol = -1;
			try {
				String sql = "SELECT * FROM Car_Rent";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				Object column[] = { "RENT NO", "CAR ID", "LICENSE NO", "COMP ID", "RENT DATE", "RENTAL PERIOD",
						"CHARGE", "PAYMENT DEADLINE", "BILL HISTORY", "BILL HISTORY COST" };
				model.setDataVector(null, column);

				while (rs.next()) {
					Object[] data = { rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getDate(5),
							rs.getInt(6), rs.getInt(7), rs.getDate(8), rs.getString(9), rs.getInt(10) };
					model.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			updatePanel.carCheck();
			buttonPanel.carReturn();
			table = 5;
			updatePanel.tf[0].setEnabled(false);
			updatePanel.tf[1].setEnabled(false);

			revalidate();
			repaint();
		} else if (e.getSource() == btnCarCheck) {
			curRow = -1;
			curCol = -1;
			try {
				String sql = "SELECT * FROM Car_Check c order by c.rentno";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				Object column[] = { "RENT NO", "CAR ID", "FRONT EX", "LEFT EX", "RIGHT EX", "BACK EX",
						"REPAIR REQUIRED" };
				model.setDataVector(null, column);

				while (rs.next()) {
					Object[] data = { rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
							rs.getString(6), rs.getString(7), };
					model.addRow(data);
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			updatePanel.repairList();
			buttonPanel.repairRequest();

			table = 6;

			updatePanel.tf[1].setEnabled(false);
			updatePanel.tf[3].setEnabled(false);
			updatePanel.tf[4].setEnabled(false);

			revalidate();
			repaint();
		} else if (e.getSource() == btnRepairList) {
			curRow = -1;
			curCol = -1;
			try {
				String sql = "SELECT * FROM Repair_List";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				Object column[] = { "REPAIR NO", "CAR ID", "SHOP ID", "COMP ID", "LICENSE NO", "REPAIR DETAILS",
						"REPAIR DATE", "REPAIR COST", "PAYMENT DEADLINE", "REPAIRHISTORY" };
				model.setDataVector(null, column);

				while (rs.next()) {
					Object[] data = { rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
							rs.getString(6), rs.getDate(7), rs.getInt(8), rs.getDate(9), rs.getString(10), };
					model.addRow(data);
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			updatePanel.repairList();
			buttonPanel.repairList();

			table = 7;

			updatePanel.tf[0].setEnabled(false);
			updatePanel.tf[1].setEnabled(false);
			updatePanel.tf[2].setEnabled(false);
			updatePanel.tf[3].setEnabled(false);
			updatePanel.tf[4].setEnabled(false);

			revalidate();
			repaint();

		} else if (e.getSource() == btnRentList) {
			curRow = -1;
			curCol = -1;
			try {
				String sql = "select *\r\n" + "from Camping_Car c\r\n"
						+ "where not exists (select 1 from Car_Rent r\r\n"
						+ "left join Car_Check cc on r.rentno = cc.rentno\r\n" + "where r.carid = c.carid\r\n"
						+ "and cc.rentno is null\r\n" + ")\r\n" + "order by carid;";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				Object column[] = { "CARID", "CARNAME", "CARNO", "SEAT", "MANUFACTURER", "MANU YEAR",
						"DRIVING DISTANCE", "RENTCOST", "COMPID", "REGISTDATE" };
				model.setDataVector(null, column);

				while (rs.next()) {
					Object[] data = { rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
							rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getDate(10) };
					model.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			updatePanel.rentCar();
			buttonPanel.carRent();

			table = 8;

			updatePanel.tf[1].setEnabled(false);
			updatePanel.tf[3].setEnabled(false);

			revalidate();
			repaint();
		}

		else if (e.getSource() == buttonPanel.btnInput) {
			updatePanel.insert(table);

		} else if (e.getSource() == buttonPanel.btnDelete) {
			if (curRow != -1) {
				updatePanel.delete(table, dbResult.getModel().getValueAt(curRow, 0));
			} else
				JOptionPane.showMessageDialog(null, "������ �����͸� ������ �ּ���.");
		} else if (e.getSource() == buttonPanel.btnUpdate) {
			if (curRow != -1) {
				updatePanel.update(table, dbResult.getModel().getValueAt(curRow, 0));
			} else
				JOptionPane.showMessageDialog(null, "������ �����͸� ������ �ּ���.");
		} else if (e.getSource() == buttonPanel.btnReturn) {
			if (curRow != -1) {
				updatePanel.carReutrn();
			} else
				JOptionPane.showMessageDialog(null, "��ȯ�� �����͸� ������ �ּ���.");
		}

		else if (e.getSource() == buttonPanel.btnRequest && dbResult.getModel().getValueAt(curRow, 6).equals("Y")) {
			if (curRow != -1) {
				updatePanel.repairRequest();
				curRow = -1;
				curCol = -1;
			} else
				JOptionPane.showMessageDialog(null, "��û�� �����͸� ������ �ּ���.");
		} else if (e.getSource() == buttonPanel.btnRequest && dbResult.getModel().getValueAt(curRow, 6).equals("N")) {

			JOptionPane.showMessageDialog(null, "������ ������ �߸��Ǿ����ϴ�.");
		}

		else if (e.getSource() == btnSearch1) {
			updatePanel.removeAll();
			buttonPanel.removeAll();
			String input = JOptionPane.showInputDialog("���ϴ� ��¥�� �Է����ּ���");
			if (input != null && (input.matches("\\d{4}-\\d{2}-\\d{2}"))) {
				try {
					System.out.println(input);
					String sql = "select *\r\n" + "from Car_Rent r\r\n"
							+ "where r.rentno not in (select c.rentno from Car_Check c) and date_format('" + input
							+ "', '%Y-%m-%d') > date_add(r.rentdate, interval rentalperiod day);";

					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);

					Object column[] = { "RENT NO", "CAR ID", "LICENSE NO", "COMP ID", "RENT DATE", "RENTAL PERIOD",
							"CHARGE", "PAYMENT DEADLINE", "BILL HISTORY", "BILL HISTORY COST" };
					model.setDataVector(null, column);

					while (rs.next()) {
						Object[] data = { rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getDate(5),
								rs.getInt(6), rs.getInt(7), rs.getDate(8), rs.getString(9), rs.getInt(10) };
						model.addRow(data);
					}

				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}

				revalidate();
				repaint();
			} else
				JOptionPane.showMessageDialog(null, "��¥����(yyyy-mm-dd)�� ���� �Է����ֽʽÿ�");

		} else if (e.getSource() == btnSearch2) {
			updatePanel.removeAll();
			buttonPanel.removeAll();
			try {
				String sql = "select cc.compid, cc.compname, coalesce(count(*), 0) as rental_count\r\n"
						+ "from Camping_Company cc\r\n" + "left join Car_Rent cr\r\n" + "on cc.compid = cr.compid\r\n"
						+ "group by cc.compid, cc.compname\r\n" + "order by 3 desc\r\n" + "limit 0, 10;";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				Object column[] = { "COMP ID", "COMP NAME", "TOTAL RENTAL COUNT" };
				model.setDataVector(null, column);

				while (rs.next()) {
					Object[] data = { rs.getInt(1), rs.getString(2), rs.getInt(3) };
					model.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			revalidate();
			repaint();
		} else if (e.getSource() == btnSearch3) {
			updatePanel.removeAll();
			buttonPanel.removeAll();
			try {
				String sql = "select rc.name, cr.license_no, count(*) as total_count, count(case when cc.repair_required = 'Y' then 1 end) as repair_count\r\n"
						+ "from Car_Rent cr, Car_Check cc, Rent_Customer rc\r\n" + "where cr.rentno = cc.rentno\r\n"
						+ "and cr.license_no = rc.license_no\r\n" + "group by cr.license_no\r\n" + "order by 4 desc;";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				Object column[] = { "NAME", "LICENSE NO", "TOTAL RENTAL COUNT", "REPAIR COUNT" };
				model.setDataVector(null, column);

				while (rs.next()) {
					Object[] data = { rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getInt(4) };
					model.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			revalidate();
			repaint();
		} else if (e.getSource() == btnSearch4) {
			updatePanel.removeAll();
			buttonPanel.removeAll();
			try {
				String sql = "select rs.shopid, rs.shopname, coalesce(sum(rl.repaircost), 0) as income\r\n"
						+ "from Repairshop rs\r\n" + "left join Repair_List rl\r\n" + "on rs.shopid = rl.shopid\r\n"
						+ "group by rs.shopid, rs.shopname\r\n" + "order by income desc;";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				Object column[] = { "SHOP ID", "SHOP NAME", "INCOME" };
				model.setDataVector(null, column);

				while (rs.next()) {
					Object[] data = { rs.getInt(1), rs.getString(2), rs.getInt(3) };
					model.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			revalidate();
			repaint();
		}

		else if (e.getSource() == buttonPanel.btnRent) {
			if (curRow != -1) {
				updatePanel.carRent();
			} else
				JOptionPane.showMessageDialog(null, "�뿩Ȱ �����͸� ������ �ּ���.");
		} else if (e.getSource() == btnUser_Search1) {

			curRow = -1;
			curCol = -1;
			try {
				// updatePanel.removeAll();
				// buttonPanel.removeAll();
				String input = JOptionPane.showInputDialog("Max_price�� �����ϼ���.");

				String sql = "select * from Camping_car c where c.rentcost <= " + input + " and "
						+ "c.carid not in (select r.carid from Car_Rent r "
						+ "where r.rentno not in (select rentno from Car_Check)) order by carid;\n";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				Object column[] = { "CARID", "CARNAME", "CARNO", "SEAT", "MANUFACTURER", "MANU YEAR",
						"DRIVING DISTANCE", "RENTCOST", "COMPID", "REGISTDATE" };
				model.setDataVector(null, column);

				while (rs.next()) {
					Object[] data = { rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
							rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getDate(10) };
					model.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			updatePanel.rentCar();
			buttonPanel.carRent();

			table = 8;

			updatePanel.tf[1].setEnabled(false);
			updatePanel.tf[3].setEnabled(false);
			updatePanel.tf[4].setEnabled(false);

			revalidate();
			repaint();

		} else if (e.getSource() == btnUser_Search2) {

			curRow = -1;
			curCol = -1;
			try {
				// updatePanel.removeAll();
				// buttonPanel.removeAll();
				String input = JOptionPane.showInputDialog("�� ��� ���ĸ� ���Ͻó���?");

				String sql = "select * from Camping_car c where c.manu_year >= " + input + " and "
						+ "c.carid not in (select r.carid from Car_Rent r "
						+ "where r.rentno not in (select rentno from Car_Check)) order by carid;\n";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				Object column[] = { "CARID", "CARNAME", "CARNO", "SEAT", "MANUFACTURER", "MANU YEAR",
						"DRIVING DISTANCE", "RENTCOST", "COMPID", "REGISTDATE" };
				model.setDataVector(null, column);

				while (rs.next()) {
					Object[] data = { rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
							rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getDate(10) };
					model.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			updatePanel.rentCar();
			buttonPanel.carRent();

			table = 8;

			updatePanel.tf[1].setEnabled(false);
			updatePanel.tf[3].setEnabled(false);
			updatePanel.tf[4].setEnabled(false);

			revalidate();
			repaint();

		} else if (e.getSource() == btnUser_Search3) {

			curRow = -1;
			curCol = -1;
			try {
				// updatePanel.removeAll();
				// buttonPanel.removeAll();
				String input = JOptionPane.showInputDialog("����Ÿ��� �� km ���ϸ� ���Ͻó���?");

				String sql = "select * from Camping_car c where c.drivingdistance <= " + input + " and "
						+ "c.carid not in (select r.carid from Car_Rent r "
						+ "where r.rentno not in (select rentno from Car_Check)) order by carid;\n";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				Object column[] = { "CARID", "CARNAME", "CARNO", "SEAT", "MANUFACTURER", "MANU YEAR",
						"DRIVING DISTANCE", "RENTCOST", "COMPID", "REGISTDATE" };
				model.setDataVector(null, column);

				while (rs.next()) {
					Object[] data = { rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
							rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getDate(10) };
					model.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			updatePanel.rentCar();
			buttonPanel.carRent();

			table = 8;

			updatePanel.tf[1].setEnabled(false);
			updatePanel.tf[3].setEnabled(false);
			updatePanel.tf[4].setEnabled(false);

			revalidate();
			repaint();

		}

		else if (e.getSource() == btnReset) {
			model.setColumnCount(0);
			model.setRowCount(0);
			initDB();
		}
	}

	public static void main(String[] args) {
		CarRentGUI md = new CarRentGUI();
		md.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				md.closeDB();
			}
		});
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		curRow = dbResult.getSelectedRow();
		curCol = dbResult.getSelectedColumn();
		if (table == 5) {
			String rentno = dbResult.getModel().getValueAt(curRow, 0).toString();
			String carid = dbResult.getModel().getValueAt(curRow, 1).toString();

			updatePanel.tf[0].setText(rentno);
			updatePanel.tf[0].setDisabledTextColor(Color.black);

			updatePanel.tf[1].setText(carid);
			updatePanel.tf[1].setDisabledTextColor(Color.black);
		}

		if (table == 6) {

			try {
				String rentno = dbResult.getModel().getValueAt(curRow, 0).toString();

				String sql = "select carid, compid, license_no from Car_Rent where rentno = " + rentno + ";";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				String carid = null;
				String compid = null;
				String license_no = null;
				while (rs.next()) {
					carid = Integer.toString(rs.getInt(1));
					compid = Integer.toString(rs.getInt(2));
					license_no = Integer.toString(rs.getInt(3));
				}

				updatePanel.tf[1].setText(carid);
				updatePanel.tf[1].setDisabledTextColor(Color.black);

				updatePanel.tf[3].setText(compid);
				updatePanel.tf[3].setDisabledTextColor(Color.black);

				updatePanel.tf[4].setText(license_no);
				updatePanel.tf[4].setDisabledTextColor(Color.black);

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (table == 7) {
			String rentno = dbResult.getModel().getValueAt(curRow, 0).toString();
			String carid = dbResult.getModel().getValueAt(curRow, 1).toString();
			String shopid = dbResult.getModel().getValueAt(curRow, 2).toString();
			String compid = dbResult.getModel().getValueAt(curRow, 3).toString();
			String license_no = dbResult.getModel().getValueAt(curRow, 4).toString();

			updatePanel.tf[0].setText(rentno);
			updatePanel.tf[0].setDisabledTextColor(Color.black);

			updatePanel.tf[1].setText(carid);
			updatePanel.tf[1].setDisabledTextColor(Color.black);

			updatePanel.tf[2].setText(shopid);
			updatePanel.tf[2].setDisabledTextColor(Color.black);

			updatePanel.tf[3].setText(compid);
			updatePanel.tf[3].setDisabledTextColor(Color.black);

			updatePanel.tf[4].setText(license_no);
			updatePanel.tf[4].setDisabledTextColor(Color.black);

		}

		if (table == 8) {
			String carid = dbResult.getModel().getValueAt(curRow, 0).toString();
			String compid = dbResult.getModel().getValueAt(curRow, 8).toString();

			updatePanel.tf[1].setText(carid);
			updatePanel.tf[1].setDisabledTextColor(Color.black);

			updatePanel.tf[3].setText(compid);
			updatePanel.tf[3].setDisabledTextColor(Color.black);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}