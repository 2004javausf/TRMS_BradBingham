package com.revature.beans;

public class Message {
	private int ID;
	private String submittedOn;
	private int sendID;
	private int recID;
	private int formID;
	private String message;
	
	public Message(int iD, String submittedOn, int sendID, int recID, int formID, String message) {
		super();
		ID = iD;
		this.submittedOn = submittedOn;
		this.sendID = sendID;
		this.recID = recID;
		this.formID = formID;
		this.message = message;
	}
	public Message() {
		super();
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getSubmittedOn() {
		return submittedOn;
	}
	public void setSubmittedOn(String submittedOn) {
		this.submittedOn = submittedOn;
	}
	public int getSendID() {
		return sendID;
	}
	public void setSendID(int sendID) {
		this.sendID = sendID;
	}
	public int getRecID() {
		return recID;
	}
	public void setRecID(int recID) {
		this.recID = recID;
	}
	public int getFormID() {
		return formID;
	}
	public void setFormID(int formID) {
		this.formID = formID;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "Message [ID=" + ID + ", submittedOn=" + submittedOn + ", sendID=" + sendID + ", recID=" + recID
				+ ", formID=" + formID + ", message=" + message + "]";
	}
	
}
