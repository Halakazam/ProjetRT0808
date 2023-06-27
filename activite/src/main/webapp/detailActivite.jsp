<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Detail de l'activite</title>
    <style>
        .utilisateur {
            border: 1px solid #000;
            padding: 10px;
            margin-bottom: 10px;
        }
        .btn-vert {
            background-color: white;
            color: green;
            padding: 5px 10px;
            border: solid;
            cursor: pointer;
        }
        .btn-rouge {
            background-color: white;
            color: red;
            padding: 5px 10px;
            border: solid;
            cursor: pointer;
        }
        .btn-bleu {
            background-color: white;
            color: blue;
            padding: 5px 10px;
            border: solid;
            cursor: pointer;
        }
    </style>
    <!-- Feuille de styles CSS de Leaflet -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/leaflet@1.7.1/dist/leaflet.css" />
    <!-- Bibliothèque JavaScript de Leaflet -->
    <script src="https://cdn.jsdelivr.net/npm/leaflet@1.7.1/dist/leaflet.js"></script>
</head>
<body>
    <input type="button" value="Retour à l'accueil" onclick="window.location.href='/activite/accueil'" class="btn-bleu">
    <form action="/activite/infoUtilisateur" method="get">
        <input type="hidden" name="utilisateur" value="${utilisateur}">
        <input class="btn-bleu" type="submit" value="Retour à l'utilisateur">
    </form>
    <h1>Activite ${sport} de ${utilisateur} </h1>
    <h2>En date du ${date} de ${heure_debut} jusque ${heure_fin}</h2>
    <br/>
    <div id="map" style="height: 400px;"></div>
    <script>
		<%@ page import="java.util.HashMap" %>
		<%@ page import="java.util.List" %>
		<%@ page import="java.util.ArrayList" %>
		<%@ page import="java.time.LocalTime" %>
	    <%@ page import="java.util.Collections" %>
	    
		var listeCoordGPS = new Map();

		<%
		HashMap<LocalTime, List<Double>> coordGPS = (HashMap<LocalTime, List<Double>>) request.getAttribute("listeCoordGPS");
		List<LocalTime> times = new ArrayList<>(coordGPS.keySet());
		Collections.sort(times); // Trie les clés dans l'ordre croissant
		for (LocalTime time : times) {
		    List<Double> coordinates = coordGPS.get(time);
		    double latitude = coordinates.get(0);
		    double longitude = coordinates.get(1);
		%>

		listeCoordGPS.set('<%= time %>', [<%= latitude %>, <%= longitude %>]);
		
		<%
		}
		%>
	    // Créez une carte Leaflet avec une vue initiale sur le premier point
	    var initialCoords = listeCoordGPS.values().next().value;
	    var initialLat = initialCoords[0];
	    var initialLng = initialCoords[1];
	    var map = L.map('map').setView([initialLat, initialLng], 10);
	
	    // Ajoutez une couche de tuiles (cartographie de base)
	    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	        attribution: '© OpenStreetMap contributors'
	    }).addTo(map);
	    
	    var polylinePoints = [];//pour tracer des points
	    listeCoordGPS.forEach(function (coords, time, mapObj) {
	        var latitude = coords[0];
	        var longitude = coords[1];

	        polylinePoints.push([latitude, longitude]);

	        if (time === mapObj.keys().next().value) {
	            var coordinatesDiv = document.createElement("div");
	            coordinatesDiv.innerHTML = "<p>Heure de départ :" + time + " Latitude: " + latitude + " Longitude: " + longitude + "</p>";
	            document.body.appendChild(coordinatesDiv);

	            L.marker([latitude, longitude]).addTo(map);
	        }else if(time === [...mapObj.keys()].pop()){
	            var coordinatesDiv = document.createElement("div");
	            coordinatesDiv.innerHTML = "<p>Heure d'arrivée :" + time + " Latitude: " + latitude + " Longitude: " + longitude + "</p>";
	            document.body.appendChild(coordinatesDiv);

	            L.marker([latitude, longitude]).addTo(map);
	        }
	    });
	 	// Créez une polyline pour relier les points
	    L.polyline(polylinePoints, { color: 'blue' }).addTo(map);
	</script>
</body>
</html>

