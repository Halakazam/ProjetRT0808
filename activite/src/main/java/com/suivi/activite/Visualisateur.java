package com.suivi.activite;

import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;

import java.io.*;
import java.util.HashMap;
import java.util.List;

//classe pour l'utilisation des XML : il va lire les fichiers XML
public class Visualisateur {
	private String utilisateur;
	private HashMap<Integer, Activite> activites;
	private ManipulationXML manipulationXML;
	public Visualisateur(String utilisateur) throws SAXException, IOException, TransformerException {
		this.setUtilisateur(utilisateur);
		this.setManipulationXML(new ManipulationXML(utilisateur));    
		this.setActivites(this.getManipulationXML().getActivity());
	}
	public String getUtilisateur() {
		return utilisateur;
	}
	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}
	public HashMap<Integer, Activite> getActivites() {
		return activites;
	}
	public void setActivites(HashMap<Integer, Activite> hashMap) {
		this.activites = hashMap;
	}
	public ManipulationXML getManipulationXML() {
		return manipulationXML;
	}
	public void setManipulationXML(ManipulationXML manipulationXML) {
		this.manipulationXML = manipulationXML;
	}
}