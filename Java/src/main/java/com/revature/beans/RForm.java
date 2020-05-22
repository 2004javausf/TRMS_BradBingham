package com.revature.beans;

import java.sql.Blob;

public class RForm {
	private int ID;
	private int empID;
	private String status;
	private String supApr;
	private String supSubDate;
	private String headArp;
	private String headSubDate;
	private String coorApr;
	private String coorSubDate;
	private String isAltered;
	private String rejectMessage;
	private String formSubDate;
	private String startDate;
	private String startTime;
	private String location;
	private double cost;
	private String description;
	private String justification;
	private int gradeFormID;
	private String eventType;
	private Blob onSubmit;
	private double finalGrade;
	private String gradeApr;
	private Blob finalPres;
	private String presApr;
	
	
	public RForm(int empID, String startDate, String startTime, String location, double cost, String description,
			String justification, int gradeFormID, String eventType, Blob onSubmit) {
		super();
		this.empID = empID;
		this.startDate = startDate;
		this.startTime = startTime;
		this.location = location;
		this.cost = cost;
		this.description = description;
		this.justification = justification;
		this.gradeFormID = gradeFormID;
		this.eventType = eventType;
		this.onSubmit = onSubmit;
	}

	public RForm(int iD, int empID, String status, String supApr, String supSubDate, String headArp, String headSubDate,
			String coorApr, String coorSubDate, String isAltered, String rejectMessage, String formSubDate,
			String startDate, String startTime, String location, double cost, String description, String justification,
			int gradeFormID, String eventType, Blob onSubmit, double finalGrade, String gradeApr, Blob finalPres,
			String presApr) {
		super();
		ID = iD;
		this.empID = empID;
		this.status = status;
		this.supApr = supApr;
		this.supSubDate = supSubDate;
		this.headArp = headArp;
		this.headSubDate = headSubDate;
		this.coorApr = coorApr;
		this.coorSubDate = coorSubDate;
		this.isAltered = isAltered;
		this.rejectMessage = rejectMessage;
		this.formSubDate = formSubDate;
		this.startDate = startDate;
		this.startTime = startTime;
		this.location = location;
		this.cost = cost;
		this.description = description;
		this.justification = justification;
		this.gradeFormID = gradeFormID;
		this.eventType = eventType;
		this.onSubmit = onSubmit;
		this.finalGrade = finalGrade;
		this.gradeApr = gradeApr;
		this.finalPres = finalPres;
		this.presApr = presApr;
	}

	public RForm() {
		super();
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getEmpID() {
		return empID;
	}

	public void setEmpID(int empID) {
		this.empID = empID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSupApr() {
		return supApr;
	}

	public void setSupApr(String supApr) {
		this.supApr = supApr;
	}

	public String getSupSubDate() {
		return supSubDate;
	}

	public void setSupSubDate(String supSubDate) {
		this.supSubDate = supSubDate;
	}

	public String getHeadArp() {
		return headArp;
	}

	public void setHeadArp(String headArp) {
		this.headArp = headArp;
	}

	public String getHeadSubDate() {
		return headSubDate;
	}

	public void setHeadSubDate(String headSubDate) {
		this.headSubDate = headSubDate;
	}

	public String getCoorApr() {
		return coorApr;
	}

	public void setCoorApr(String coorApr) {
		this.coorApr = coorApr;
	}

	public String getCoorSubDate() {
		return coorSubDate;
	}

	public void setCoorSubDate(String coorSubDate) {
		this.coorSubDate = coorSubDate;
	}

	public String getIsAltered() {
		return isAltered;
	}

	public void setIsAltered(String isAltered) {
		this.isAltered = isAltered;
	}

	public String getRejectMessage() {
		return rejectMessage;
	}

	public void setRejectMessage(String rejectMessage) {
		this.rejectMessage = rejectMessage;
	}

	public String getFormSubDate() {
		return formSubDate;
	}

	public void setFormSubDate(String formSubDate) {
		this.formSubDate = formSubDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public int getGradeFormID() {
		return gradeFormID;
	}

	public void setGradeFormID(int gradeFormID) {
		this.gradeFormID = gradeFormID;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Blob getOnSubmit() {
		return onSubmit;
	}

	public void setOnSubmit(Blob onSubmit) {
		this.onSubmit = onSubmit;
	}

	public double getFinalGrade() {
		return finalGrade;
	}

	public void setFinalGrade(double finalGrade) {
		this.finalGrade = finalGrade;
	}

	public String getGradeApr() {
		return gradeApr;
	}

	public void setGradeApr(String gradeApr) {
		this.gradeApr = gradeApr;
	}

	public Blob getFinalPres() {
		return finalPres;
	}

	public void setFinalPres(Blob finalPres) {
		this.finalPres = finalPres;
	}

	public String getPresApr() {
		return presApr;
	}

	public void setPresApr(String presApr) {
		this.presApr = presApr;
	}

	@Override
	public String toString() {
		return "RForm [ID=" + ID + ", empID=" + empID + ", status=" + status + ", supApr=" + supApr + ", supSubDate="
				+ supSubDate + ", headArp=" + headArp + ", headSubDate=" + headSubDate + ", coorApr=" + coorApr
				+ ", coorSubDate=" + coorSubDate + ", isAltered=" + isAltered + ", rejectMessage=" + rejectMessage
				+ ", formSubDate=" + formSubDate + ", startDate=" + startDate + ", startTime=" + startTime
				+ ", location=" + location + ", cost=" + cost + ", description=" + description + ", justification="
				+ justification + ", gradeFormID=" + gradeFormID + ", eventType=" + eventType + ", onSubmit=" + onSubmit
				+ ", finalGrade=" + finalGrade + ", gradeApr=" + gradeApr + ", finalPres=" + finalPres + ", presApr="
				+ presApr + "]";
	}

}
