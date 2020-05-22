package com.revature.dao;

import java.sql.SQLException;
import java.util.List;

import com.revature.beans.Message;

public interface MessageDAO {

	public void insertMessage(Message m) throws SQLException;

	public List<Message> getMessageList() throws SQLException;

}
