<!DOCTYPE html>
<html>
<head>
    <title>Donn�es m�t�orologiques</title>
</head>
<body>
    <h1>Donn�es m�t�orologiques</h1>
    <p>Nous sommes le ${date} � ${heure}. Veuillez saisir une ville pour voir la m�t�orologie actuelle :</p>
	<form action="/servlet/weather" method="post">
	    <input type="text" name="ville" placeholder="Saisir une ville" required><br>
	    <input type="submit" value="Envoyer">
	</form>
</body>
</html>