package com.structs;
//
public class shelterStruct 
{
	private String ShelterID=null;
	private String ShelterName=null;
	private String ShelterAddress=null;
	private Integer PostalCode=000000;
	private Integer TotalRoomNums=0;
	private Integer RemainingRoomNums=0;
	private String Note=null;
	
	public String getShelterID() {
		return ShelterID;
	}

	public void setShelterID(String shelterID) {
		ShelterID = shelterID==null ? "" : shelterID;
	}

	public String getShelterName() {
		return ShelterName;
	}

	public void setShelterName(String shelterName) {
		ShelterName = shelterName==null ? "" : shelterName;
	}

	public String getShelterAddress() {
		return ShelterAddress;
	}

	public void setShelterAddress(String shelterAddress) {
		ShelterAddress = shelterAddress==null ? "" : shelterAddress;
	}

	public Integer getPostalCode() {
		return PostalCode;
	}

	public void setPostalCode(Integer postalCode) {
		PostalCode = postalCode==null ? -1 : postalCode;
	}

	public Integer getTotalRoomNums() {
		return TotalRoomNums;
	}

	public void setTotalRoomNums(Integer totalRoomNums) {
		TotalRoomNums = totalRoomNums==null ? -1 : totalRoomNums;
	}

	public Integer getRemainingRoomNums() {
		return RemainingRoomNums;
	}

	public void setRemainingRoomNums(Integer remainingRoomNums) {
		RemainingRoomNums = remainingRoomNums==null ? -1 : remainingRoomNums;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note==null ? "" : note;
	}

	public static String[] toArray() 
	{
		String[]shelterTableHeaders= {"ShelterID","ShelterName","ShelterAddress","PostalCode","TotalRoomNums","RemainingRoomNums","Note"};
		return shelterTableHeaders;
	}
}
