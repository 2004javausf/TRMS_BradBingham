package com.revature.daoimpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Employee;
import com.revature.beans.RForm;
import com.revature.beans.StatusChange;
import com.revature.dao.RFormDAO;
import com.revature.util.ConnFactory;

import oracle.sql.BLOB;

public class RFormDAOImpl implements RFormDAO {
	public static ConnFactory cf = ConnFactory.getInstance();

	@Override
	public void insertForm(RForm rf) throws SQLException {
		String sql = "{ call INSERT_R_FORM(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = cf.getConnection();
		CallableStatement call = conn.prepareCall(sql);
		call.setInt(1, rf.getEmpID());
		call.setString(2, formatDate(rf.getStartDate()));
		call.setString(3, rf.getStartTime());
		call.setString(4, rf.getLocation());
		call.setDouble(5, rf.getCost());
		call.setDouble(6, rf.getPendingRe());
		call.setString(7, rf.getDescription());
		call.setString(8, rf.getJustification());
		call.setInt(9, rf.getGradeFormatID());
		call.setString(10, rf.getEventType());
		call.setString(11, rf.getOnSubmit());
		call.setString(12, rf.getIsUrgent());
		call.setString(13, rf.getSupApr());
		call.setString(14, formatDate(rf.getSupSubDate()));
		call.setString(15, rf.getHeadApr());
		call.setString(16, formatDate(rf.getHeadSubDate()));
		
		call.execute();
		call.close();
		conn.close();
	}

	private static String formatDate(String test) {
		StringBuilder sb = new StringBuilder();
		if(test == null)
			return null;
		sb.append(test.substring(8, 10));
		String key = test.substring(5, 7);
		switch (key) {
		case "01":
			sb.append("-JAN-");
			break;
		case "02":
			sb.append("-FEB-");
			break;
		case "03":
			sb.append("-MAR-");
			break;
		case "04":
			sb.append("-APR-");
			break;
		case "05":
			sb.append("-MAY-");
			break;
		case "06":
			sb.append("-JUN-");
			break;
		case "07":
			sb.append("-JUL-");
			break;
		case "08":
			sb.append("-AUG-");
			break;
		case "09":
			sb.append("-SEP-");
			break;
		case "10":
			sb.append("-OCT-");
			break;
		case "11":
			sb.append("-NOV-");
			break;
		case "12":
			sb.append("-DEC-");
			break;
		default:
			break;
		}
		sb.append(test.substring(0, 4));
			return sb.toString();			
		
	}
	private ArrayList<RForm> runQuery(String sql) throws SQLException{
		Connection conn = cf.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		RForm rf = null;
		ArrayList<RForm> rfList = new ArrayList<>();
		while (rs.next()) {
			rf = new RForm(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
					rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
					rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15),
					rs.getDouble(16), rs.getDouble(17), rs.getString(18), rs.getString(19), rs.getInt(20),
					rs.getString(21), rs.getString(22), rs.getDouble(23), rs.getString(24), rs.getString(25),
					rs.getString(26),rs.getString(27));
			rfList.add(rf);
		}
		conn.close();
		return rfList;
	}

	@Override
	public ArrayList<RForm> getFormList() throws SQLException {
		String sql = "SELECT * FROM R_FORMS";
		return runQuery(sql);
	}

	@Override
	public List<RForm> getFormsById(int id) throws SQLException {
		String sql = "SELECT * FROM R_FORMS WHERE EMPLOYEE_ID =" + id;
		return runQuery(sql);
	}

	
	private void awardToID(int id) throws SQLException{
		Connection conn = cf.getConnection();
		try {
			String award;  
			award="UPDATE EMPLOYEES "  
				+ "SET "  
				+ "AVAILABLE_AMOUNT = (AVAILABLE_AMOUNT - (SELECT PENDING_REIMBURSEMENT FROM R_FORMS WHERE R_FORMS.ID = ?)) "  
				+ "WHERE "  
				+ "EMPLOYEES.ID = (SELECT EMPLOYEE_ID FROM R_FORMS WHERE R_FORMS.ID = ?) ";
			
			PreparedStatement ps = conn.prepareStatement(award);
			ps.setInt(1, id);
			ps.setInt(2, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			conn.close();
		}
		
	}
	
	private void sendMessage(StatusChange sc, String msg) throws SQLException{
		Connection conn = cf.getConnection();
		try {
			String message;
			message = "INSERT INTO MESSAGES VALUES("  
					+ "MESSAGE_SEQ.NEXTVAL,"  
					+ "CURRENT_TIMESTAMP,"  
					+ "?,"  
					+ "?," 
					+ "?," 
					+ "?)";
			
			PreparedStatement ps = conn.prepareStatement(message);
			ps.setInt(1, sc.getAprId());
			ps.setInt(2, sc.getEmpId());
			ps.setInt(3, sc.getRfId());
			ps.setString(4, msg);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			conn.close();
		}
		
	}
	
	@Override
	public void updateStatus(StatusChange sc) throws SQLException {
		String sql = null;
		switch (sc.getTitle()) {
		case "Supervisor":
			switch (sc.getNewStatus()) {
			case "ApproveForm": 
			sql = "UPDATE R_FORMS SET APPROVE_Supervisor = 'Approved',"
				+ "Supervisor_SUBMIT_DATE = CURRENT_TIMESTAMP"
				+ " WHERE ID ="+sc.getRfId();
				sendMessage(sc, "Supervisor "+sc.getAprId()+" approved form #"+sc.getRfId());
				break;
			case "DenyForm":
			sql = "UPDATE R_FORMS SET APPROVE_Supervisor = 'Declined',"
				+ "Supervisor_SUBMIT_DATE = CURRENT_TIMESTAMP,"
				+ "REJECTION_JUSTIFY = '"+sc.getReason()+"', "
				+ "FORM_STATUS = 'Denied' WHERE ID ="+sc.getRfId();
				sendMessage(sc, "Supervisor "+sc.getAprId()+" declined form #"+sc.getRfId()+"\n"+sc.getReason());
				break;
			case "ConfirmGrade": 
			sql = "UPDATE R_FORMS SET FORM_STATUS = 'Approved' "
				+ "WHERE ID ="+sc.getRfId();
				awardToID(sc.getEmpId());
				sendMessage(sc, "Reienbursement from form #"+sc.getRfId()+" has been awarded");
				break;
			case "RejectGrade":
			sql = "UPDATE R_FORMS SET FORM_STATUS = 'Rejected',"
				+ "APPROVE_GRADE = 'Rejected',"
				+ "REJECTION_JUSTIFY = 'Not a passing Grade' "
				+ "WHERE ID ="+sc.getRfId();
				sendMessage(sc, "Reienbursement from form #"+sc.getRfId()+" has been denied"+"\n"+sc.getReason());
				break;
			default:
				break;
			}
			break;
		case "Head":
			switch (sc.getNewStatus()) {
			case "ApproveForm": 
			sql = "UPDATE R_FORMS SET APPROVE_Head = 'Approved',"
				+ "Head_SUBMIT_DATE = CURRENT_TIMESTAMP"
				+ " WHERE ID ="+sc.getRfId();
				sendMessage(sc, "Head Manager "+sc.getAprId()+" approved form #"+sc.getRfId());
				break;
			case "DenyForm":
			sql = "UPDATE R_FORMS SET APPROVE_Head = 'Declined',"
				+ "Head_SUBMIT_DATE = CURRENT_TIMESTAMP,"
				+ "REJECTION_JUSTIFY = '"+sc.getReason()+"', "
				+ "FORM_STATUS = 'Denied' WHERE ID ="+sc.getRfId();
				sendMessage(sc, "Head Manager "+sc.getAprId()+" approved form #"+sc.getRfId()+sc.getReason());
				break;
			default:
				break;
			}
			break;
		case "Associate":
			
			switch (sc.getNewStatus()) {
			case "ApproveForm": 
			sql = "UPDATE R_FORMS SET APPROVE_COORDINATOR = 'Approved',"
				+ "COORDINATOR_SUBMIT_DATE = CURRENT_TIMESTAMP,"
				+ "FORM_STATUS = 'Pending' WHERE ID ="+sc.getRfId();
				sendMessage(sc, "Benefits Coordinator "+sc.getAprId()+" approved form #"+sc.getRfId());
				break;
			case "DenyForm":
			sql = "UPDATE R_FORMS SET APPROVE_COORDINATOR = 'Declined',"
				+ "COORDINATOR_SUBMIT_DATE = CURRENT_TIMESTAMP,"
				+ "REJECTION_JUSTIFY = '"+sc.getReason()+"', "
				+ "FORM_STATUS = 'Denied' WHERE ID ="+sc.getRfId();
				sendMessage(sc, "Benefits Coordinator "+sc.getAprId()+" declined form #"+sc.getRfId()+"\n"+sc.getReason());
				break;
			case "AcceptBenCoOffer": 
			sql = "UPDATE R_FORMS SET ALTERED_FORM = 'Approved',"
				+ "FORM_STATUS = 'In-review' WHERE ID ="+sc.getRfId();
				sendMessage(sc, "Associate "+sc.getAprId()+" accepted alternate offer on form #"+sc.getRfId());
				break;
			case "DeclineBenCoOffer":
			sql = "UPDATE R_FORMS SET ALTERED_FORM = 'Declined',"
				+ "FORM_STATUS = 'Canceled' WHERE ID ="+sc.getRfId();
				sendMessage(sc, "Associate "+sc.getAprId()+" delined alternate offer on form #"+sc.getRfId());
				break;
			case "SubmitFinalGrade": 
			sql = "UPDATE R_FORMS SET ON_FINISH_GRADE = "+sc.getReason()
				+ " WHERE ID ="+sc.getRfId();
				sendMessage(sc, "Associate "+sc.getAprId()+" submitted final grade "+sc.getReason()+" for form #"+sc.getRfId());
				break;
			case "SubmitFinalPres":
			sql = "UPDATE R_FORMS SET ON_FINISH_PRESENTATION = '"+sc.getReason()
				+ "' WHERE ID ="+sc.getRfId();
				sendMessage(sc, "Associate "+sc.getAprId()+" submitted final presentation for form #"+sc.getRfId());
				break;
			case "ConfirmPres": 
			sql = "UPDATE R_FORMS SET FORM_STATUS = 'Approved' "
				+ "WHERE ID ="+sc.getRfId();
				awardToID(sc.getEmpId());
				sendMessage(sc, "Reienbursement from form #"+sc.getRfId()+" has been awarded");
				break;
			case "RejectPres":
			sql = "UPDATE R_FORMS SET FORM_STATUS = 'Rejected',"
				+ "APPROVE_PRESENTATION = 'Rejected',"
				+ "REJECTION_JUSTIFY = 'Not a passing Presentation' "
				+ "WHERE ID ="+sc.getRfId();
				sendMessage(sc, "Reienbursement from form #"+sc.getRfId()+" has been denied"+"\n"+sc.getReason());
				break;
			case "AlterForm": 
			sql = "UPDATE R_FORMS SET ALTERED_FORM = 'True', "
				+ "PENDING_REIMBURSEMENT = "+sc.getReason()
				+ "WHERE ID ="+sc.getRfId();
				sendMessage(sc, "Benefits Coordinator#"+sc.getAprId()+" has altered form "+sc.getRfId()+"\nPlease review the new offer");
				break;	
			default:
				break;
			} 
			break;
		default:
			break;
		}
		Connection conn = cf.getConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			conn.close();
		}
	}

	public int getNextFormId() throws SQLException {
		String sql = "SELECT R_FORM_SEQ.NEXTVAL FROM DUAL";
		int result = 0;
		Connection conn = cf.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		result = rs.getInt(1);
		conn.close();
		return result;
	}
	
	public List<RForm> getManagerEmployees(Employee em) throws SQLException{
		System.out.println(em);
		String filter = "";
		String deptFilter = "AND DEPARTMENT = '"+em.getDepartment();
		switch (em.getTitle()) {
		case "Supervisor": filter = deptFilter+"' AND (r_forms.approve_supervisor IS NULL OR r_forms.form_status = 'Pending')" ;break;
		case "Head": filter = " AND r_forms.approve_supervisor = 'Approved' AND r_forms.approve_head IS NULL"; break;
		case "Associate": 
			if(em.getDepartment().equals("Benefits")) {
				filter = "AND r_forms.approve_head = 'Approved'"; 
			} else filter = "AND r_forms.approve_head = 'Not a Manager'";break;
		default: break;
		}

		String sql =
				"SELECT * FROM R_FORMS INNER JOIN EMPLOYEES ON (R_FORMS.EMPLOYEE_ID = EMPLOYEES.ID) \r\n" + 
				"WHERE OFFICE_LOC = '"+em.getOfficeLoc()+"' "+filter;
		return runQuery(sql);
	}
}
