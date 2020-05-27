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
import com.revature.beans.Message;
import com.revature.daoimpl.EmployeeDAOImpl;
import com.revature.daoimpl.MessageDAOImpl;

public class MessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setAccessControlHeaders(response);
		response.setStatus(HttpServletResponse.SC_OK);
	}
	private void setAccessControlHeaders(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		response.setHeader("Access-Control-Allow-Methods", "*");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setAccessControlHeaders(response);
		System.out.println("In doGet MessageServlet");
		String filter = request.getPathInfo().substring(1);
		
		ObjectMapper mapper = new ObjectMapper();
		MessageDAOImpl mdi = new MessageDAOImpl();
		PrintWriter pw = response.getWriter();
		String msJSON;
		if(filter.equals("")) {
			try {
				msJSON = mapper.writeValueAsString(mdi.getMessageList());
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				pw.print(msJSON);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			pw.flush();			
		} else {
			try {
				msJSON = mapper.writeValueAsString(mdi.getMessagesById(Integer.parseInt(filter)));
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				pw.print(msJSON);
			} catch (JsonProcessingException | NumberFormatException | SQLException e) {
				e.printStackTrace();
			}
			pw.flush();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setAccessControlHeaders(response);
		System.out.println("In doPost MessageServlet");
		Message ms = null;
		ObjectMapper mapper = new ObjectMapper();
		ms = mapper.readValue(request.getInputStream(), Message.class);
		System.out.println(ms);
		MessageDAOImpl mdi = new MessageDAOImpl();
		try {
			mdi.insertMessage(ms);
			PrintWriter pw = response.getWriter();
			pw.write("Added Message");
			pw.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
