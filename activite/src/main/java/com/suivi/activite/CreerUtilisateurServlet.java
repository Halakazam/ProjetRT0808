package com.suivi.activite;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CreerUtilisateurServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String utilisateurAAdd = request.getParameter("username");
		try {
			ManipulationXML manip=new ManipulationXML(utilisateurAAdd);
		} catch (SAXException | IOException | TransformerException e) {
			e.printStackTrace();
		}
		response.sendRedirect("/activite/accueil");
    }
}
