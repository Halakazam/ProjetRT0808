<!DOCTYPE html>
<html>
<head>
    <title>Données météorologiques</title>
</head>
<body>
    <h1>Données météorologiques de la ville de ${ville}</h1>
    <h2>Données issues de l'API https://api.wheaterapi.com</h2>
    <h2>${locationName}, ${region}, ${country}</h2>
    <p>Temps actuel :<img src="${conditionIcon}"> ${conditionText}</p>
    <p>${isDay}</p>
	<p>Latitude : ${latitude}, Longitude : ${longitude}</p>
	<p>Fuseau horaire de l'emplacement : ${timezoneId}</p>
	<p>Heure de la donnée : ${localtime}, mis à jour pour la dernière fois à ${lastUpdated}.</p>
	<p>La température extérieure est actuellement de ${tempC}°C soit ${tempF}°F.</p>
	<p>Le vent a une vitesse de ${windKph} km/h, dans la direction de ${windDegree}°${windDir}.</p>
	<p>La température ressentie est de ${feelslikeC}°C.</p>
	<h2>Données de pression atmosphériques :</h2>
	<p>La pression atmosphérique actuelle est de ${pressureMb} millibars.<p>
	<p>Les précipitations actuelles sont de ${precipMm}mm, et l'humidité relative est de ${humidity}%.</p>
	<p>La visibilité est de ${visKm} kilomètres, et la couverture nuageuse est ${cloud}%.</p>
	<p>L'indice uv est de ${uv} et des rafales de vents ont été relevé à ${gustKph} km/h.</p>
</body>
</html>
