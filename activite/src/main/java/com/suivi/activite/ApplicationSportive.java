package com.suivi.activite;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
import java.util.Random;

public class ApplicationSportive{
	//est initialisé  au début au lancement de l'application
	private String utilisateur;
	private ManipulationXML manip;
	
	public ApplicationSportive(String utilisateur) throws SAXException, IOException, TransformerException {
		this.setUtilisateur(utilisateur);
		this.setManip(new ManipulationXML(utilisateur));//va soit créer le fichier XML, soit le charger en mémoire
	}
	public void ajouterActivite(Activite activite) throws TransformerException {
		this.getManip().addActivity(activite);
	}
	//une simule une activité pour une durée donnée
	public void simulerActivite(String sport,double lat,double lng,LocalDate date,LocalTime heure,long duree) throws TransformerException {
		Activite activite=Activite.demarrerActivite(sport, date, heure, lat, lng);
		//une fois l'activité démarré, on simule les déplacements
		double bearing,distance;
		LocalDateTime startDateTime = LocalDateTime.of(date, heure);
		LocalDateTime endDateTime = startDateTime.plusMinutes(duree);
		LocalDateTime endTime=LocalDateTime.of(date, heure);;
		double[] newCoord = {lat,lng};
		//on va faire 100 points de repère
		do {
			Random random = new Random();
	        bearing = random.nextDouble() * 180;// entre 0 et 180 deg
	        distance = 0.5+random.nextDouble()*0.5;//entre 0.5 et 1 km
			newCoord=PositionsGPS.getNewCoordinates(newCoord[0], newCoord[1], bearing, distance);
			int minutesToAdd = 3+random.nextInt(8); //next int entre 0 et 7 Nombre de minutes à ajouter entre 3 et 10 minutes du km
	        endTime = endTime.plusMinutes(minutesToAdd);
	        if(endTime.isBefore(endDateTime))
	        	activite.ajouterPointRepere(newCoord[0], newCoord[1], endTime.toLocalTime());
		}while((endTime.isBefore(endDateTime)));
		//on arrête l'activité
		activite.terminerActivite(newCoord[0], newCoord[1], endTime.toLocalTime());
		//on enregistre l'activité
		this.ajouterActivite(activite);
	}
	
	public String getUtilisateur() {
		return utilisateur;
	}
	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}
	public ManipulationXML getManip() {
		return manip;
	}
	public void setManip(ManipulationXML manip) {
		this.manip = manip;
	}
	
}