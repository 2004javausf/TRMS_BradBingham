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
import com.revature.beans.RForm;
import com.revature.daoimpl.RFormDAOImpl;

/**
 * Servlet implementation class RFormServlet
 */
public class RFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("In doGet ReinbursementFormServlet");
		ObjectMapper mapper = new ObjectMapper();
		RFormDAOImpl rdi = new RFormDAOImpl();
		PrintWriter pw = response.getWriter();
		String rfJSON;
		try {
			rfJSON = mapper.writeValueAsString(rdi.getFormList());
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			pw.print(rfJSON);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pw.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("In doPost ReinbursementFormServlet");
		//get a json from  our ajax and save it to the db
				RForm rf = null;
				ObjectMapper mapper = new ObjectMapper();
				//convert JSON to java object
				rf = mapper.readValue(request.getInputStream(), RForm.class);
				System.out.println(rf);
				RFormDAOImpl rdi = new RFormDAOImpl();
				try {
					rdi.insertForm(rf);
					PrintWriter pw = response.getWriter();
					pw.write("<h3>Added Form</h3>");
					pw.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	}

}
