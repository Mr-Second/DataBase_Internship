package com.structs;


public class userStruct 
{
	private String UserID=null;
	private String UserNameString = null;
	private String PassWord = null;
	private String EmailAddress =null;
	private String PhoneNumber = null;
	private String ShelterID = null;
	
	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID==null ? "" : userID;
	}

	public String getUserNameString() {
		return UserNameString;
	}

	public void setUserNameString(String userNameString) {
		UserNameString = userNameString==null ? "" : userNameString;
	}

	public String getPassWord() {
		return PassWord;
	}

	public void setPassWord(String passWord) {
		PassWord = passWord==null ? "" : passWord;
	}

	public String getEmailAddress() {
		return EmailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		EmailAddress = emailAddress==null ? "" : emailAddress;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber==null ? "" : phoneNumber;
	}

	public String getShelterID() {
		return ShelterID;
	}

	public void setShelterID(String shelterID) {
		ShelterID = shelterID==null ? "" : shelterID;
	}

	public static String[] toArray() 
	{
		String[]userTableHeaders= {"UserID","UserName","PassWord","EmailAddress","PhoneNumber","ShelterID"};
		return userTableHeaders;
	}
}
