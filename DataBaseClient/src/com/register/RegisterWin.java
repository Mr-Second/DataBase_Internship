package com.register;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.UUID;

import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.DB.DataBase;
import com.Statement.State;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class RegisterWin extends JDialog {

	
	private static final long serialVersionUID = 1L;
	private JTextField UserName_input;
	private JTextField PassWord_input;
	private JTextField Email_input;
	private JTextField PhoneNumber_input;
	private JTextField ShelterId_input;
	private JButton registerBtn;
	
	private JFrame ParentWin;
	
	private DataBase mydb;
	
	public void setMyDB(DataBase mydb) 
	{
		this.mydb = mydb;
	}

	public static String getID() 
	{
		return  UUID.randomUUID().toString();
	}

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
	
	public RegisterWin(JFrame loginWin) 
	{
		getContentPane().setBackground(new Color(102, 204, 255));
		setBackground(new Color(102, 204, 255));
		setForeground(new Color(102, 204, 255));
		getContentPane().setForeground(new Color(102, 204, 255));
		setBounds(100, 100, 450, 408);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("User Register");
		lblNewLabel.setBounds(0, 0, 444, 57);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("UserName");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(10, 67, 120, 30);
		getContentPane().add(lblNewLabel_1);
		
		UserName_input = new JTextField();
		UserName_input.setBounds(140, 67, 229, 30);
		getContentPane().add(UserName_input);
		UserName_input.setColumns(10);
		
		PassWord_input = new JTextField();
		PassWord_input.setColumns(10);
		PassWord_input.setBounds(140, 107, 229, 30);
		getContentPane().add(PassWord_input);
		
		Email_input = new JTextField();
		Email_input.setColumns(10);
		Email_input.setBounds(140, 158, 229, 30);
		getContentPane().add(Email_input);
		
		PhoneNumber_input = new JTextField();
		PhoneNumber_input.setColumns(10);
		PhoneNumber_input.setBounds(140, 198, 229, 30);
		getContentPane().add(PhoneNumber_input);
		
		ShelterId_input = new JTextField();
		ShelterId_input.setColumns(10);
		ShelterId_input.setBounds(140, 238, 229, 30);
		getContentPane().add(ShelterId_input);
		
		JLabel lblPassword = new JLabel("PassWord");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblPassword.setBounds(10, 107, 120, 30);
		getContentPane().add(lblPassword);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblEmail.setBounds(10, 154, 120, 30);
		getContentPane().add(lblEmail);
		
		JLabel lblPhoneNumber = new JLabel("Phone Number");
		lblPhoneNumber.setHorizontalAlignment(SwingConstants.CENTER);
		lblPhoneNumber.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblPhoneNumber.setBounds(10, 194, 120, 30);
		getContentPane().add(lblPhoneNumber);
		
		JLabel lblShelterId = new JLabel("Shelter ID");
		lblShelterId.setHorizontalAlignment(SwingConstants.CENTER);
		lblShelterId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblShelterId.setBounds(10, 234, 120, 30);
		getContentPane().add(lblShelterId);
		
		registerBtn = new JButton("Register");
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String UserID = getID();
				String UserName = UserName_input.getText();
				String PassWord = PassWord_input.getText();
				String EmailAddr = Email_input.getText();
				String PhoneNo = PhoneNumber_input.getText();
				String ShelterID = ShelterId_input.getText();
				if (""==UserName||UserName==null||""==PassWord||PassWord==null)
				{
					JOptionPane.showMessageDialog(null,"Error Message","User Name or Password is not allowed to be null!",JOptionPane.ERROR_MESSAGE);
					return;
				}

				State status = mydb.registerUser(UserID,UserName, PassWord, EmailAddr, PhoneNo, ShelterID);
				
				switch (status) 
				{
					case Insert_Success:
						JOptionPane.showMessageDialog(null,"Success to Register,Please login in the MainWindow! ","Tip",JOptionPane.INFORMATION_MESSAGE);
						break;
					case Insert_Error:
						JOptionPane.showMessageDialog(null,"Fail to Register,Please try to register again! ","Error Message",JOptionPane.ERROR_MESSAGE);
						break;
					case Connection_Error:
						JOptionPane.showMessageDialog(null,"Fail to connect to the DataBase,Please Check your NetWork! ","Error Message",JOptionPane.ERROR_MESSAGE);
						break;
					case InValid_EmailAddress:
						JOptionPane.showMessageDialog(null,"Wrong Email Address,Please Check your Email Address!","Error Message",JOptionPane.ERROR_MESSAGE);
						break;
					case InValid_PhoneNumber:
						JOptionPane.showMessageDialog(null,"Wrong PhoneNumber,Please Check your Phone Number again! ","Error Message",JOptionPane.ERROR_MESSAGE);
						break;
					case Repetitive_UserID:
						JOptionPane.showMessageDialog(null,"Repetitive User ID, Please try to Register again! ","Error Message",JOptionPane.ERROR_MESSAGE);
						break;
					case Repetitive_UserName:
						JOptionPane.showMessageDialog(null,"The User Name you input has already been registered, Please choose another User Name!","Error Message",JOptionPane.ERROR_MESSAGE);
						break;
					case InValid_ShelterID:
						JOptionPane.showMessageDialog(null,"The Shelter ID you input does't exist, Please check your Shelter ID and Register again!","Error Message",JOptionPane.ERROR_MESSAGE);
						break;
					default:
						throw new IllegalArgumentException("Unexpected value: " + status);
				}
			}
		});
		registerBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		registerBtn.setBounds(169, 301, 125, 39);
		getContentPane().add(registerBtn);
		
		this.ParentWin = loginWin;
		this.setWinStyle();
		this.setResizable(false);
		
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
