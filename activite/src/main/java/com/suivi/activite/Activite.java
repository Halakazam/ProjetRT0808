package com.suivi.activite;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Activite {
	private static final double R = 6371.0; // Rayon de la Terre en kilomètres
	private String sport;
	private LocalDate date;
	private LocalTime heureDebut;
	private LocalTime heureFin;
	private List<PositionsGPS> positionsGPS;
	private double vitesseMoyenne;
	private double distanceParcourue;
	
	//constructeur de base pour un début d'activité
	public Activite(String sport,LocalDate date,LocalTime heureDebut) {
		this.setSport(sport);
		this.setDate(date);
		this.setHeureDebut(heureDebut);
		this.setPositionsGPS(new ArrayList<PositionsGPS>());//on fera des adds à chaque fois
		//le reste n'existe pas encore
	}
    public Activite(String sport,LocalDate date,LocalTime heureDebut,LocalTime heureFin,
    		List<PositionsGPS> positionsGPS) {
    	this.setSport(sport);
    	this.setDate(date);
    	this.setHeureDebut(heureDebut);
    	this.setHeureFin(heureFin);
    	this.setPositionsGPS(positionsGPS);
    	//calcul de la vitesse moyenne à partir des positions GPS
    	this.vitesseMoyenne=this.calculerVitesseMoyenne();
    	this.distanceParcourue=this.calculerDistanceTotale();
    }
    public static Activite demarrerActivite(String sport,LocalDate date,LocalTime heureDebut,double lat,double lng) {
    	Activite activite=new Activite(sport,date,heureDebut);
    	PositionsGPS pos=new PositionsGPS(lat,lng,heureDebut);
    	activite.getPositionsGPS().add(pos);
    	return activite;
    }
    public void ajouterPointRepere(double lat,double lng,LocalTime heure) {
    	PositionsGPS pos=new PositionsGPS(lat,lng,heure);
    	this.getPositionsGPS().add(pos);
    }
    public void terminerActivite(double lat,double lng,LocalTime heureFin) {
    	this.ajouterPointRepere(lat, lng, heureFin);
    	this.setHeureFin(heureFin);
    	this.vitesseMoyenne=this.calculerVitesseMoyenne();
    	this.setDistanceParcourue(this.calculerDistanceTotale());
    }
    public String afficherActivite() {
    	String actString="";
    	actString="Activite :"+this.getSport()+" Le :"+this.getDate()+"\nHeure de début :"+this.getHeureDebut()+" Heure de fin :"+this.getHeureFin();
    	actString+="\nDistance parcourue :"+ this.getDistanceParcourue()+" km";
    	actString+="\nVitesse moyenne :"+this.getVitesseMoyenne()+"km/h\n";
    	/*for(int i=0;i<this.getPositionsGPS().size();i++) {
    		actString+=this.getPositionsGPS().get(i).afficherPositionsGPS()+"\n";
    	}*/
    	return actString;
    }
    //______UTILITAIRES______
    //calculer la distance entre deux points à partir des coordonnées lat et long
    private static double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
    	//Utilisation de la formule de la Haversine
    	double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        
        return distance;//en kilomètres
    }  
    private static double calculerVitesse(double lat1, double lon1, LocalTime time1,
            							double lat2, double lon2, LocalTime time2) {
    	double distance = calculerDistance(lat1, lon1, lat2, lon2);
    	Duration duration = Duration.between(time1, time2);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long secondes = duration.toSeconds() % 60 ;
    	double timeDiff =  hours+minutes/60.0+secondes/3600.0 ; //converti en heure
    	double speed = distance / timeDiff; // Calculer la vitesse en km/h
    	return speed;
    }
    private double calculerVitesseMoyenne() {
    	double vitesse=0;
    	double retour=0;
    	for(int i=0;i<this.getPositionsGPS().size()-1;i++) {
    		vitesse+=calculerVitesse(this.getPositionsGPS().get(i).getLat(), this.getPositionsGPS().get(i).getLng(), this.getPositionsGPS().get(i).getHeure(),
    				this.getPositionsGPS().get(i+1).getLat(), this.getPositionsGPS().get(i+1).getLng(), this.getPositionsGPS().get(i+1).getHeure());
    	}
    	if(this.getPositionsGPS().size()>2)
    		retour=vitesse/(this.getPositionsGPS().size()-2);
    	else
    		retour=calculerVitesse(this.getPositionsGPS().get(0).getLat(), this.getPositionsGPS().get(0).getLng(), this.getPositionsGPS().get(0).getHeure(),
    				this.getPositionsGPS().get(1).getLat(), this.getPositionsGPS().get(1).getLng(), this.getPositionsGPS().get(1).getHeure());
    	return retour;
    }
    public double calculerDistanceTotale() {
    	double distance=0;
    	for(int i=0;i<this.getPositionsGPS().size()-1;i++) {
    		distance+=calculerDistance(this.getPositionsGPS().get(i).getLat(), this.getPositionsGPS().get(i).getLng(),
    				this.getPositionsGPS().get(i+1).getLat(), this.getPositionsGPS().get(i+1).getLng());
    	}
    	return(distance);
    }
	public String getSport() {
		return sport;
	}
	public void setSport(String sport) {
		this.sport = sport;
	}
	public LocalDate getDate() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateFormat = LocalDate.parse(date.toString(), formatter);
		return dateFormat;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public LocalTime getHeureDebut() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    	LocalTime time = LocalTime.parse(heureDebut.toString(), formatter);
		return time;
	}
	public void setHeureDebut(LocalTime heureDebut) {
		this.heureDebut = heureDebut;
	}
	public LocalTime getHeureFin() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    	LocalTime time = LocalTime.parse(heureFin.toString(), formatter);
		return time;
	}
	public void setHeureFin(LocalTime heureFin) {
		this.heureFin = heureFin;
	}
	public List<PositionsGPS> getPositionsGPS() {
		return positionsGPS;
	}
	public void setPositionsGPS(List<PositionsGPS> positionsGPS) {
		this.positionsGPS = positionsGPS;
	}
	public double getVitesseMoyenne() {
		Double valeur=this.vitesseMoyenne;
		double retour=0;
		if(valeur!=Double.NaN && !Double.isInfinite(valeur)) {	
			BigDecimal bd = new BigDecimal(this.vitesseMoyenne);
	    	bd = bd.setScale(2, RoundingMode.HALF_UP);
	    	retour=bd.doubleValue();
		}
		return retour;
	}
	public void setVitesseMoyenne(double vitesseMoyenne) {
		this.vitesseMoyenne = vitesseMoyenne;
	}
	public double getDistanceParcourue() {
		String valeur=String.valueOf(distanceParcourue);
		double retour=0;
		if(!valeur.equals("Infinite") && !valeur.equals("NaN")) {	
			BigDecimal bd = new BigDecimal(distanceParcourue);
	    	bd = bd.setScale(2, RoundingMode.HALF_UP);
	    	retour=bd.doubleValue();
		}
		return retour;
	}
	public void setDistanceParcourue(double distanceParcourue) {
		this.distanceParcourue = distanceParcourue;
	}
}