package com.suivi.activite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.LocalTime;

public class DetailActiviteServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String utilisateur = request.getParameter("utilisateur");
		String id=request.getParameter("id");
    	request.setAttribute("utilisateur", utilisateur);
    	Map<LocalTime, List<Double>> listeCoordGPS = new HashMap<>();//forme <ID,[date,sport,heure_debut,heure_fin,vitesse_moyenne,distance_parcourue]>
    	try {
			ManipulationXML manip=new ManipulationXML(utilisateur);
			Activite activites=manip.recupActivity(id);
			LocalDate date=activites.getDate();
			String sport=activites.getSport();
			LocalTime heureDebut=activites.getHeureDebut();
			LocalTime heureFin=activites.getHeureFin();
			for (PositionsGPS pos : activites.getPositionsGPS()) {
				List<Double> latLng= new ArrayList<Double>();
				latLng.add(pos.getLat());
				latLng.add(pos.getLng());
				LocalTime heure=pos.getHeure();
				listeCoordGPS.put(heure, latLng);
			}
			request.setAttribute("heure_debut", heureDebut);
			request.setAttribute("heure_fin", heureFin);
			request.setAttribute("sport", sport);
			// Convertir les coordonnées GPS en format JSON
			request.setAttribute("listeCoordGPS", listeCoordGPS);
			request.setAttribute("date", date);
		} catch (SAXException | IOException | TransformerException e) {
			e.printStackTrace();
		}
        request.getRequestDispatcher("../detailActivite.jsp").forward(request, response);

    }

}
