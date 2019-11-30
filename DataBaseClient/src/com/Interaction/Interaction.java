package com.Interaction;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.DB.DataBase;
import com.Statement.State;
import com.structs.adminStruct;
import com.structs.animalStruct;
import com.structs.healthStruct;
import com.structs.shelterStruct;
import com.structs.userStruct;
import com.structs.vaccineStruct;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.awt.Color;
import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import java.awt.BorderLayout;
import java.awt.Button;

public class Interaction {

	private JFrame frame;
	
	private static Point originPoint = new Point();
	
	private DataBase mydb;
	private JTabbedPane adminPane;
	private JTabbedPane userPane;
	private JScrollPane adminInfoPane;
	private JScrollPane userInfoPane;
	private JScrollPane animalInfoPane;
	private JScrollPane shelterInfoPane;
	private JScrollPane healthInfoPane;
	private JScrollPane vaccineInfoPane;
	private JScrollPane animalInfoPane_U;
	private JScrollPane shelterInfo_U;
	private JScrollPane healthInfo_U;
	private JScrollPane vaccineInfo_U;
	
	private String UserName;
	private Object[] curHeaders=null;
	private JTable universal_table;
	private JTable input_table;
	private String changeTableName;
	private boolean isCurTableChanged=false;
	
	private JTabbedPane tabbedPane;
	private JLabel timeLabel;
	private JButton updateBtn;
	private JButton searchBtn;
	private JButton exitBtn;
	private JScrollPane inputPane;
	private JButton insertBtn;
	private JButton deleteBtn;
	private JButton uploadBtn;
	
	private String fileURL;
	
	public Frame getFrame() 
	{
		return frame;
	}
	
	//设置数据库操作对象
	public void setMyDB(DataBase mydb) 
	{
		this.mydb = mydb;
	}

    //设置系统样式
    public void setWinStyle()
    {
        try 
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) 
        {
            e.printStackTrace();
        }

    }
    
	
	public Interaction(String UserType,String UserName) 
	{
		this.UserName = UserName;
		initialize();
		
		if (UserType=="Admin") 
		{
			tabbedPane.setSelectedIndex(0);
		} 
		else 
		{
			tabbedPane.setSelectedIndex(1);
			tabbedPane.setEnabledAt(0, false);
		}
		this.setWinStyle();
		this.setTimeLabel(timeLabel);
		
		insertBtn = new JButton("");
		insertBtn.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				changeTable("I");
			}
		});
		insertBtn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		insertBtn.setBounds(103, 451, 47, 43);
		ImageIcon image = new ImageIcon("./ico/insert.png");
		image.setImage(image.getImage().getScaledInstance(insertBtn.getWidth(),insertBtn.getHeight(),Image.SCALE_DEFAULT ));
		insertBtn.setIcon(image);
		frame.getContentPane().add(insertBtn);
		
		deleteBtn = new JButton("");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTable("D");
			}
		});
		deleteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		deleteBtn.setBounds(215, 451, 47, 43);
		image = new ImageIcon("./ico/delete.png");
		image.setImage(image.getImage().getScaledInstance(deleteBtn.getWidth(),deleteBtn.getHeight(),Image.SCALE_DEFAULT ));
		deleteBtn.setIcon(image);
		frame.getContentPane().add(deleteBtn);
		
		updateBtn = new JButton("");
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTable("U");
			}
		});
		updateBtn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		updateBtn.setBounds(342, 451, 47, 43);
		image = new ImageIcon("./ico/update.png");
		image.setImage(image.getImage().getScaledInstance(updateBtn.getWidth(),updateBtn.getHeight(),Image.SCALE_DEFAULT ));
		updateBtn.setIcon(image);
		frame.getContentPane().add(updateBtn);
		
		searchBtn = new JButton("");
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = "";
				if (changeTableName=="adminInfoPane") 
				{
					text  = "Please Input the Admin Name：";
				}
				else if (changeTableName=="userInfoPane") 
				{
					text  = "Please Input the User Name：";
				}
				else if (changeTableName=="animalInfoPane")
				{
					text  = "Please Input the Animal Number：";
				}
				else if (changeTableName=="shelterInfoPane") 
				{
					text  = "Please Input the Shelter  Name：";
				}
				else if (changeTableName=="healthInfoPane") 
				{
					text  = "Please Input the Health Information ID：";
				}
				else if (changeTableName=="vaccineInfoPane") 
				{
					text  = "Please Input the Vaccine ID：";
				}
				JOptionPane optionPane = new JOptionPane(text, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, null, text);
			    optionPane.setWantsInput(true);
			    JDialog dialog = optionPane.createDialog(null, "输入提示框");
			    dialog.setLocation(10, 20);
			    dialog.setVisible(true);
			    String flag = (String)optionPane.getInputValue();
			    
			    System.out.println(flag);
			    
			    if(flag==null||flag.equals("")) 
			    {
			    	System.out.println("输入为空");
			    	return;
			    }
			    
			    Object[][] content = new Object[1][curHeaders.length];
			    input_table = new JTable(content,curHeaders);
			    
				if (changeTableName=="adminInfoPane") 
				{
				    adminStruct ast = new adminStruct();
				    mydb.SearchAdmin(flag, ast);
				    input_table.setValueAt(ast.getAdminID(), 0, 0);
				    input_table.setValueAt(ast.getAdminName(), 0, 1);
				    input_table.setValueAt(ast.getAdminPassWord(), 0, 2);
				}
				else if (changeTableName=="userInfoPane") 
				{
					userStruct ust = new userStruct();
					mydb.SearchUser(flag, ust);
				    input_table.setValueAt(ust.getUserID(), 0, 0);
				    input_table.setValueAt(ust.getUserNameString(), 0, 1);
				    input_table.setValueAt(ust.getPassWord(), 0, 2);
				    input_table.setValueAt(ust.getEmailAddress(), 0, 3);
				    input_table.setValueAt(ust.getPhoneNumber(), 0, 4);
				    input_table.setValueAt(ust.getShelterID(), 0, 5);
				}
				else if (changeTableName=="animalInfoPane")
				{
					animalStruct animalst = new animalStruct();
					mydb.SearchAnimal(flag, animalst);
				    input_table.setValueAt(animalst.getAnimalID(), 0, 0);
				    input_table.setValueAt(animalst.getAnimalNumber(),0, 1);
				    input_table.setValueAt(animalst.getAnimalName(), 0, 2);
				    input_table.setValueAt(animalst.getSpeciesKind(), 0, 3);
				    input_table.setValueAt(animalst.getSex(), 0, 4);
				    input_table.setValueAt(animalst.getAge(), 0, 5);
				    input_table.setValueAt(animalst.getShelterID(), 0, 6);
				    input_table.setValueAt(animalst.getImage(), 0, 7);
				    
				    
				    JFrame pictureWin = new JFrame();

				    ImageIcon img = new ImageIcon(animalst.getImage());
				    JLabel pictureLabel = new JLabel(img);
				    pictureWin.getLayeredPane().add(pictureLabel,new Integer(Integer.MIN_VALUE));
				    pictureLabel.setBounds(0,0,img.getIconWidth(),img.getIconHeight());
				    pictureWin.setSize(500, 300);
				    
				    Container cp = pictureWin.getContentPane();
				    cp.setLayout(new BorderLayout());
				    ((JPanel)cp).setOpaque(false);
				    pictureLabel.repaint();
				    pictureWin.repaint();
				    pictureWin.setVisible(true);
				    
				}
				else if (changeTableName=="shelterInfoPane") 
				{
					shelterStruct shelterst = new shelterStruct();
					mydb.SearchShelter(flag, shelterst);
				    input_table.setValueAt(shelterst.getShelterID(), 0, 0);
				    input_table.setValueAt(shelterst.getShelterName(),0, 1);
				    input_table.setValueAt(shelterst.getShelterAddress(), 0, 2);
				    input_table.setValueAt(shelterst.getPostalCode(), 0, 3);
				    input_table.setValueAt(shelterst.getTotalRoomNums(), 0, 4);
				    input_table.setValueAt(shelterst.getRemainingRoomNums(), 0, 5);
				    input_table.setValueAt(shelterst.getNote(), 0, 6);
				}
				else if (changeTableName=="healthInfoPane") 
				{
					healthStruct healthst = new healthStruct();
					mydb.SearchHealthInfo(flag, healthst);
				    input_table.setValueAt(healthst.getHealthInfoID(), 0, 0);
				    input_table.setValueAt(healthst.getAnimalID(),0, 1);
				    input_table.setValueAt(healthst.getUserID(), 0, 2);
				    input_table.setValueAt(healthst.getHealthInfo(), 0, 3);
				    input_table.setValueAt(healthst.getCheckDate(), 0, 4);
				    input_table.setValueAt(healthst.getNote(), 0, 5);
				}
				else if (changeTableName=="vaccineInfoPane") 
				{
					vaccineStruct vaccinest = new vaccineStruct();
					mydb.SearchVaccineInfo(flag, vaccinest);
				    input_table.setValueAt(vaccinest.getVaccineID(), 0, 0);
				    input_table.setValueAt(vaccinest.getAnimalID(),0, 1);
				    input_table.setValueAt(vaccinest.getUserID(), 0, 2);
				    input_table.setValueAt(vaccinest.getVaccineName(), 0, 3);
				    input_table.setValueAt(vaccinest.getInoculateDate(), 0, 4);
				    input_table.setValueAt(vaccinest.getNote(), 0, 5);
				}
				input_table.setRowHeight(25);
				inputPane.setViewportView(input_table);
				inputPane.repaint();
			}
		});
		searchBtn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		searchBtn.setBounds(468, 451, 47, 43);
		image = new ImageIcon("./ico/search.png");
		image.setImage(image.getImage().getScaledInstance(searchBtn.getWidth(),searchBtn.getHeight(),Image.SCALE_DEFAULT ));
		searchBtn.setIcon(image);
		frame.getContentPane().add(searchBtn);
		
		exitBtn = new JButton("");
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitBtn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		exitBtn.setBounds(573, 451, 47, 43);
		image = new ImageIcon("./ico/exit.png");
		image.setImage(image.getImage().getScaledInstance(exitBtn.getWidth(),exitBtn.getHeight(),Image.SCALE_DEFAULT ));
		exitBtn.setIcon(image);
		frame.getContentPane().add(exitBtn);
		
		Button addBtn = new Button("+");
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertBtn.setEnabled(true);
				updateBtn.setEnabled(true);
				deleteBtn.setEnabled(true);
				if (input_table==null) 
				{
					input_table = new JTable();
				}
				if (curHeaders==null) 
				{
					return;
				}
				int colCount = curHeaders.length;
				int rowCount =isCurTableChanged==true?1:input_table.getRowCount()+1;
				Object[][]tmp=new Object[rowCount][colCount];
				for (int i = 0; i < rowCount; i++) 
				{
					for (int j = 0; j < colCount; j++) 
					{
						tmp[i][j] = "";
					}
				}
				input_table = new JTable(tmp,curHeaders);
				input_table.setRowHeight(25);
				input_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
				TableColumnModel cm = input_table.getColumnModel();
				for (int i = 0; i < curHeaders.length; i++) 
				{
					TableColumn column = cm.getColumn(i);
					column.setMinWidth(110);
					column.setMaxWidth(110);
				}
				inputPane.setViewportView(input_table);
				inputPane.repaint();
				isCurTableChanged = false;
				return;
			}
		});
		addBtn.setBounds(64, 368, 28, 28);
		frame.getContentPane().add(addBtn);
		
		Button delBtn = new Button("-");
		delBtn.setBounds(64, 413, 28, 28);
		frame.getContentPane().add(delBtn);
		
		inputPane = new JScrollPane();
		inputPane.setName("inputPane");
		inputPane.setBounds(108, 368, 525, 73);
		inputPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(inputPane);
		
		frame.setUndecorated(true);
		
		frame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) 
			{
				originPoint = e.getPoint();
			}
		});
		
		frame.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				Point p = frame.getLocation();
				frame.setLocation(p.x+e.getX()-originPoint.x,p.y+e.getY()-originPoint.y);
			}
		});
		
		uploadBtn = new JButton("");
		uploadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);
				chooser.setCurrentDirectory(new File("."));
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Allowed File", "png","jpg","bmp","gif","svg");
				chooser.setFileFilter(filter);
				chooser.showOpenDialog(null);
				
				File file = chooser.getSelectedFile();
				fileURL = file.getAbsolutePath();
				System.out.println(fileURL);
			}
		});
		uploadBtn.setBounds(635, 385, 25, 25);
		image = new ImageIcon("./ico/upload.png");
		image.setImage(image.getImage().getScaledInstance(uploadBtn.getWidth(),uploadBtn.getHeight(),Image.SCALE_DEFAULT ));
		uploadBtn.setIcon(image);
		frame.getContentPane().add(uploadBtn);
		
		this.insertBtn.setEnabled(false);
		this.updateBtn.setEnabled(false);
		this.deleteBtn.setEnabled(false);
		this.uploadBtn.setEnabled(false);

		
	}
	
	private void setTimeLabel(JLabel timeLabel) 
	{
		 final JLabel varTime = timeLabel;   
	        Timer timeAction = new Timer(1000, new ActionListener() {          
	  
	            public void actionPerformed(ActionEvent e) {       
	                long timemillis = System.currentTimeMillis();   
	                //转换日期显示格式   
	                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
	                varTime.setText(df.format(new Date(timemillis)));   
	            }      
	        });            
	        timeAction.start();
	}
	
	private void setTable(String tableName) 
	{
		universal_table = new JTable();
		Object[]headers=null;
		Object[][]tableContent=null;
		if (tableName=="adminInfoPane") 
		{
			ArrayList<adminStruct>tableList = mydb.getAdminTable();
			headers = adminStruct.toArray();
			int ColumnSize = headers.length;
			curHeaders = headers;
			isCurTableChanged = true;
			
			int RowSize = tableList.size();
			if (RowSize==0) 
			{
				System.out.println("表内无内容");
				return;
			}
			tableContent = new Object[RowSize][ColumnSize];
			for (int i=0;i<RowSize;i++) 
			{
				tableContent[i][0] = tableList.get(i).getAdminID();
				tableContent[i][1] = tableList.get(i).getAdminName();
				tableContent[i][2] = tableList.get(i).getAdminPassWord();
			}
		}
		else if (tableName=="userInfoPane") 
		{
			ArrayList<userStruct>tableList =mydb.getUserTable();
			headers = userStruct.toArray();
			int ColumnSize = headers.length;
			curHeaders = headers;
			isCurTableChanged = true;
			int RowSize = tableList.size();
			if (RowSize==0) 
			{
				System.out.println("表内无内容");
				return;
			}
			tableContent = new Object[RowSize][ColumnSize];
			for (int i=0;i<RowSize;i++) 
			{
				tableContent[i][0] = tableList.get(i).getUserID();
				tableContent[i][1] = tableList.get(i).getUserNameString();
				tableContent[i][2] = tableList.get(i).getPassWord();
				tableContent[i][3] = tableList.get(i).getEmailAddress();
				tableContent[i][4] = tableList.get(i).getPhoneNumber();
				tableContent[i][5] = tableList.get(i).getShelterID();
			}
		}
		else if (tableName=="animalInfoPane") 
		{
			uploadBtn.setEnabled(true);
			ArrayList<animalStruct>tableList = mydb.getAnimalTable();
			headers = animalStruct.toArray();
			int ColumnSize = headers.length;
			curHeaders = headers;
			isCurTableChanged = true;
			int RowSize = tableList.size();
			if (RowSize==0) 
			{
				System.out.println("表内无内容");
				return;
			}
			tableContent = new Object[RowSize][ColumnSize];
			for (int i=0;i<RowSize;i++) 
			{
				tableContent[i][0] = tableList.get(i).getAnimalID();
				tableContent[i][1] = tableList.get(i).getAnimalNumber();
				tableContent[i][2] = tableList.get(i).getAnimalName();
				tableContent[i][3] = tableList.get(i).getSpeciesKind();
				tableContent[i][4] = tableList.get(i).getSex();
				tableContent[i][5] = tableList.get(i).getAge();
				tableContent[i][6] = tableList.get(i).getImage();
				tableContent[i][7] = tableList.get(i).getShelterID();
			}
		}
		else if (tableName=="shelterInfoPane") 
		{
			ArrayList<shelterStruct>tableList=mydb.getShelterTable();
			headers =shelterStruct.toArray();
			int ColumnSize = headers.length;
			curHeaders = headers;
			isCurTableChanged = true;
			int RowSize = tableList.size();
			if (RowSize==0) 
			{
				System.out.println("表内无内容");
				return;
			}
			tableContent = new Object[RowSize][ColumnSize];
			for (int i=0;i<RowSize;i++) 
			{
				tableContent[i][0] = tableList.get(i).getShelterID();
				tableContent[i][1] = tableList.get(i).getShelterName();
				tableContent[i][2] = tableList.get(i).getShelterAddress();
				tableContent[i][3] = tableList.get(i).getPostalCode();
				tableContent[i][4] = tableList.get(i).getTotalRoomNums();
				tableContent[i][5] = tableList.get(i).getRemainingRoomNums();
				tableContent[i][6] = tableList.get(i).getNote();
			}
		}
		else if (tableName=="healthInfoPane") 
		{
			ArrayList<healthStruct>tableList=mydb.getHealthTable();
			headers =healthStruct.toArray();
			int ColumnSize = headers.length;
			curHeaders = headers;
			isCurTableChanged = true;
			int RowSize = tableList.size();
			if (RowSize==0) 
			{
				System.out.println("表内无内容");
				return;
			}
			tableContent = new Object[RowSize][ColumnSize];
			for (int i=0;i<RowSize;i++) 
			{
				tableContent[i][0] = tableList.get(i).getHealthInfoID();
				tableContent[i][1] = tableList.get(i).getAnimalID();
				tableContent[i][2] = tableList.get(i).getUserID();
				tableContent[i][3] = tableList.get(i).getHealthInfo();
				tableContent[i][4] = tableList.get(i).getCheckDate();
				tableContent[i][5] = tableList.get(i).getNote();
			}
		}
		else if (tableName=="vaccineInfoPane") 
		{
			ArrayList<vaccineStruct>tableList=mydb.getVaccineTable();
			headers =vaccineStruct.toArray();
			int ColumnSize = headers.length;
			curHeaders = headers;
			isCurTableChanged = true;
			int RowSize = tableList.size();
			if (RowSize==0) 
			{
				System.out.println("表内无内容");
				return;
			}
			tableContent = new Object[RowSize][ColumnSize];
			for (int i=0;i<RowSize;i++) 
			{
				tableContent[i][0] = tableList.get(i).getVaccineID();
				tableContent[i][1] = tableList.get(i).getAnimalID();
				tableContent[i][2] = tableList.get(i).getUserID();
				tableContent[i][3] = tableList.get(i).getVaccineName();
				tableContent[i][4] = tableList.get(i).getInoculateDate();
				tableContent[i][5] = tableList.get(i).getNote();
			}
		}
		universal_table = new JTable(tableContent,headers);
		universal_table.setRowHeight(25);
		if (tableName=="shelterInfoPane") 
		{
			universal_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
			TableColumnModel cm = universal_table.getColumnModel();
			for (int i = 0; i < tableContent[0].length; i++) 
			{
				TableColumn column = cm.getColumn(i);
				column.setMinWidth(100);
				column.setMaxWidth(100);
			}
		}
	}
	
	private void changeTable(String changeFlag) 
	{
		if (changeFlag.equals("I")) 
		{
			if (universal_table==null||input_table==null) 
			{
				System.out.println("表为空");
				return;
			}
			if (changeTableName.equals("adminInfoPane")) 
			{
				String adminID = (String)input_table.getValueAt(0, 0);
				String adminName = (String)input_table.getValueAt(0, 1);
				String adminPassword = (String)input_table.getValueAt(0, 2);
				
				System.out.println("adminID："+adminID+" admin Name："+adminName+" Password："+adminPassword);
				
				State flag = mydb.ChangeAdminInfo("I", adminID, adminName, adminPassword);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Insert data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("userInfoPane")) 
			{
				String UserID = (String)input_table.getValueAt(0, 0);
				String UserName = (String)input_table.getValueAt(0, 1);
				String PassWord = (String)input_table.getValueAt(0, 2);
				String EmailAddress = (String)input_table.getValueAt(0, 3);
				String PhoneNumber =(String) input_table.getValueAt(0, 4);
				String ShelterID = (String)input_table.getValueAt(0, 5);
				
				System.out.println(UserID+" "+UserName+" "+PassWord+" "+EmailAddress+" "+PhoneNumber+" "+ShelterID);
				
				State flag = mydb.ChangeUserInfo("I", UserID, UserName, PassWord, EmailAddress, PhoneNumber, ShelterID);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Insert data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("animalInfoPane")) 
			{
				String AnimalID = (String)input_table.getValueAt(0, 0);
				String AnimalNumber = (String)input_table.getValueAt(0, 1);
				String AnimalName = (String)input_table.getValueAt(0, 2);
				String SpeciesKind = (String)input_table.getValueAt(0, 3);
				int Sex =Integer.parseInt((String)input_table.getValueAt(0, 4));
				int Age =Integer.parseInt((String)input_table.getValueAt(0, 5));
				String ShelterID = (String)input_table.getValueAt(0, 7);
				String ImageURL = fileURL;
				
				State flag = mydb.ChangeAnimalInfo("I", AnimalID, AnimalNumber, AnimalName, SpeciesKind, Sex, Age, ShelterID, ImageURL);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Insert data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("shelterInfoPane")) 
			{
				String ShelterID = (String)input_table.getValueAt(0, 0);
				String ShelterName = (String)input_table.getValueAt(0, 1);
				String ShelterAddr = (String)input_table.getValueAt(0, 2);
				int PostalCode =Integer.parseInt((String)input_table.getValueAt(0, 3));
				int TotalRoomNums =Integer.parseInt((String)input_table.getValueAt(0, 4));
				int RemainingRoomNums =Integer.parseInt((String)input_table.getValueAt(0, 5));
				String Note = (String)input_table.getValueAt(0, 6);

				State flag = mydb.ChangeShelterInfo("I", ShelterID, ShelterName, ShelterAddr, PostalCode, TotalRoomNums, RemainingRoomNums, Note);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Insert data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("healthInfoPane")) 
			{
				String HealthInfoID = (String)input_table.getValueAt(0, 0);
				String AnimalID = (String)input_table.getValueAt(0, 1);
				String UserID = (String)input_table.getValueAt(0, 2);
				String HealthInfo = (String)input_table.getValueAt(0, 3);
				String checkDate =(String) input_table.getValueAt(0, 4);
				String Note = (String)input_table.getValueAt(0, 5);

				State flag = mydb.ChangeHealthInfo("I", HealthInfoID, AnimalID, UserID, HealthInfo, checkDate, Note);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Insert data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("vaccineInfoPane")) 
			{
				String VaccineID = (String)input_table.getValueAt(0, 0);
				String AnimalID = (String)input_table.getValueAt(0, 1);
				String UserID = (String)input_table.getValueAt(0, 2);
				String VaccineName = (String)input_table.getValueAt(0, 3);
				String InoculateDate =(String) input_table.getValueAt(0, 4);
				String Note = (String)input_table.getValueAt(0, 5);

				State flag = mydb.ChangeVaccineInfo("I", VaccineID, AnimalID, UserID, VaccineName, InoculateDate, Note);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Insert data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
		}
		if (changeFlag.equals("D")) 
		{
			if (universal_table==null||input_table==null) 
			{
				System.out.println("表为空");
				return;
			}
			if (changeTableName.equals("adminInfoPane")) 
			{
				String adminID = (String)input_table.getValueAt(0, 0);
				String adminName = (String)input_table.getValueAt(0, 1);
				String adminPassword = (String)input_table.getValueAt(0, 2);
				
				System.out.println("adminID："+adminID+" admin Name："+adminName+" Password："+adminPassword);
				
				State flag = mydb.ChangeAdminInfo("D", adminID, adminName, adminPassword);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Delete data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("userInfoPane")) 
			{
				String UserID = (String)input_table.getValueAt(0, 0);
				String UserName = (String)input_table.getValueAt(0, 1);
				String PassWord = (String)input_table.getValueAt(0, 2);
				String EmailAddress = (String)input_table.getValueAt(0, 3);
				String PhoneNumber =(String) input_table.getValueAt(0, 4);
				String ShelterID = (String)input_table.getValueAt(0, 5);
				
				System.out.println(UserID+" "+UserName+" "+PassWord+" "+EmailAddress+" "+PhoneNumber+" "+ShelterID);
				
				State flag = mydb.ChangeUserInfo("D", UserID, UserName, PassWord, EmailAddress, PhoneNumber, ShelterID);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Delete data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("animalInfoPane")) 
			{
				String AnimalID = (String)input_table.getValueAt(0, 0);
				String AnimalNumber = (String)input_table.getValueAt(0, 1);
				String AnimalName = (String)input_table.getValueAt(0, 2);
				String SpeciesKind = (String)input_table.getValueAt(0, 3);
				int Sex =Integer.parseInt((String)input_table.getValueAt(0, 4));
				int Age =Integer.parseInt((String)input_table.getValueAt(0, 5));
				String ShelterID = (String)input_table.getValueAt(0, 6);
				String ImageURL = (String)input_table.getValueAt(0, 7);
				
				State flag = mydb.ChangeAnimalInfo("D", AnimalID, AnimalNumber, AnimalName, SpeciesKind, Sex, Age, ShelterID, ImageURL);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Delete data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("shelterInfoPane")) 
			{
				String ShelterID = (String)input_table.getValueAt(0, 0);
				String ShelterName = (String)input_table.getValueAt(0, 1);
				String ShelterAddr = (String)input_table.getValueAt(0, 2);
				int PostalCode =Integer.parseInt((String)input_table.getValueAt(0, 3));
				int TotalRoomNums =Integer.parseInt((String)input_table.getValueAt(0, 4));
				int RemainingRoomNums =Integer.parseInt((String)input_table.getValueAt(0, 5));
				String Note = (String)input_table.getValueAt(0, 6);

				State flag = mydb.ChangeShelterInfo("D", ShelterID, ShelterName, ShelterAddr, PostalCode, TotalRoomNums, RemainingRoomNums, Note);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Delete data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("healthInfoPane")) 
			{
				String HealthInfoID = (String)input_table.getValueAt(0, 0);
				String AnimalID = (String)input_table.getValueAt(0, 1);
				String UserID = (String)input_table.getValueAt(0, 2);
				String HealthInfo = (String)input_table.getValueAt(0, 3);
				String checkDate =(String) input_table.getValueAt(0, 4);
				String Note = (String)input_table.getValueAt(0, 5);

				State flag = mydb.ChangeHealthInfo("D", HealthInfoID, AnimalID, UserID, HealthInfo, checkDate, Note);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Delete data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("vaccineInfoPane")) 
			{
				String VaccineID = (String)input_table.getValueAt(0, 0);
				String AnimalID = (String)input_table.getValueAt(0, 1);
				String UserID = (String)input_table.getValueAt(0, 2);
				String VaccineName = (String)input_table.getValueAt(0, 3);
				String InoculateDate =(String) input_table.getValueAt(0, 4);
				String Note = (String)input_table.getValueAt(0, 5);

				State flag = mydb.ChangeVaccineInfo("D", VaccineID, AnimalID, UserID, VaccineName, InoculateDate, Note);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Delete data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
		}
		if (changeFlag.equals("U")) 
		{
			if (universal_table==null||input_table==null) 
			{
				System.out.println("表为空");
				return;
			}
			if (changeTableName.equals("adminInfoPane")) 
			{
				String adminID = (String)input_table.getValueAt(0, 0);
				String adminName = (String)input_table.getValueAt(0, 1);
				String adminPassword = (String)input_table.getValueAt(0, 2);
				
				System.out.println("adminID："+adminID+" admin Name："+adminName+" Password："+adminPassword);
				
				State flag = mydb.ChangeAdminInfo("U", adminID, adminName, adminPassword);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Update data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("userInfoPane")) 
			{
				String UserID = (String)input_table.getValueAt(0, 0);
				String UserName = (String)input_table.getValueAt(0, 1);
				String PassWord = (String)input_table.getValueAt(0, 2);
				String EmailAddress = (String)input_table.getValueAt(0, 3);
				String PhoneNumber =(String) input_table.getValueAt(0, 4);
				String ShelterID = (String)input_table.getValueAt(0, 5);
				
				System.out.println(UserID+" "+UserName+" "+PassWord+" "+EmailAddress+" "+PhoneNumber+" "+ShelterID);
				
				State flag = mydb.ChangeUserInfo("U", UserID, UserName, PassWord, EmailAddress, PhoneNumber, ShelterID);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Update data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("animalInfoPane")) 
			{
				String AnimalID = (String)input_table.getValueAt(0, 0);
				String AnimalNumber = (String)input_table.getValueAt(0, 1);
				String AnimalName = (String)input_table.getValueAt(0, 2);
				String SpeciesKind = (String)input_table.getValueAt(0, 3);
				int Sex =Integer.parseInt((String)input_table.getValueAt(0, 4));
				int Age =Integer.parseInt((String)input_table.getValueAt(0, 5));
				String ShelterID = (String)input_table.getValueAt(0, 6);
				String ImageURL = (String)input_table.getValueAt(0, 7);
				
				State flag = mydb.ChangeAnimalInfo("U", AnimalID, AnimalNumber, AnimalName, SpeciesKind, Sex, Age, ShelterID, ImageURL);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Update data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("shelterInfoPane")) 
			{
				String ShelterID = (String)input_table.getValueAt(0, 0);
				String ShelterName = (String)input_table.getValueAt(0, 1);
				String ShelterAddr = (String)input_table.getValueAt(0, 2);
				int PostalCode =Integer.parseInt((String)input_table.getValueAt(0, 3));
				int TotalRoomNums =Integer.parseInt((String)input_table.getValueAt(0, 4));
				int RemainingRoomNums =Integer.parseInt((String)input_table.getValueAt(0, 5));
				String Note = (String)input_table.getValueAt(0, 6);

				State flag = mydb.ChangeShelterInfo("U", ShelterID, ShelterName, ShelterAddr, PostalCode, TotalRoomNums, RemainingRoomNums, Note);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Update data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("healthInfoPane")) 
			{
				String HealthInfoID = (String)input_table.getValueAt(0, 0);
				String AnimalID = (String)input_table.getValueAt(0, 1);
				String UserID = (String)input_table.getValueAt(0, 2);
				String HealthInfo = (String)input_table.getValueAt(0, 3);
				String checkDate =(String) input_table.getValueAt(0, 4);
				String Note = (String)input_table.getValueAt(0, 5);

				State flag = mydb.ChangeHealthInfo("U", HealthInfoID, AnimalID, UserID, HealthInfo, checkDate, Note);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Update data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
			else if (changeTableName.equals("vaccineInfoPane")) 
			{
				String VaccineID = (String)input_table.getValueAt(0, 0);
				String AnimalID = (String)input_table.getValueAt(0, 1);
				String UserID = (String)input_table.getValueAt(0, 2);
				String VaccineName = (String)input_table.getValueAt(0, 3);
				String InoculateDate =(String) input_table.getValueAt(0, 4);
				String Note = (String)input_table.getValueAt(0, 5);

				State flag = mydb.ChangeVaccineInfo("U", VaccineID, AnimalID, UserID, VaccineName, InoculateDate, Note);
				if (flag==State.Operate_Success) 
				{
					JOptionPane.showMessageDialog(null,"Success to Update data","Tip",JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < curHeaders.length; i++) 
					{
						input_table.setValueAt("", 0, i);
					}
				}
			}
		}
	};
	
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(new Color(153, 102, 255));
		frame.getContentPane().setBackground(new Color(102, 204, 204));
		frame.setBounds(100, 100, 663, 524);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(new Color(255, 255, 255));
		tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		tabbedPane.setBounds(0, 42, 647, 316);
		frame.getContentPane().add(tabbedPane);
		
		adminPane = new JTabbedPane(JTabbedPane.LEFT);
		adminPane.setBackground(new Color(255, 255, 255));
		adminPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTabbedPane curPane = (JTabbedPane)e.getComponent();
				System.out.println(curPane.getSelectedIndex());
				System.out.println(curPane.getSelectedComponent());
				JScrollPane curScrollPane = (JScrollPane)curPane.getSelectedComponent();
				System.out.println(curScrollPane.getName());
				
				String tableName = curScrollPane.getName();
				State flag = mydb.GetTable(tableName);
				if (flag==State.GetTable_Error) 
				{
					JOptionPane.showMessageDialog(null,"Fail to get the Table, Please try again!","Error Message",JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				setTable(tableName);
				changeTableName = tableName;
				if (universal_table==null) 
				{
					return;
				}
				curScrollPane.setViewportView(universal_table);
				curScrollPane.repaint();
			}
		});
		adminPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		tabbedPane.addTab("Admin", null, adminPane, null);
		
		adminInfoPane = new JScrollPane();
		adminInfoPane.setBackground(new Color(255, 255, 255));
		adminInfoPane.setName("adminInfoPane");
		adminPane.addTab("adminInfo", null, adminInfoPane, null);
		
		userInfoPane = new JScrollPane();
		userInfoPane.setName("userInfoPane");
		adminPane.addTab("userInfo", null, userInfoPane, null);
		
		animalInfoPane = new JScrollPane();
		animalInfoPane.setName("animalInfoPane");
		adminPane.addTab("animalInfo", null, animalInfoPane, null);
		
		shelterInfoPane = new JScrollPane();
		
		shelterInfoPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		shelterInfoPane.setName("shelterInfoPane");
		adminPane.addTab("shelterInfo", null, shelterInfoPane, null);
		
		healthInfoPane = new JScrollPane();
		healthInfoPane.setName("healthInfoPane");
		adminPane.addTab("healthInfo", null, healthInfoPane, null);
		
		vaccineInfoPane = new JScrollPane();
		vaccineInfoPane.setName("vaccineInfoPane");
		adminPane.addTab("vaccineInfo", null, vaccineInfoPane, null);
		
		userPane = new JTabbedPane(JTabbedPane.LEFT);
		userPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTabbedPane curPane = (JTabbedPane)e.getComponent();
				JScrollPane curScrollPane = (JScrollPane)curPane.getSelectedComponent();
				
				String tableName = curScrollPane.getName();
				State flag = mydb.GetTable(tableName);
				if (flag==State.GetTable_Error) 
				{
					JOptionPane.showMessageDialog(null,"Fail to get table,Please try to click the tab again! ","Error Message",JOptionPane.ERROR_MESSAGE);
					return;
				}
				setTable(tableName);
				if (universal_table==null) 
				{
					return;
				}
				curScrollPane.setViewportView(universal_table);
				curScrollPane.repaint();

			}
		});
		userPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		tabbedPane.addTab("User", null, userPane, null);
		
		animalInfoPane_U = new JScrollPane();
		animalInfoPane_U.setBackground(new Color(255, 255, 255));
		animalInfoPane_U.setName("animalInfoPane");
		
		userPane.addTab("animalInfo", null, animalInfoPane_U, null);
		
		shelterInfo_U = new JScrollPane();
		shelterInfo_U.setName("shelterInfoPane");
		userPane.addTab("shelterInfo", null, shelterInfo_U, null);
		
		healthInfo_U = new JScrollPane();
		healthInfo_U.setName("healthInfoPane");
		userPane.addTab("healthInfo", null, healthInfo_U, null);
		
		vaccineInfo_U = new JScrollPane();
		vaccineInfo_U.setName("vaccineInfoPane");
		userPane.addTab("vaccineInfo", null, vaccineInfo_U, null);
		
		JLabel helloLabel = new JLabel("Current User: "+this.UserName);
		helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
		helloLabel.setFont(new Font("华文楷体", Font.PLAIN, 23));
		helloLabel.setBounds(0, 0, 421, 43);
		frame.getContentPane().add(helloLabel);
		
		timeLabel = new JLabel("");
		timeLabel.setFont(new Font("华文楷体", Font.PLAIN, 23));
		timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timeLabel.setBounds(427, 0, 220, 43);
		frame.getContentPane().add(timeLabel);

	}
}
