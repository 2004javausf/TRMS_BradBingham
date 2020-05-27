package com.revature.beans;

public class StatusChange {
	int rfId;
	int empId;
	int aprId;
	String title;
	String newStatus;
	String reason;

	public StatusChange(int rfId, int empId, int aprId, String title, String newStatus, String reason) {
		super();
		this.rfId = rfId;
		this.empId = empId;
		this.aprId = aprId;
		this.title = title;
		this.newStatus = newStatus;
		this.reason = reason;
	}

	public StatusChange() {
		super();
	}

	public int getRfId() {
		return rfId;
	}

	public void setRfId(int rfId) {
		this.rfId = rfId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public int getAprId() {
		return aprId;
	}

	public void setAprId(int aprId) {
		this.aprId = aprId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return "StatusChange [rfId=" + rfId + ", empId=" + empId + ", aprId=" + aprId + ", title=" + title
				+ ", newStatus=" + newStatus + ", reason=" + reason + "]";
	}

}
