package com.revature.daoimpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Message;
import com.revature.beans.RForm;
import com.revature.dao.MessageDAO;
import com.revature.util.ConnFactory;

public class MessageDAOImpl implements MessageDAO {
	public static ConnFactory cf = ConnFactory.getInstance();
	
	@Override
	public void insertMessage(Message m) throws SQLException {
		String sql = "{ call INSERT_MESSAGE(?,?,?,?)";
		Connection conn = cf.getConnection();
		CallableStatement call = conn.prepareCall(sql);
		call.setInt(1, m.getSendID());
		call.setInt(2, m.getRecID());
		call.setInt(3, m.getFormID());
		call.setString(4, "Sender: "+m.getSendID()+" Form: "+m.getFormID()+" : "+m.getMessage());
		call.execute();
		call.close();
		conn.close();
		
	}

	@Override
	public List<Message> getMessageList() throws SQLException {
		String sql = "SELECT * FROM MESSAGES";
		Connection conn = cf.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		Message m = null;
		ArrayList<Message> mList = new ArrayList<>();
		while (rs.next()) {
			m = new Message(
					rs.getInt(1),
					rs.getString(2),
					rs.getInt(3),
					rs.getInt(4),
					rs.getInt(5),
					rs.getString(6)
					);
			mList.add(m);
		}
		conn.close();
		return mList;
	}
	
	
			@Override
			public List<Message> getMessagesById(int id) throws SQLException {
				String sql = "SELECT * FROM MESSAGES WHERE RECIPIANT_EMPLOYEE_ID ="+id;
				Connection conn = cf.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				Message m = null;
				ArrayList<Message> mList = new ArrayList<>();
				while (rs.next()) {
					m = new Message(
							rs.getInt(1),
							rs.getString(2),
							rs.getInt(3),
							rs.getInt(4),
							rs.getInt(5),
							rs.getString(6)
							);
					mList.add(m);
				}
				conn.close();
				return mList;
			}

			@Override
			public void deleteMessage(String id) throws SQLException {
				String sql="DELETE FROM MESSAGES WHERE ID ="+id;
				Connection conn = cf.getConnection();
				Statement stmt = conn.createStatement();
				stmt.executeQuery(sql);
				conn.close();
			}
}
