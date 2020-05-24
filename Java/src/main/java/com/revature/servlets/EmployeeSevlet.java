package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.Employee;
import com.revature.daoimpl.EmployeeDAOImpl;


public class EmployeeSevlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("In doGet EmployeeServlet");
		String filter = request.getPathInfo().substring(1);
		
		ObjectMapper mapper = new ObjectMapper();
		EmployeeDAOImpl edi = new EmployeeDAOImpl();
		PrintWriter pw = response.getWriter();
		String emJSON;
		if(filter.equals("")) {
			try {
				emJSON = mapper.writeValueAsString(edi.getEmployeeList());
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				pw.print(emJSON);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			pw.flush();
		} else {
			try {
				emJSON = mapper.writeValueAsString(edi.getEmployeeBy(filter));
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				pw.print(emJSON);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			pw.flush();
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("In doPost EmployeeServlet");
		Employee em = null;
		ObjectMapper mapper = new ObjectMapper();
		//convert JSON to java object
		em = mapper.readValue(request.getInputStream(),Employee.class);
		EmployeeDAOImpl edi = new EmployeeDAOImpl();
		Employee checkEm = edi.getEmployeeBy(em.getUsername());
		PrintWriter pw = response.getWriter();
		if(checkEm.getPassword().equals(em.getPassword())) {
			String emJSON = mapper.writeValueAsString(checkEm);
			pw.write(emJSON);
			pw.close();
			response.setStatus(201);			
		}
		pw.write("Incorrect Username or Password");
		pw.close();
		
	}

}
