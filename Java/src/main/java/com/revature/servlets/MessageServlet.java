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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("In doGet MessageServlet");
		ObjectMapper mapper = new ObjectMapper();
		MessageDAOImpl mdi = new MessageDAOImpl();
		PrintWriter pw = response.getWriter();
		String msJSON;
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
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("In doPost MessageServlet");
		Message ms = null;
		ObjectMapper mapper = new ObjectMapper();
		ms = mapper.readValue(request.getInputStream(), Message.class);
		System.out.println(ms);
		MessageDAOImpl mdi = new MessageDAOImpl();
		try {
			mdi.insertMessage(ms);
			PrintWriter pw = response.getWriter();
			pw.write("<h3>Added Message</h3>");
			pw.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
