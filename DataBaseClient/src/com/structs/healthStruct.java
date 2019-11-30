package com.structs;

import java.sql.Date;


public class healthStruct 
{
	private String HealthInfoID=null;
	private String AnimalID=null;
	private String UserID=null;
	private String HealthInfo=null;
	private Date CheckDate=null;
	private String Note=null;
	
	public String getHealthInfoID() {
		return HealthInfoID;
	}

	public void setHealthInfoID(String healthInfoID) {
		HealthInfoID = healthInfoID==null ? "" : healthInfoID;
	}

	public String getAnimalID() {
		return AnimalID;
	}

	public void setAnimalID(String animalID) {
		AnimalID = animalID==null ? "" : animalID;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID==null ? "" : userID;
	}

	public String getHealthInfo() {
		return HealthInfo;
	}

	public void setHealthInfo(String healthInfo) {
		HealthInfo = healthInfo==null ? "" : healthInfo;
	}

	public Date getCheckDate() {
		return CheckDate;
	}

	public void setCheckDate(Date checkDate) {
		CheckDate = checkDate==null ? new Date(0) : checkDate;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note==null ? "" : note;
	}

	public static String[] toArray() 
	{
		String[]healthTableHeaders= {"HealthInfoID","AnimalID","UserID","HealthInfo","CheckDate","Note"};
		return healthTableHeaders;
	}
}
