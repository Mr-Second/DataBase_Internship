package com.structs;

//	private String[]adminTableHeaders= {"adminID","adminName","adminPassWord"};

public class adminStruct 
{
	private String adminID =null;
	private String adminName = null;
	private String adminPassWord = null;
	
	public String getAdminID() {
		return adminID;
	}

	public void setAdminID(String adminID) {
		this.adminID = adminID==null ? "" : adminID;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName==null ? "" : adminName;
	}

	public String getAdminPassWord() {
		return adminPassWord;
	}

	public void setAdminPassWord(String adminPassWord) {
		this.adminPassWord = adminPassWord==null ? "" : adminPassWord;
	}

	public static String[] toArray() 
	{
		String[]adminTableHeaders= {"adminID","adminName","adminPassWord"};
		return adminTableHeaders;
	}
}
