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
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setAccessControlHeaders(response);
		System.out.println("In doPostUpdateStatusServlet");
		RFormDAOImpl rdi = new RFormDAOImpl();
		int nextVal = 0;
		try {
			nextVal = rdi.getNextFormId();
			System.out.println(nextVal);
			PrintWriter pw = response.getWriter();
			pw.write(Integer.toString(nextVal));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setAccessControlHeaders(response);
		System.out.println("In doPost UpdateStatusServlet");
				StatusChange sc = null;
				ObjectMapper mapper = new ObjectMapper();
				//convert JSON to java object
				sc = mapper.readValue(request.getInputStream(), StatusChange.class);
					RFormDAOImpl rdi = new RFormDAOImpl();
					try {
						rdi.updateStatus(sc);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				
		
	}

}
