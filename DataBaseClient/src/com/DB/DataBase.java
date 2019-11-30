package com.DB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.Statement.State;
import com.structs.adminStruct;
import com.structs.animalStruct;
import com.structs.healthStruct;
import com.structs.shelterStruct;
import com.structs.userStruct;
import com.structs.vaccineStruct;


public class DataBase 
{
	private ArrayList<adminStruct>adminTable=new ArrayList<adminStruct>();
	private ArrayList<userStruct>userTable =new ArrayList<userStruct>();
	private ArrayList<animalStruct>animalTable =new ArrayList<animalStruct>();
	private ArrayList<shelterStruct>shelterTable =new ArrayList<shelterStruct>();
	private ArrayList<healthStruct>healthTable =new ArrayList<healthStruct>();
	private ArrayList<vaccineStruct>vaccineTable =new ArrayList<vaccineStruct>();
	
	private Connection conn=null;
	private Statement st=null;
	private ResultSet rs=null;
	
	private String url="jdbc:oracle:thin:@47.94.134.159:1521:orcl";
	private String user = "C##TMP_USER";
	private String password = "mountain";
	
	public ResultSet getResultSet() 
	{
		return rs;
	}
	
	@SuppressWarnings("finally")
	public State ConnectToDataBase()
	{
		  State status  = State.Connection_Success;
		  try 
		  {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(url, user, password);// 连接数据库
				st = conn.createStatement();
		  } 
		  catch (ClassNotFoundException | SQLException e) 
		  {
			  status = State.Connection_Error;
			    e.printStackTrace();
		  }
		  finally 
		  {
			  return status;
		  }
	}
	
	@SuppressWarnings("finally")
	public State login(String UserName,String PassWord) 
	{
		State status = State.Login_Success;
		try 
		{
			CallableStatement c = conn.prepareCall("{? = call check_Login(?,?)}");
			c.registerOutParameter(1, Types.SMALLINT);
			c.setString(2, UserName);
			c.setString(3, PassWord);
			c.execute();
			int result = c.getInt(1);
			//调用数据库的登陆函数，参数为用户名和密码，返回为整数若为1则说明用户存在登陆成功，反之登陆失败
			status = ( result==1 ? State.Login_Success : State.Login_Error);
		} 
		catch (SQLException e) 
		{
			status = State.Connection_Error;
			e.printStackTrace();
		}
		finally 
		{
			return status;
		}
	}
	
	@SuppressWarnings("finally")
	public State adminLogin(String UserName,String PassWord) 
	{
		State status = State.Login_Success;
		try 
		{
			CallableStatement c = conn.prepareCall("{? = call check_Admin_Login(?,?)}");
			c.registerOutParameter(1, Types.SMALLINT);
			c.setString(2, UserName);
			c.setString(3, PassWord);
			c.execute();
			int result = c.getInt(1);
			//调用数据库的登陆函数，参数为用户名和密码，返回为整数若为1则说明用户存在登陆成功，反之登陆失败
			status = ( result==1 ? State.Login_Success : State.Login_Error);
		} 
		catch (SQLException e) 
		{
			status = State.Connection_Error;
			e.printStackTrace();
		}
		finally 
		{
			return status;
		}
	}
	
	@SuppressWarnings("finally")
	public State registerUser(String UserID,String UserName,String PassWord,String EmailAddr,String PhoneNo,String ShelterID) 
	{
		State status = State.Insert_Success;
		
		//插入用户信息之前先检查UserID、UserName、EmailAddress、PhoneNumber是否有效
		status = CheckUserID(UserID);
		if (status!=State.Valid_UserID) 
		{
			return status;
		}
		
		status = CheckUserName(UserName);
		if (status!=State.Valid_UserName) 
		{
			return status;
		}
		if (!"".equals(EmailAddr)) 
		{
			status = CheckEmailAddr(EmailAddr);
			if (status!=State.Valid_EmailAddress) 
			{
				return status;
			}
		}

		if(!"".equals(PhoneNo)) 
		{
			status = CheckPhoneNumber(PhoneNo);
			if (status!=State.Valid_PhoneNumber)
			{
				return status;
			}
		}
		
		status = CheckShelterID(ShelterID);
		if(status!=State.Valid_ShelterID) 
		{
			return status;
		}
		
		//当用户ID、用户名、Email地址、手机号码均有效时才进行插入语句
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT into C##TMP_USER.\"user_info\" VALUES(");
		sql.append("\'"+UserID+"\',");
		sql.append("\'"+UserName+"\',");
		sql.append("\'"+PassWord+"\',");
		sql.append("\'"+EmailAddr+"\',");
		sql.append("\'"+PhoneNo+"\',");
		sql.append("\'"+ShelterID+"\')");
		System.out.println(sql.toString());
		try 
		{
			if (st.execute(sql.toString())==false) 
			{
				status = State.Insert_Success;
			}; 	//execute成功查找对象返回true,成功插入或删除返回false
		} 
		catch (SQLException e) 
		{
			status = State.Insert_Error;
			e.printStackTrace();
		}
		finally 
		{
			System.out.print(status);
			return status;
		}
	}
	
	public State CheckEmailAddr(String EmailAddr) 
	{
		State status  = State.Valid_EmailAddress;
		String regEx = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"; 
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(EmailAddr);
		if (!matcher.matches())
		{
			status = State.InValid_EmailAddress;
		}
		return status;
	}
	
	public State CheckPhoneNumber(String PhoneNumber) 
	{
		State status = State.Valid_PhoneNumber;
		String regEx = "\\d{11}";  						//11位纯数字
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(PhoneNumber);
		if (!matcher.matches())
		{
			status = State.InValid_PhoneNumber;
		}
		return status;
	}
	
	@SuppressWarnings("finally")
	public State CheckShelterID(String ShelterID) 
	{
		State status = State.Valid_ShelterID;
		try 
		{
			CallableStatement c = conn.prepareCall("{? = call check_Insert_User_ShelterID(?)}");
			c.registerOutParameter(1, Types.SMALLINT);
			c.setString(2, ShelterID);
			c.execute();
			int result = c.getInt(1);
			//当插入用户信息表时检查用户ID是否重复，如果重复返回0，不重复返回1
			status = ( result==0 ? State.Valid_ShelterID : State.InValid_ShelterID);
		} 
		catch (SQLException e) 
		{
			status = State.Connection_Error;
			e.printStackTrace();
		}
		finally 
		{
			return status;
		}
	}
	
	@SuppressWarnings("finally")
	public State CheckUserID(String UserID) 
	{
		State status = State.Repetitive_UserID;
		try 
		{
			CallableStatement c = conn.prepareCall("{? = call check_Insert_User_ID(?)}");
			c.registerOutParameter(1, Types.SMALLINT);
			c.setString(2, UserID);
			c.execute();
			int result = c.getInt(1);
			//当插入用户信息表时检查用户ID是否重复，如果重复返回0，不重复返回1
			status = ( result==0 ? State.Repetitive_UserID : State.Valid_UserID );
		} 
		catch (SQLException e) 
		{
			status = State.Connection_Error;
			e.printStackTrace();
		}
		finally 
		{
			return status;
		}
	}
	
	@SuppressWarnings("finally")
	public State CheckUserName(String UserName) 
	{
		State status = State.Valid_UserName;
		try 
		{
			CallableStatement c = conn.prepareCall("{? = call check_Insert_User_Name(?)}");
			c.registerOutParameter(1, Types.SMALLINT);
			c.setString(2, UserName);
			c.execute();
			int result = c.getInt(1);
			//当插入用户信息表时检查用户ID是否重复，如果重复返回0，不重复返回1
			status = ( result==0 ? State.Repetitive_UserName : State.Valid_UserName );
		} 
		catch (SQLException e) 
		{
			status = State.Connection_Error;
			e.printStackTrace();
		}
		finally 
		{
			return status;
		}
	}
	
	@SuppressWarnings("finally")
	public State registerAdminUser(String AdminID,String AdminName,String PassWord) 
	{
		State status = State.Insert_Success;
		
		status = CheckAdminID(AdminID);
		if (status!=State.Valid_UserID) 
		{
			return status;
		}
		
		status = CheckAdminName(AdminName);
		if (status!=State.Valid_UserName) 
		{
			return status;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT into C##TMP_USER.\"admin_info\" VALUES(");
		sql.append("\'"+AdminID+"\',");
		sql.append("\'"+AdminName+"\',");
		sql.append("\'"+PassWord+"\')");
		System.out.println(sql.toString());
		try 
		{
			if (st.execute(sql.toString())==false) 
			{
				status = State.Insert_Success;
			}; 	//execute成功查找对象返回true,成功插入或删除返回false
		} 
		catch (SQLException e) 
		{
			status = State.Insert_Error;
			e.printStackTrace();
		}
		finally 
		{
			System.out.print(status);
			return status;
		}
	}
	
	@SuppressWarnings("finally")
	public State CheckAdminID(String AdminID) 
	{
		State status = State.Valid_UserID;
		try 
		{
			CallableStatement c = conn.prepareCall("{? = call check_Insert_Admin_ID(?)}");
			c.registerOutParameter(1, Types.SMALLINT);
			c.setString(2, AdminID);
			c.execute();
			int result = c.getInt(1);
			//当插入用户信息表时检查用户ID是否重复，如果重复返回0，不重复返回1
			status = ( result==0 ? State.Repetitive_UserID : State.Valid_UserID );
		} 
		catch (SQLException e) 
		{
			status = State.Connection_Error;
			e.printStackTrace();
		}
		finally 
		{
			return status;
		}
	}
	
	@SuppressWarnings("finally")
	public State CheckAdminName(String AdminName) 
	{
		State status = State.Valid_UserName;
		try 
		{
			CallableStatement c = conn.prepareCall("{? = call check_Insert_Admin_Name(?)}");
			c.registerOutParameter(1, Types.SMALLINT);
			c.setString(2, AdminName);
			c.execute();
			int result = c.getInt(1);
			//当插入用户信息表时检查用户ID是否重复，如果重复返回0，不重复返回1
			status = ( result==0 ? State.Repetitive_UserName : State.Valid_UserName );
		} 
		catch (SQLException e) 
		{
			status = State.Connection_Error;
			e.printStackTrace();
		}
		finally 
		{
			return status;
		}
	}
	
	
	@SuppressWarnings("finally")
	public State ChangeUserInfo(String flag, String UserID, String UserName, String PassWord, String EmailAddress, String PhoneNumber, String ShelterID) 
	{
		State status = State.Operate_Success;
		String sql = "call changeUserInfo(?,?,?,?,?,?,?,?)";
		try 
		{
			CallableStatement csmt = conn.prepareCall(sql);
			csmt.setString(1, flag);
			csmt.setString(2, UserID);
			csmt.setString(3, UserName);
			csmt.setString(4, PassWord);
			csmt.setString(5, EmailAddress);
			csmt.setString(6, PhoneNumber);
			csmt.setString(7, ShelterID);
			csmt.registerOutParameter(8, Types.SMALLINT);
			csmt.execute();
			int myFlag = csmt.getInt(8);
			if (myFlag==0) 
			{
				status = State.Operate_Error;
			}
			csmt.close();
		} 
		catch (SQLException e) 
		{
			status = State.Connection_Error;
			e.printStackTrace();
		}
		finally 
		{
			return status;
		}

	}
	
	@SuppressWarnings("finally")
	public State ChangeAdminInfo(String flag,String adminID,String adminName,String adminPassword) 
	{
		State status = State.Operate_Success;
		String sql = "call changeAdminInfo(?,?,?,?,?)";
		try 
		{
			CallableStatement csmt = conn.prepareCall(sql);
			csmt.setString(1, flag);
			csmt.setString(2, adminID);
			csmt.setString(3, adminName);
			csmt.setString(4, adminPassword);
			csmt.registerOutParameter(5, Types.SMALLINT);
			csmt.execute();
			int myFlag = csmt.getInt(5);
			if (myFlag==0) 
			{
				status = State.Operate_Error;
			}
			csmt.close();
		} 
		catch (SQLException e) 
		{
			status = State.Connection_Error;
			e.printStackTrace();
		}
		finally 
		{
			return status;
		}
	}
	
	@SuppressWarnings("finally")
	public State ChangeAnimalInfo(String flag,String animalID,String animalNumber,String animalName,String speciesKind,int Sex,int age,String shelterID,String imageURL ) 
	{
		State status = State.Operate_Success;
		String sql = "call changeAnimalInfo(?,?,?,?,?,?,?,?,?,?)";
		try 
		{
			CallableStatement csmt = conn.prepareCall(sql);
			csmt.setString(1, flag);
			csmt.setString(2, animalID);
			csmt.setString(3, animalNumber);
			csmt.setString(4, animalName);
			csmt.setString(5, speciesKind);
			csmt.setLong(6, Sex);
			csmt.setLong(7, age);
			csmt.setString(8, shelterID);
			csmt.setString(9, imageURL);

			csmt.registerOutParameter(10, Types.SMALLINT);
			csmt.execute();
			int myFlag = csmt.getInt(10);
			if (myFlag==0) 
			{
				status = State.Operate_Error;
			}
			csmt.close();
		} 
		catch (SQLException e) 
		{
			status = State.Connection_Error;
			e.printStackTrace();
		}
		finally 
		{
			return status;
		}
	}

	@SuppressWarnings("finally")
	public State ChangeHealthInfo(String flag,String healthInfoID,String animalID,String userID,String healthInfo,String checkDate,String Note) 
	{
		State status = State.Operate_Success;
		String sql = "call changeHealthInfo(?,?,?,?,?,?,?,?)";
		try 
		{
			CallableStatement csmt = conn.prepareCall(sql);
			csmt.setString(1, flag);
			csmt.setString(2, healthInfoID);
			csmt.setString(3, animalID);
			csmt.setString(4, userID);
			csmt.setString(5, healthInfo);
			

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = format.parse(checkDate);
			csmt.setDate(6, new Date(date.getTime()));
			
			csmt.setString(7, Note);
			csmt.registerOutParameter(8, Types.SMALLINT);
			csmt.execute();
			int myFlag = csmt.getInt(8);
			if (myFlag==0) 
			{
				status = State.Operate_Error;
			}
			csmt.close();
		} 
		catch (SQLException e) 
		{
			status = State.Connection_Error;
			e.printStackTrace();
		}
		finally 
		{
			return status;
		}
	}
	
	@SuppressWarnings("finally")
	public State ChangeShelterInfo(String flag,String shelterID,String shelterName,String shelterAddr,int postalCode,int totalRoomNums,int remainingRoomNums,String Note) 
	{
		State status = State.Operate_Success;
		String sql = "call changeShelterInfo(?,?,?,?,?,?,?,?,?)";
		try 
		{
			CallableStatement csmt = conn.prepareCall(sql);
			csmt.setString(1, flag);
			csmt.setString(2, shelterID);
			csmt.setString(3, shelterName);
			csmt.setString(4, shelterAddr);
			csmt.setInt(5, postalCode);
			csmt.setInt(6, totalRoomNums);
			csmt.setInt(7, remainingRoomNums);
			csmt.setString(8,Note);
			
			csmt.registerOutParameter(9, Types.SMALLINT);
			csmt.execute();
			int myFlag = csmt.getInt(9);
			if (myFlag==0) 
			{
				status = State.Operate_Error;
			}
			csmt.close();
		} 
		catch (SQLException e) 
		{
			status = State.Connection_Error;
			e.printStackTrace();
		}
		finally 
		{
			return status;
		}
	}

	 @SuppressWarnings("finally")
	public State ChangeVaccineInfo(String flag,String vaccineID,String animalID,String userID,String vaccineName,String inoculateDate,String Note) 
	 {
		 State status = State.Operate_Success;
			String sql = "call changeVaccineInfo(?,?,?,?,?,?,?,?)";
			try 
			{
				CallableStatement csmt = conn.prepareCall(sql);
				csmt.setString(1, flag);
				csmt.setString(2, vaccineID);
				csmt.setString(3, animalID);
				csmt.setString(4, userID);
				csmt.setString(5, vaccineName);
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date = format.parse(inoculateDate);
				csmt.setDate(6, new Date(date.getTime()));
				
				csmt.setString(7, Note);

				csmt.registerOutParameter(8, Types.SMALLINT);
				csmt.execute();
				int myFlag = csmt.getInt(8);
				if (myFlag==0) 
				{
					status = State.Operate_Error;
				}
				csmt.close();
			} 
			catch (SQLException e) 
			{
				status = State.Connection_Error;
				e.printStackTrace();
			}
			finally 
			{
				return status;
			}
	 }

	 
	 @SuppressWarnings("finally")
	public State SearchAdmin(String adminName,adminStruct ast) 
	 {
		 State status = State.Operate_Success;
		 String sql = "  Select \"adminID\",\"adminName\",\"adminPassWord\"  " 
				 + "  from C##TMP_USER.\"admin_info\"    " 
				 + "  WHERE \"adminName\"=\'"+adminName+"\'";
		 System.out.println(sql);
		 try 
			{
			 	Statement stmt = conn.prepareCall(sql);
				rs = stmt.executeQuery(sql);
				while (rs.next()) 
				{
					ast.setAdminID(rs.getString("adminID"));
					ast.setAdminName(rs.getString("adminName"));
					ast.setAdminPassWord(rs.getString("adminPassWord"));
				}
				stmt.close();
			} 
			catch (SQLException e) 
			{
				status = State.Connection_Error;
				e.printStackTrace();
			}
			finally 
			{
				return status;
			}
	 }
	 
	 @SuppressWarnings("finally")
	public State SearchUser(String userName,userStruct ust) 
	 {
		 State status = State.Operate_Success;
		 String sql 	= "  SELECT \"UserID\",\"UserName\",\"PassWord\",\"EmailAddress\",\"PhoneNumber\",\"ShelterID\"  " + "\n"
								 + "  from C##TMP_USER.\"user_info\"    " + "\n"
								 + "  WHERE \"UserName\" = \'"+ userName+"\'";
		 System.out.println(sql);
		 try 
			{
			 	Statement stmt = conn.prepareCall(sql);
				rs = stmt.executeQuery(sql);
				while (rs.next()) 
				{
					ust.setUserID(rs.getString("UserID"));
					ust.setUserNameString(rs.getString("UserName"));
					ust.setPassWord(rs.getString("PassWord"));
					ust.setEmailAddress(rs.getString("EmailAddress"));
					ust.setPhoneNumber(rs.getString("PhoneNumber"));
					ust.setShelterID(rs.getString("ShelterID"));
				}
				stmt.close();
			} 
			catch (SQLException e) 
			{
				status = State.Connection_Error;
				e.printStackTrace();
			}
			finally 
			{
				return status;
			}
	 }
	 
 	@SuppressWarnings("finally")
	public State SearchAnimal(String animalNumber,animalStruct animalst) 
	 {
		 State status = State.Operate_Success;
		 String sql 	= "  SELECT *                          " + "\n"
				 + "  from C##TMP_USER.\"animal_info\"  " + "\n"
				 + "  WHERE \"AnimalNumber\"=\'"+animalNumber+"\'";
		 System.out.println(sql);
		 try 
			{
			 	Statement stmt = conn.prepareCall(sql);
				rs = stmt.executeQuery(sql);
				while (rs.next()) 
				{
					animalst.setAnimalID(rs.getString("AnimalID"));
					animalst.setAnimalNumber(rs.getString("AnimalNumber"));
					animalst.setAnimalName(rs.getString("AnimalName"));
					animalst.setSpeciesKind(rs.getString("SpeciesKind"));
					animalst.setSex(rs.getInt("Sex"));
					animalst.setAge(rs.getInt("Age"));
					animalst.setShelterID(rs.getString("ShelterID"));
					animalst.setImage(rs.getString("Image"));
				}
				stmt.close();
			} 
			catch (SQLException e) 
			{
				status = State.Connection_Error;
				e.printStackTrace();
			}
			finally 
			{
				return status;
			}
	 }
	
	@SuppressWarnings("finally")
	public State SearchShelter(String shelterName,shelterStruct shelterst) 
	 {
		 State status = State.Operate_Success;
		 String sql = "  SELECT *                           " + "\n"
				 + "  FROM C##TMP_USER.\"shelter_info\"  " + "\n"
				 + "  WHERE \"ShelterName\"=\'"+shelterName+"\'";
		 System.out.println(sql);
		 try 
			{
			 	Statement stmt = conn.prepareCall(sql);
				rs = stmt.executeQuery(sql);
				while (rs.next()) 
				{
					shelterst.setShelterID(rs.getString("ShelterID"));
					shelterst.setShelterName(rs.getString("ShelterName"));
					shelterst.setShelterAddress(rs.getString("ShelterAddr"));
					shelterst.setPostalCode(rs.getInt("PostalCode"));
					shelterst.setTotalRoomNums(rs.getInt("TotalRoomNums"));
					shelterst.setRemainingRoomNums(rs.getInt("RemainingRoomNums"));
					shelterst.setNote(rs.getString("Note"));
				}
				stmt.close();
			} 
			catch (SQLException e) 
			{
				status = State.Connection_Error;
				e.printStackTrace();
			}
			finally 
			{
				return status;
			}
	 }
 	
	@SuppressWarnings("finally")
	public State SearchHealthInfo(String healthInfoID,healthStruct healthst) 
	 {
		 State status = State.Operate_Success;
		 String sql = "  SELECT *                                 " + "\n"
							 + "  FROM C##TMP_USER.\"animal_health_info\"  " + "\n"
							 + "  WHERE \"HealthInfoID\"=\'"+healthInfoID+"\'";
		 System.out.println(sql);
		 try 
			{
			 	Statement stmt = conn.prepareCall(sql);
				rs = stmt.executeQuery(sql);
				while (rs.next()) 
				{
					healthst.setHealthInfo(rs.getString("HealthInfoID"));
					healthst.setAnimalID(rs.getString("AnimalID"));
					healthst.setUserID(rs.getString("UserID"));
					healthst.setHealthInfo(rs.getString("HealthInfo"));
					healthst.setCheckDate(rs.getDate("CheckDate"));
					healthst.setNote(rs.getString("Note"));
				}
				stmt.close();
			} 
			catch (SQLException e) 
			{
				status = State.Connection_Error;
				e.printStackTrace();
			}
			finally 
			{
				return status;
			}
	 }
	
	@SuppressWarnings("finally")
	public State SearchVaccineInfo(String vaccineID,vaccineStruct vaccinest) 
	 {
		 State status = State.Operate_Success;
		 String sql = "  SELECT *                           " + "\n"
							 + "  from C##TMP_USER.\"vaccine_info\"  " + "\n"
							 + "  WHERE \"VaccineID\"=\'"+vaccineID+"\'";
		 System.out.println(sql);
		 try 
			{
			 	Statement stmt = conn.prepareCall(sql);
				rs = stmt.executeQuery(sql);
				while (rs.next()) 
				{
					vaccinest.setVaccineID(rs.getString("VaccineID"));
					vaccinest.setAnimalID(rs.getString("AnimalID"));
					vaccinest.setUserID(rs.getString("UserID"));
					vaccinest.setVaccineName(rs.getString("VaccineName"));
					vaccinest.setInoculateDate(rs.getDate("InoculateDate"));
					vaccinest.setNote(rs.getString("Note"));
				}
				stmt.close();
			} 
			catch (SQLException e) 
			{
				status = State.Connection_Error;
				e.printStackTrace();
			}
			finally 
			{
				return status;
			}
	 }
	
	public void initDataBase() 
	{
		ArrayList<String>homeIDlist = new ArrayList<String>();
		ArrayList< String>homeList =new ArrayList<String>();
		for (int i = 3; i <= 100; i++) 
		{
			String homeID = "S";
			if (i<10) 
			{
				homeID+="000"+String.valueOf(i);
			}
			else if (i==100) 
			{
				homeID+="0100";
			}
			else 
			{
				homeID+="00"+String.valueOf(i);
			}
			homeIDlist.add(homeID);
		}

		for (int i = 0; i < 98; i++) 
		{
			String home = "";
			if (i/26==0) 
			{
				int offset = i%26;
				char tail = (char) (97+offset);
				home+="Red_"+String.valueOf(tail);
			}
			if (i/26==1) 
			{
				int offset = i%26;
				char tail = (char) (97+offset);
				home+="Blue_"+String.valueOf(tail);
			}
			if (i/26==2) 
			{
				int offset = i%26;
				char tail = (char) (97+offset);
				home+="Green_"+String.valueOf(tail);
			}
			if (i/26==3) 
			{
				int offset = i%26;
				char tail = (char) (97+offset);
				home+="Yellow_"+String.valueOf(tail);
			}
			homeList.add(home);
		}
		
		System.out.println(homeIDlist.size()==homeList.size());
		ArrayList<String>sqlList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		for (int i = 0; i < homeIDlist.size(); i++) 
		{
			sql.append("Insert into C##TMP_USER.\"shelter_info\" Values(\'");
			sql.append(homeIDlist.get(i));
			sql.append("\',");
			sql.append("\'");
			sql.append(homeList.get(i));
			sql.append("\',");
			sql.append("\'\',");
			sql.append("\'\',");
			sql.append("\'\',");
			sql.append("\'\',");
			sql.append("\'\'");
			sql.append(")");
			sqlList.add(sql.toString());
			System.out.println(sql.toString());
			sql.delete(0, sql.length());
		}
		for (String string : sqlList) 
		{
			try 
			{
				System.out.println(st.execute(string));
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	}

	private void initTables() 
	{
		adminTable.clear();
		userTable.clear();
		animalTable.clear();
		shelterTable .clear();
		healthTable.clear();
		vaccineTable.clear();
	}
	
	@SuppressWarnings("finally")
	public State GetTable(String tableName) 
	{
		initTables();
		String curTableName = tableName;
		State status = State.GetTable_Success;
		if(tableName=="adminInfoPane") 
		{
			tableName="C##TMP_USER.\"admin_info\"";
		}
		else if (tableName=="userInfoPane") 
		{
			tableName="C##TMP_USER.\"user_info\"";
		}
		else if (tableName=="animalInfoPane") 
		{
			tableName="C##TMP_USER.\"animal_info\"";
		}
		else if (tableName=="shelterInfoPane") 
		{
			tableName="C##TMP_USER.\"shelter_info\"";
		}
		else if (tableName=="healthInfoPane") 
		{
			tableName="C##TMP_USER.\"animal_health_info\"";
		}
		else if (tableName=="vaccineInfoPane") 
		{
			tableName="C##TMP_USER.\"vaccine_info\"";
		}
		else 
		{
			System.out.println("Wrong Table Name："+tableName);
			return null;
		}
		String sql = "select * from "+tableName;
		System.out.println(sql);
		
		try 
		{
			rs = st.executeQuery(sql);
			if(curTableName=="adminInfoPane") 
			{
				GetAdminTable();
			}
			else if (curTableName=="userInfoPane") 
			{
				GetUserTable();
			}
			else if (curTableName=="animalInfoPane") 
			{
				GetAnimalTable();
			}
			else if (curTableName=="shelterInfoPane") 
			{
				GetShelterTable();
			}
			else if (curTableName=="healthInfoPane") 
			{
				GetHealthTable();
			}
			else if (curTableName=="vaccineInfoPane") 
			{
				GetVaccineTable();
			}
		} 
		catch (SQLException e) 
		{
			status = State.GetTable_Error;
			e.printStackTrace();
		}
		finally 
		{
			return status;
		}
		
	}
	
	private void GetAdminTable() 
	{
			try 
			{
				while(rs.next()) 
				{
					String AdminID=rs.getString("adminID");
					String AdminName = rs.getString("adminName");
					String PassWord = rs.getString("adminPassWord");
					System.out.println(AdminID+" "+AdminName+" "+PassWord);
					adminStruct tmp=new adminStruct();
					tmp.setAdminID(AdminID);
					tmp.setAdminName(AdminName);
					tmp.setAdminPassWord(PassWord);
					adminTable.add(tmp);
				}
				rs.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
	}
	
	private void GetUserTable() 
	{
		try 
		{
			while(rs.next()) 
			{
				String UserID=rs.getString("UserID");
				String UserName = rs.getString("UserName");
				String PassWord = rs.getString("PassWord");
				String EmailAddr = rs.getString("EmailAddress");
				String PhoneNumber = rs.getString("PhoneNumber");
				String ShelterID = rs.getString("ShelterID");
				
				userStruct tmp=new userStruct();
				
				tmp.setUserID(UserID);
				tmp.setUserNameString(UserName);
				tmp.setPassWord(PassWord);
				tmp.setPhoneNumber(PhoneNumber);
				tmp.setEmailAddress(EmailAddr);
				tmp.setShelterID(ShelterID);
				
				userTable.add(tmp);
			}
			rs.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void GetShelterTable() 
	{
		try 
		{
			while(rs.next()) 
			{
				String ShelterID=rs.getString("ShelterID");
				String ShelterName = rs.getString("ShelterName");
				String ShelterAddr = rs.getString("ShelterAddr");
				Integer PostalCode = rs.getInt("PostalCode");
				Integer TotalRoomNums = rs.getInt("TotalRoomNums");
				Integer RemainingRoomNums=rs.getInt("RemainingRoomNums");
				String Note = rs.getString("Note");
				
				shelterStruct tmp = new shelterStruct();

				tmp.setShelterID(ShelterID);
				tmp.setShelterName(ShelterName);
				tmp.setShelterAddress(ShelterAddr);
				tmp.setPostalCode(PostalCode);
				tmp.setTotalRoomNums(TotalRoomNums);
				tmp.setRemainingRoomNums(RemainingRoomNums);
				tmp.setNote(Note);

				shelterTable.add(tmp);
			}
			rs.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void GetAnimalTable() 
	{
		try 
		{
			while(rs.next()) 
			{
				String AnimalID = rs.getString("AnimalID");
				String AnimalNumber = rs.getString("AnimalNumber");
				String AnimalName = rs.getString("AnimalName");
				String SpeciesKind = rs.getString("SpeciesKind");
				Integer Sex = rs.getInt("Sex");
				Integer Age = rs.getInt("Age");
				String Image = rs.getString("Image");
				String ShelterID = rs.getString("ShelterID");
				
				animalStruct tmp = new animalStruct();
				
				tmp.setAnimalID(AnimalID);
				tmp.setAnimalName(AnimalName);
				tmp.setAnimalNumber(AnimalNumber);
				tmp.setSpeciesKind(SpeciesKind);
				tmp.setSex(Sex);
				tmp.setAge(Age);
				tmp.setImage(Image);
				tmp.setShelterID(ShelterID);
				
				animalTable.add(tmp);
			}
			rs.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void GetHealthTable() 
	{
		try 
		{
			while(rs.next()) 
			{
				String HealthInfoID = rs.getString("HealthInfoID");
				String AnimalID = rs.getString("AnimalID");
				String UserID = rs.getString("UserID");
				String HealthInfo = rs.getString("HealthInfo");
				Date CheckDate = rs.getDate("CheckDate");
				String Note =rs.getString("Note");
				
				healthStruct tmp = new healthStruct();
				
				tmp.setHealthInfoID(HealthInfoID);
				tmp.setAnimalID(AnimalID);
				tmp.setUserID(UserID);
				tmp.setHealthInfo(HealthInfo);
				tmp.setCheckDate(CheckDate);
				tmp.setNote(Note);
				
				healthTable.add(tmp);
			}
			rs.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void GetVaccineTable() 
	{
		try 
		{
			while(rs.next()) 
			{
				String VaccineID = rs.getString("VaccineID");
				String AnimalID = rs.getString("AnimalID");
				String UserID = rs.getString("UserID");
				String VaccineName = rs.getString("VaccineName");
				Date inoculateDate = rs.getDate("inoculateDate");
				String Note = rs.getString("Note");
				
				vaccineStruct tmp = new vaccineStruct();
				tmp.setVaccineID(VaccineID);
				tmp.setAnimalID(AnimalID);
				tmp.setVaccineName(VaccineName);
				tmp.setUserID(UserID);
				tmp.setInoculateDate(inoculateDate);
				tmp.setNote(Note);
				
				vaccineTable.add(tmp);
			}
			rs.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	public ArrayList<adminStruct> getAdminTable() 
	{
		return adminTable;
	}

	public ArrayList<userStruct> getUserTable() 
	{
		return userTable;
	}

	public ArrayList<animalStruct> getAnimalTable() 
	{
		return animalTable;
	}

	public ArrayList<shelterStruct> getShelterTable() 
	{
		return shelterTable;
	}

	public ArrayList<healthStruct> getHealthTable() 
	{
		return healthTable;
	}

	public ArrayList<vaccineStruct> getVaccineTable() 
	{
		return vaccineTable;
	}

	
}
