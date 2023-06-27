package com.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        //on formatte l'heure et la date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        // On envoi l'heure actuelle ainsi que la date
        request.setAttribute("date", currentDate.format(formatter));
        formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        request.setAttribute("heure", currentTime.format(formatter));
        // Transmettez la requête à la page JSP pour l'affichage
        request.getRequestDispatcher("/accueil.jsp").forward(request, response);
    }
}