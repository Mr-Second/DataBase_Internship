package com.register;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.DB.DataBase;
import com.Statement.State;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class RegisterAdminWin extends JDialog 
{

	private static final long serialVersionUID = 1L;
	private JTextField adminUserName_input;
	private JPasswordField adminPassword_input;
	
	private JFrame ParentWin;
	
	private DataBase mydb;
	
	public void setMyDB(DataBase mydb) 
	{
		this.mydb = mydb;
	}

	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					RegisterAdminWin dialog = new RegisterAdminWin(new JFrame());
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
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


	public RegisterAdminWin(JFrame loginWin) 
	{
		getContentPane().setBackground(new Color(102, 204, 255));
		setBackground(new Color(51, 204, 255));
		setBounds(100, 100, 421, 302);
		getContentPane().setLayout(null);
		
		JLabel topLabel = new JLabel("Admin Register");
		topLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
		topLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topLabel.setBounds(0, 0, 405, 72);
		getContentPane().add(topLabel);
		
		JLabel lblNewLabel = new JLabel("Admin User Name");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblNewLabel.setBounds(10, 82, 144, 35);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Admin Password");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(10, 142, 144, 29);
		getContentPane().add(lblNewLabel_1);
		
		adminUserName_input = new JTextField();
		adminUserName_input.setFont(new Font("华文楷体", Font.PLAIN, 20));
		adminUserName_input.setBounds(164, 82, 199, 35);
		getContentPane().add(adminUserName_input);
		adminUserName_input.setColumns(10);
		
		adminPassword_input = new JPasswordField();
		adminPassword_input.setFont(new Font("华文楷体", Font.PLAIN, 20));
		adminPassword_input.setBounds(164, 139, 199, 35);
		getContentPane().add(adminPassword_input);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String AdminID = RegisterWin.getID();
				String AdminName = adminUserName_input.getText();
				String Password = String.valueOf(adminPassword_input.getPassword());
				State flag = mydb.registerAdminUser(AdminID, AdminName, Password);
				switch (flag)
				{
					case Insert_Success:
						JOptionPane.showMessageDialog(null,"Success to Register,Please login in the MainWindow! ","Tip",JOptionPane.INFORMATION_MESSAGE);
						break;
					case Repetitive_UserID:
						JOptionPane.showMessageDialog(null,"Repetitive User ID, Please try to Register again! ","Error Message",JOptionPane.ERROR_MESSAGE);
						break;
					case Repetitive_UserName:
						JOptionPane.showMessageDialog(null,"The User Name you input has already been registered, Please choose another User Name!","Error Message",JOptionPane.ERROR_MESSAGE);
						break;
					case Insert_Error:
						JOptionPane.showMessageDialog(null,"Fail to Register,Please try to register again! ","Error Message",JOptionPane.ERROR_MESSAGE);
						break;
					default:
						throw new IllegalArgumentException("Unexpected value: " + flag);
				}
			}
		});
		btnRegister.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		btnRegister.setBounds(164, 201, 130, 35);
		getContentPane().add(btnRegister);
		
		this.setWinStyle();
		this.ParentWin = loginWin;
		
		this.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowDeactivated(WindowEvent e) 
			{
				ParentWin.setVisible(true);
				dispose();
			}
		});
	}
}
