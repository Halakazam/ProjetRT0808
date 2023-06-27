<!DOCTYPE html>
<html>
<head>
    <title>Données météorologiques</title>
</head>
<body>
    <h1>Données météorologiques</h1>
    <p>Nous sommes le ${date} à ${heure}. Veuillez saisir une ville pour voir la météorologie actuelle :</p>
	<form action="/servlet/weather" method="post">
	    <input type="text" name="ville" placeholder="Saisir une ville" required><br>
	    <input type="submit" value="Envoyer">
	</form>
</body>
</html>