package com.suivi.activite;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	List<String> utilisateurs = ManipulationXML.afficherUtilisateur();
    	Map<String, Integer> listeUtilisateurs = new HashMap<>();
        // Autres opérations pour récupérer les activités et autres informations
    	for (String utilisateur : utilisateurs) {
            ManipulationXML manip = null;
            try {
                manip = new ManipulationXML(utilisateur);
                String userName = manip.getCurrentUser();
                HashMap<Integer, Activite> activites = manip.getActivity();
                int nombreActivites = activites.size();
                listeUtilisateurs.put(userName, nombreActivites);
            } catch (SAXException | IOException | TransformerException e) {
                e.printStackTrace();
            }
        }
    	request.setAttribute("utilisateurs", listeUtilisateurs);
        request.getRequestDispatcher("accueil.jsp").forward(request, response);
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String utilisateurASup = request.getParameter("utilisateur");
        ManipulationXML.supprimerUtilisateur(utilisateurASup);
        List<String> utilisateurs = ManipulationXML.afficherUtilisateur();
    	Map<String, Integer> listeUtilisateurs = new HashMap<>();
        // Autres opérations pour récupérer les activités et autres informations
    	for (String utilisateur : utilisateurs) {
            ManipulationXML manip = null;
            try {
                manip = new ManipulationXML(utilisateur);
                String userName = manip.getCurrentUser();
                HashMap<Integer, Activite> activites = manip.getActivity();
                int nombreActivites = activites.size();
                listeUtilisateurs.put(userName, nombreActivites);
            } catch (SAXException | IOException | TransformerException e) {
                e.printStackTrace();
            }
        }
    	request.setAttribute("utilisateurs", listeUtilisateurs);
        request.getRequestDispatcher("accueil.jsp").forward(request, response);
	}

}
