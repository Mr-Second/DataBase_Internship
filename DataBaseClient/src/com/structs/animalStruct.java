package com.structs;

public class animalStruct 
{
	private String AnimalID = null;
	private String AnimalNumber = null;
	private String AnimalName=null;
	private String SpeciesKind=null;
	private Integer Sex=0;					//0为雄，1为雌
	private Integer Age=0;
	private String Image=null;
	private String ShelterID=null;
	
	public String getAnimalID() {
		return AnimalID;
	}

	public void setAnimalID(String animalID) {
		AnimalID = animalID==null ? "" : animalID;
	}

	public String getAnimalNumber() {
		return AnimalNumber;
	}

	public void setAnimalNumber(String animalNumber) {
		AnimalNumber = animalNumber==null ? "" : animalNumber;
	}

	public String getAnimalName() {
		return AnimalName;
	}

	public void setAnimalName(String animalName) {
		AnimalName = animalName==null ? "" : animalName;
	}

	public String getSpeciesKind() {
		return SpeciesKind;
	}

	public void setSpeciesKind(String speciesKind) {
		SpeciesKind = speciesKind==null ? "" : speciesKind;
	}

	public Integer getSex() {
		return Sex;
	}

	public void setSex(Integer sex) {
		Sex = sex==null ? -1 : sex;
	}

	public Integer getAge() {
		return Age;
	}

	public void setAge(Integer age) {
		Age = age==null ? -1 : age;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public String getShelterID() {
		return ShelterID;
	}

	public void setShelterID(String shelterID) {
		ShelterID = shelterID==null ? "" : shelterID;
	}

	public static String[] toArray() 
	{
		String[]animalTableHeaders= {"AnimalID","AnimalNumber","AnimalName","SpeciesKind","Sex","Age","Image","ShelterID"};
		return animalTableHeaders;
	} 
}
