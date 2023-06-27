package com.suivi.activite;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DemarrerActiviteServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String utilisateur = request.getParameter("utilisateur");
		String sport = request.getParameter("sport");
		int duree = Integer.valueOf(request.getParameter("duree"));
		String heureDebut = request.getParameter("heure");
		System.out.println(heureDebut);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime heure = LocalTime.parse(heureDebut, formatter);
		String dateDebut = request.getParameter("date");
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(dateDebut, formatter);
		//conversion en LocalDate et LocalTime
		
		String ville= request.getParameter("ville");
		double lat,lng;
		switch(ville) {
			case "Paris" : lat=48.8566; lng=2.3522;break;
			case "Marseille" : lat=43.2964; lng=5.3690;break;
			case "Lyon" : lat=45.75;lng= 4.85;break;
			case "Reims" : lat=49.2583;lng= 4.0317;break;
			case "Lille" :lat=50.6310;lng=3.0570;break;
			case "Grenoble":lat=45.1885;lng=5.7245;break;
			case "Nantes":lat=47.2181;lng=-1.5528;break;
			case "Strasbourg": lat=48.5734;lng=7.7521;break;
			case "Montpellier": lat=43.6110; lng=3.8767;break;
			case "Nice": lat=43.7102;lng=7.2620;break;
        	default:lat=0;lng=0;
		}
		try {
			ApplicationSportive appli=new ApplicationSportive(utilisateur);
			appli.simulerActivite(sport, lat, lng, date, heure, duree);
		} catch (SAXException | IOException | TransformerException e) {
			e.printStackTrace();
		}
		request.setAttribute("utilisateur", utilisateur);
		Map<Integer, List<String>> listeActivite = new HashMap<>();//forme <ID,[date,sport,heure_debut,heure_fin,vitesse_moyenne,distance_parcourue]>
    	try {
			ManipulationXML manip=new ManipulationXML(utilisateur);
			HashMap<Integer, Activite> activites=manip.getActivity();
			for (Entry<Integer, Activite> entry : activites.entrySet()) {
				int id= entry.getKey();
                Activite act=entry.getValue();
				List<String> liste=new ArrayList<>();
				liste.add(act.getDate().toString());
				liste.add(act.getSport());
				liste.add(act.getHeureDebut().toString());
				liste.add(act.getHeureFin().toString());
				liste.add(String.valueOf(act.getVitesseMoyenne()));
				liste.add(String.valueOf(act.getDistanceParcourue()));
				listeActivite.put(id, liste);
			}
			request.setAttribute("activite", listeActivite);
		} catch (SAXException | IOException | TransformerException e) {
			e.printStackTrace();
		}
        request.getRequestDispatcher("./infoUtilisateur.jsp").forward(request, response);
    }


}
