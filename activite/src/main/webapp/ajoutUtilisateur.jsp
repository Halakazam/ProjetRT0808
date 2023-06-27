<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Application de Tracking Sportif - Ajouter un utilisateur</title>
    <style>
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
</head>
<body>
    <h1>Ajouter un utilisateur</h1>

    <form action="/activite/creerUtilisateur" method="post">
        <label for="username">Nom d'utilisateur :</label>
        <input type="text" id="username" name="username" required>
        <br>
        <input type="button" value="Retour" onclick="window.location.href='/activite/accueil'" class="btn-bleu">
    	<input type="submit" value="Valider" class="btn-vert">
    </form>
</body>
</html>
