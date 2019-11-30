package com.login;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.DB.DataBase;
import com.Interaction.Interaction;
import com.Statement.State;
import com.register.RegisterAdminWin;
import com.register.RegisterWin;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileLock;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class LoginWin {

	private static Point originPoint = new Point();
	
	private JFrame frame;
	private JPasswordField Psw_input;
	private JButton LoginBtn;
	private JButton RegisterBtn;
	private JComboBox<String> comboBox;
	private JTextField UserName_input;
	
	private DataBase mydb;
	private JButton exitBtn;

	private static FileLock lock = null;
	
	public static boolean isRunning()
    {
         try
         {
             //获得实例标志文件
             File flagFile = new File(".lock");
            
           //如果不存在就新建一个
             if(!flagFile.exists())
                 flagFile.createNewFile();
             
           //获得文件锁
            lock = new FileOutputStream(flagFile).getChannel().tryLock();
        
             //返回空表示文件已被运行的实例锁定
             if(lock==null)
                 return false;
         }
         catch(Exception ex)
         {
             ex.printStackTrace();
         }
         return true;
     }

	public static void main(String[] args) 
	{
		if(isRunning()) 
		{
			EventQueue.invokeLater(new Runnable() 
			{
				public void run() 
				{
					try 
					{
						LoginWin window = new LoginWin();
						window.frame.setVisible(true);
						window.setWinStyle();
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
			});
		}
		else 
		{
			System.out.println("不能将应用启动两次！");
		}
	}


	public LoginWin() {
		initialize();
//		frame.setResizable(false);
		if (mydb.ConnectToDataBase()==State.Connection_Error) 
		{
			JOptionPane.showMessageDialog(null,"Error Message","Error",JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		} 
		System.out.println("连接数据库成功！");

	}
	
	public JFrame getFrame()
	{
		return frame;
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


	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(51, 204, 255));
		frame.setBounds(100, 100, 401, 284);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel topLabel = new JLabel("Animal Shelter Information Management System");
		topLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		topLabel.setBounds(10, 38, 381, 50);
		frame.getContentPane().add(topLabel);
		
		UserName_input = new JTextField();
		UserName_input.setFont(new Font("华文楷体", Font.PLAIN, 18));
		UserName_input.setBounds(93, 98, 238, 35);
		frame.getContentPane().add(UserName_input);
		UserName_input.setColumns(10);
		
		Psw_input = new JPasswordField();
		Psw_input.setFont(new Font("华文楷体", Font.PLAIN, 18));
		Psw_input.setBounds(93, 157, 238, 35);
		frame.getContentPane().add(Psw_input);
		
		comboBox = new JComboBox<String>();
		comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		comboBox.setBounds(239, 202, 92, 23);
		comboBox.addItem("Admin");
		comboBox.addItem("User");
		frame.getContentPane().add(comboBox);
		
		JLabel lblNewLabel = new JLabel("UserName");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 98, 73, 35);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("PassWord");
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 157, 73, 35);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Select Login Method");
		lblNewLabel_2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNewLabel_2.setBounds(93, 202, 136, 23);
		frame.getContentPane().add(lblNewLabel_2);
		
		LoginBtn = new JButton("Login");
		LoginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String UserName = UserName_input.getText();
				String PassWord = String.valueOf(Psw_input.getPassword());
				if ("".equals(UserName)||"".equals(PassWord)) 
				{
					JOptionPane.showMessageDialog(null,"You should Input UserName and Password!","Error Message",JOptionPane.ERROR_MESSAGE);
					return;
				}
				System.out.println("用户名："+UserName+" 密码："+PassWord);
				String choice = (String)comboBox.getSelectedItem();
				State status = State.Login_Success;
				if (choice=="Admin")
				{
					status = mydb.adminLogin(UserName, PassWord);
				}
				else if (choice=="User") 
				{
					status = mydb.login(UserName, PassWord);
				}
				switch (status)
				{
					case Connection_Error:
						JOptionPane.showMessageDialog(null,"Fail to connect to the DataBase,Please Check your NetWork! ","Error Message",JOptionPane.ERROR_MESSAGE);
						return;
					case Login_Error:
						JOptionPane.showMessageDialog(null,"Fail to Log in,Please Check your User Name and Password!","Error Message",JOptionPane.ERROR_MESSAGE);
						return;
					case Login_Success:
						JOptionPane.showMessageDialog(null,"Success to Log in! ","Tip",JOptionPane.INFORMATION_MESSAGE);
						break;
					default:
						throw new IllegalArgumentException("Unexpected value: " + status);
				}
				Interaction interaction = new Interaction(choice,UserName);
				interaction.setMyDB(mydb);
				frame.setVisible(false);
				interaction.getFrame().setVisible(true);
				
			}
		});
		LoginBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		LoginBtn.setBounds(93, 235, 93, 39);
		frame.getContentPane().add(LoginBtn);
		
		RegisterBtn = new JButton("Register");
		RegisterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String chioce = (String)comboBox.getSelectedItem();
				//选择注册方式
				if (chioce=="Admin") 
				{
					int result = JOptionPane.showConfirmDialog(frame,"你将以管理员方式注册，请确认！", "Tip",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result==JOptionPane.YES_OPTION)
					{
						RegisterAdminWin registerAdminWin = new RegisterAdminWin(frame);
						frame.setVisible(false);
						registerAdminWin.setMyDB(mydb);
						registerAdminWin.setModal(true);
						registerAdminWin.setVisible(true);
					}
					else 
					{
						return;
					}
				}
				else 
				{
					int result = JOptionPane.showConfirmDialog(frame,"你将以用户方式注册，请确认！", "Tip",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result==JOptionPane.YES_OPTION)
					{
						RegisterWin registerWin = new RegisterWin(frame);
						frame.setVisible(false);
						registerWin.setMyDB(mydb);
						registerWin.setModal(true);
						registerWin.setVisible(true);
					}
					else 
					{
						return;
					}
				}
				
			}
		});
		RegisterBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		RegisterBtn.setBounds(238, 235, 93, 39);
		frame.getContentPane().add(RegisterBtn);
		
		exitBtn = new JButton("");
		exitBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				exitBtn.setBackground(Color.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				exitBtn.setBackground(Color.LIGHT_GRAY);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				exitBtn.setBackground(Color.ORANGE);
			}
		});
		exitBtn.setBackground(new Color(102, 204, 255));
		exitBtn.setForeground(Color.WHITE);
		exitBtn.setBounds(352, 0, 49, 39);
		exitBtn.setMargin(new Insets(0,0,0,0));
		exitBtn.setBorder(null);
		exitBtn.setIconTextGap(0);
		exitBtn.setFocusPainted(false);
		ImageIcon image = new ImageIcon("./ico/_Exit.png");
		image.setImage(image.getImage().getScaledInstance(exitBtn.getWidth(),exitBtn.getHeight(),Image.SCALE_DEFAULT ));
		exitBtn.setIcon(image);
		frame.getContentPane().add(exitBtn);
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
		
		mydb = new DataBase();
	}
}
