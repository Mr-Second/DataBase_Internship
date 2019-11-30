package com.structs;

import java.sql.Date;

public class vaccineStruct 
{
	private String VaccineID=null;
	private String AnimalID=null;
	private String UserID=null;
	private String VaccineName=null;
	private Date InoculateDate=null;
	private String Note=null;
	
	public String getVaccineID() {
		return VaccineID;
	}

	public void setVaccineID(String vaccineID) {
		VaccineID = vaccineID==null ? "" : vaccineID;
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

	public String getVaccineName() {
		return VaccineName;
	}

	public void setVaccineName(String vaccineName) {
		VaccineName = vaccineName==null ? "" : vaccineName;
	}

	public Date getInoculateDate() {
		return InoculateDate;
	}

	public void setInoculateDate(Date inoculateDate) {
		InoculateDate = inoculateDate==null ? new Date(0) : inoculateDate;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note==null ? "" : note;
	}
	
	public static String[] toArray() 
	{
		String[]vaccineTableHeader= {"VaccineID","AnimalID","UserID","VaccineName","InoculateDate","Note"};
		return vaccineTableHeader;
	}

	
}
