package com.revature.beans;

public class Employee {
	private int ID;
	private String firstName;
	private String lastName;
	private double availableAmount;
	private String title;
	private String department;
	private String officeLoc;

	
	public Employee(int iD, String firstName, String lastName, double availableAmount, String title, String department,
			String officeLoc) {
		super();
		ID = iD;
		this.firstName = firstName;
		this.lastName = lastName;
		this.availableAmount = availableAmount;
		this.title = title;
		this.department = department;
		this.officeLoc = officeLoc;
	}

	public Employee() {
		super();
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public double getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(double availableAmount) {
		this.availableAmount = availableAmount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getOfficeLoc() {
		return officeLoc;
	}

	public void setOfficeLoc(String officeLoc) {
		this.officeLoc = officeLoc;
	}

	@Override
	public String toString() {
		return "Employee [ID=" + ID + ", firstName=" + firstName + ", lastName=" + lastName + ", availableAmount="
				+ availableAmount + ", title=" + title + ", department=" + department + ", officeLoc=" + officeLoc
				+ "]";
	}

}
