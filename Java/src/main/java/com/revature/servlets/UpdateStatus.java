package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.Employee;
import com.revature.beans.RForm;
import com.revature.beans.StatusChange;
import com.revature.daoimpl.RFormDAOImpl;


public class UpdateStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setAccessControlHeaders(response);
		response.setStatus(HttpServletResponse.SC_OK);
	}
	private void setAccessControlHeaders(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Headers", "*");
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		response.setHeader("Access-Control-Allow-Methods", "*");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setAccessControlHeaders(response);
		System.out.println("In doPostUpdateStatusServlet");
				StatusChange sc = null;
				ObjectMapper mapper = new ObjectMapper();
				//convert JSON to java object
				sc = mapper.readValue(request.getInputStream(), StatusChange.class);
//				if(sc.getFinalGrade() != 0 || sc.getFinalPres() !=null) {
//					RFormDAOImpl rdi = new RFormDAOImpl();
//					try {
//						rdi.finalizeStatus(rf, sender);
//						PrintWriter pw = response.getWriter();
//						pw.write("updated");
//						pw.close();
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				} else {}
					RFormDAOImpl rdi = new RFormDAOImpl();
					try {
						rdi.updateStatus(sc);
						PrintWriter pw = response.getWriter();
						pw.print("updated");
						pw.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				
		
	}

}
