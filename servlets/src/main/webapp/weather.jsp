<!DOCTYPE html>
<html>
<head>
    <title>Donn�es m�t�orologiques</title>
</head>
<body>
    <h1>Donn�es m�t�orologiques de la ville de ${ville}</h1>
    <h2>Donn�es issues de l'API https://api.wheaterapi.com</h2>
    <h2>${locationName}, ${region}, ${country}</h2>
    <p>Temps actuel :<img src="${conditionIcon}"> ${conditionText}</p>
    <p>${isDay}</p>
	<p>Latitude : ${latitude}, Longitude : ${longitude}</p>
	<p>Fuseau horaire de l'emplacement : ${timezoneId}</p>
	<p>Heure de la donn�e : ${localtime}, mis � jour pour la derni�re fois � ${lastUpdated}.</p>
	<p>La temp�rature ext�rieure est actuellement de ${tempC}�C soit ${tempF}�F.</p>
	<p>Le vent a une vitesse de ${windKph} km/h, dans la direction de ${windDegree}�${windDir}.</p>
	<p>La temp�rature ressentie est de ${feelslikeC}�C.</p>
	<h2>Donn�es de pression atmosph�riques :</h2>
	<p>La pression atmosph�rique actuelle est de ${pressureMb} millibars.<p>
	<p>Les pr�cipitations actuelles sont de ${precipMm}mm, et l'humidit� relative est de ${humidity}%.</p>
	<p>La visibilit� est de ${visKm} kilom�tres, et la couverture nuageuse est ${cloud}%.</p>
	<p>L'indice uv est de ${uv} et des rafales de vents ont �t� relev� � ${gustKph} km/h.</p>
</body>
</html>
