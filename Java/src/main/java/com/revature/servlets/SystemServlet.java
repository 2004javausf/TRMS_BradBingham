package com.revature.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.daoimpl.EmployeeDAOImpl;


public class SystemServlet extends HttpServlet {
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

	static class Test{
		private String req;

		public Test() {
			super();
		}
		public String getReq() {
			return req;
		}
		public void setReq(String req) {
			this.req = req;
		}
		@Override
		public String toString() {
			return "Test [req=" + req + "]";
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setAccessControlHeaders(response);
		System.out.println("In doPost SystemServlet");
		ObjectMapper mapper = new ObjectMapper();
		Test test = null;
		test = mapper.readValue(request.getInputStream(), Test.class);
		System.out.println(test.getReq());
		switch (test.getReq()) {
		case "ResetAllAvailable":
			EmployeeDAOImpl edi = new EmployeeDAOImpl();
			try {
				edi.resetAvailableAmountAll();
			} catch (SQLException e) {
				e.printStackTrace();
			}			
			break;

		default:
			break;
		}
	}

}
