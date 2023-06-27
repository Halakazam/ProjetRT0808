package com.suivi.activite;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import org.apache.commons.math3.util.FastMath;

public class PositionsGPS{
	//positions en DD, on considere un double et un chiffre
	private double lat;
	private double lng;
	private LocalTime heure;
	private static final double EARTH_RADIUS = 6371.01; // Rayon de la Terre en km
	
	public PositionsGPS(double lat,double lng,LocalTime heure) {
		this.setLat(lat);
		this.setLng(lng);
		this.setHeure(heure);
	}
	public String afficherPositionsGPS() {
		String pos="";
		pos="Latitude :"+this.getLat()+" Longitude :"+this.getLng()+" Heure :"+this.getHeure();
		return pos;
	}
	//utilitaire pour simuler un déplacement à partir d'une position de départ, d'une direction (bearing en deg) et d'une distance
	public static double[] getNewCoordinates(double latStart, double longStart, double bearing, double distance) {
        double latRad = FastMath.toRadians(latStart);
        double longRad = FastMath.toRadians(longStart);
        double bearingRad = FastMath.toRadians(bearing);
        double angularDistance = distance / EARTH_RADIUS;
        
        double newLatRad = FastMath.asin(FastMath.sin(latRad) * FastMath.cos(angularDistance) +
                FastMath.cos(latRad) * FastMath.sin(angularDistance) * FastMath.cos(bearingRad));
        double newLongRad = longRad + FastMath.atan2(FastMath.sin(bearingRad) * FastMath.sin(angularDistance) * FastMath.cos(latRad),
                FastMath.cos(angularDistance) - FastMath.sin(latRad) * FastMath.sin(newLatRad));
        
        return new double[]{FastMath.toDegrees(newLatRad), FastMath.toDegrees(newLongRad)};
    }
	public double getLat() {
		BigDecimal bd = new BigDecimal(this.lat);
    	bd = bd.setScale(6, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		BigDecimal bd = new BigDecimal(this.lng);
    	bd = bd.setScale(6, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public LocalTime getHeure() {
		return heure;
	}
	public void setHeure(LocalTime heure) {
		this.heure = heure;
	}
}