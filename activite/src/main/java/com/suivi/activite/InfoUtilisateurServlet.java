package com.suivi.activite;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class InfoUtilisateurServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String utilisateur = request.getParameter("utilisateur");
		String id = request.getParameter("id");
		try {
			ManipulationXML manip=new ManipulationXML(utilisateur);
			manip.removeActivity(id);
			HashMap<Integer, Activite> activites=manip.getActivity();
			Map<Integer, List<String>> listeActivite = new HashMap<>();//forme <ID,[date,sport,heure_debut,heure_fin,vitesse_moyenne,distance_parcourue]>
			for (Entry<Integer, Activite> entry : activites.entrySet()) {
				int iD= entry.getKey();
                Activite act=entry.getValue();
				List<String> liste=new ArrayList<>();
				liste.add(act.getDate().toString());
				liste.add(act.getSport());
				liste.add(act.getHeureDebut().toString());
				liste.add(act.getHeureFin().toString());
				liste.add(String.valueOf(act.getVitesseMoyenne()));
				liste.add(String.valueOf(act.getDistanceParcourue()));
				listeActivite.put(iD, liste);
			}
			request.setAttribute("activite", listeActivite);
		} catch (SAXException | IOException | TransformerException e) {
			e.printStackTrace();
		}
		request.setAttribute("utilisateur", utilisateur);
        request.getRequestDispatcher("./infoUtilisateur.jsp").forward(request, response);
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String utilisateur = request.getParameter("utilisateur");
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