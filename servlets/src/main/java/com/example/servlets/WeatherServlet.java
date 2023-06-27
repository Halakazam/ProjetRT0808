package com.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;
import java.util.regex.Pattern;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.*;
import java.io.*;


public class WeatherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String cleAPI = "eca4c1e3cf6344ee96f95721231906";
	private static String ville = "Reims";
	private static String lien ="https://api.weatherapi.com/v1/current.xml?key="+cleAPI+"&q="+ville+"&aqi=no";
	private static StringBuilder xmlData;
	private static String locationName="",region="",country="";
	private static double latitude=0,longitude=0;
	private static String timezoneId="";
	private static long localtimeEpoch=0;
	private static String localtime="",lastUpdatedEpoch="",lastUpdated="",tempC="",tempF="";
	private static String isDay="",conditionText="",conditionIcon="",conditionCode="";
	private static String windMph="",windKph="",windDegree="",windDir="";
	private static String pressureMb="",pressureIn="",precipMm="",precipIn="",humidity="";
	private static String cloud="",feelslikeC="",feelslikeF="";
	private static String visKm="",visMiles="";
	private static String uv="",gustMph="",gustKph="";
	//méthode pour enlever les accents
	public static String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
	// Méthode utilitaire pour obtenir le contenu texte d'un élément donné
	private static String getTextContent(Element element, String tagName) {
	    NodeList nodeList = element.getElementsByTagName(tagName);
	    if (nodeList.getLength() > 0) {
	        Node node = nodeList.item(0);
	        return node.getTextContent();
	    } else {
	        return ""; // Retourner une valeur par défaut si l'élément n'est pas trouvé
	    }
	}
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ville = request.getParameter("ville");//il faut enlever les accents
        if(ville.equals("")) {
        	ville="Reims";
        }
        request.setAttribute("ville", ville);
        ville = removeAccents(ville);
        lien="https://api.weatherapi.com/v1/current.xml?key="+cleAPI+"&q="+ville+"&aqi=no";
        // Récupérez les données météorologiques
        try {
        	URL url = new URL(lien);
        	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        	connection.setRequestMethod("GET");

        	int responseCode = connection.getResponseCode();
        	if (responseCode == HttpURLConnection.HTTP_OK) {
        		//la réponse est un XML
        		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        		String line;
        		xmlData = new StringBuilder();
        		while ((line = reader.readLine()) != null) {
        			xmlData.append(line);
        		}
        		reader.close();
        	} else {
        		System.out.println("Error: " + responseCode);
        	}
        } catch (IOException e) {
        	e.printStackTrace();
        }
        try {
            // Créer un parseur DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Créer un flux d'entrée à partir de la chaîne XML
            InputSource inputSource = new InputSource(new StringReader(xmlData.toString()));
            // Charger le document XML à partir du flux d'entrée
            Document document = builder.parse(inputSource);
            // Obtenir l'élément racine
            Element root = document.getDocumentElement();
            // Récupérer les champs du document XML
            locationName = getTextContent(root, "name");
            region = getTextContent(root, "region");
            country = getTextContent(root, "country");
            latitude = Double.parseDouble(getTextContent(root, "lat"));
            longitude = Double.parseDouble(getTextContent(root, "lon"));
            timezoneId = getTextContent(root, "tz_id");
            localtimeEpoch = Long.parseLong(getTextContent(root, "localtime_epoch"));
            localtime = getTextContent(root, "localtime");
            lastUpdatedEpoch = getTextContent(root, "last_updated_epoch");
            lastUpdated = getTextContent(root, "last_updated");
            tempC = getTextContent(root, "temp_c");
            tempF = getTextContent(root, "temp_f");
            isDay = getTextContent(root, "is_day");
            if(root.getElementsByTagName("condition").getLength()>0) {
            	Element conditionElement = (Element) root.getElementsByTagName("condition").item(0);
                conditionText = getTextContent(conditionElement, "text");
                conditionIcon = getTextContent(conditionElement, "icon");
                conditionCode = getTextContent(conditionElement, "code");
            }else {
            	conditionText="";
            	conditionIcon="";
            	conditionCode="";
            }  
            windMph = getTextContent(root, "wind_mph");
            windKph = getTextContent(root, "wind_kph");
            windDegree = getTextContent(root, "wind_degree");
            windDir = getTextContent(root, "wind_dir");
            pressureMb = getTextContent(root, "pressure_mb");
            pressureIn = getTextContent(root, "pressure_in");
            precipMm = getTextContent(root, "precip_mm");
            precipIn = getTextContent(root, "precip_in");
            humidity = getTextContent(root, "humidity");
            cloud = getTextContent(root, "cloud");
            feelslikeC = getTextContent(root, "feelslike_c");
            feelslikeF = getTextContent(root, "feelslike_f");
            visKm = getTextContent(root, "vis_km");
            visMiles = getTextContent(root, "vis_miles");
            uv = getTextContent(root, "uv");
            gustMph = getTextContent(root, "gust_mph");
            gustKph = getTextContent(root, "gust_kph");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Les attributs de demande avec les données météorologiques
        request.setAttribute("locationName", locationName);
        request.setAttribute("region", region);
        request.setAttribute("country", country);
        request.setAttribute("latitude", latitude);
        request.setAttribute("longitude", longitude);
        request.setAttribute("timezoneId", timezoneId);
        request.setAttribute("localtime", localtime);
        request.setAttribute("lastUpdated", lastUpdated);
        request.setAttribute("tempC", tempC);
        request.setAttribute("tempF", tempF);
        request.setAttribute("conditionText", conditionText);
        request.setAttribute("conditionIcon", conditionIcon);
        if(!isDay.equals("1")) {
        	request.setAttribute("isDay","Le soleil est couché.");
        }else {
        	request.setAttribute("isDay","Le soleil est levé.");
        }
        request.setAttribute("windKph", windKph);
        request.setAttribute("windDegree", windDegree);
        request.setAttribute("windDir", windDir);
        request.setAttribute("pressureMb", pressureMb);
        request.setAttribute("precipMm", precipMm);
        request.setAttribute("humidity", humidity);
        request.setAttribute("cloud", cloud);
        request.setAttribute("feelslikeC", feelslikeC);
        request.setAttribute("visKm", visKm);
        request.setAttribute("uv", uv);
        request.setAttribute("gustKph", gustKph);
        // Transmettez la requête à la page JSP pour l'affichage
        request.getRequestDispatcher("/weather.jsp").forward(request, response);
        // Traitez les données du formulaire selon vos besoins
        // Redirigez ou renvoyez une réponse à l'utilisateur
    }
}
