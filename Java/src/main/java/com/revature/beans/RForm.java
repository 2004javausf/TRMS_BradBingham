package com.revature.beans;

public class RForm {
	private int id;
	private String contents;

	public RForm(int id, String contents) {
		super();
		this.id = id;
		this.contents = contents;
	}

	public RForm() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	@Override
	public String toString() {
		return "ReimbursementForm [id=" + id + ", contents=" + contents + "]";
	}

}
