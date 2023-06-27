<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Application de Tracking Sportif</title>
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"> <!-- Pour la gestion de la date et de l'heure -->
  	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/clockpicker/0.0.7/bootstrap-clockpicker.min.css"><!-- Pour la gestion de l'heure -->
    <style>
		.clockpicker-popover {
		    position: absolute !important;
		    top: 0 !important;
		    left: 0 !important;
		    z-index: 9999 !important;
		}
        table {
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid black;
            padding: 8px;
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
</head>
<body>
    <h1>Informations de l'utilisateur ${utilisateur}</h1>
    <input type="button" value="Retour" onclick="window.location.href='/activite/accueil'" class="btn-bleu">
	<form method="POST" action="/activite/demarrerActivite">
		<input type="hidden" name="utilisateur" value="${utilisateur}">
		<label for="date">Date :</label>
		<input type="text" id="date" name="date" pattern="[0-9]{4}-[0-9]{2}-[0-9]{2}" autocomplete="off" required>
    	<label for="heure">Heure :</label>
    	<input type="text" id="heure" name="heure" autocomplete="off" required>
		<!-- <input type="hidden" name="date" id="date">
    	<input type="hidden" name="heure" id="heure"> -->
		<label for="sport">Sport :</label>
		<input type="text" id="sport" name="sport" required>
		<label for="duree">Durée, en minutes :</label>
		<input type="text" id="duree" name="duree" required>
		<select id="ville" name="ville">
	        <option value="Paris">Paris (48.8566, 2.3522)</option>
	        <option value="Marseille">Marseille (43.2964, 5.3690)</option>
	        <option value="Lyon">Lyon (45.75, 4.85)</option>
	        <option value="Reims">Reims (49.2583, 4.0317)</option>
	        <option value="Lille">Lille (50.6310, 3.0570)</option>
	        <option value="Grenoble">Grenoble (45.1885, 5.7245)</option>
	        <option value="Nantes">Nantes (47.2181, -1.5528)</option>
	        <option value="Strasbourg">Strasbourg (48.5734, 7.7521)</option>
	        <option value="Montpellier">Montpellier (43.6110, 3.8767)</option>
	        <option value="Nice">Nice (43.7102, 7.2620)</option>
	    </select>
        <button type="submit" class="btn-vert">Demarrer une activité</button>
    </form>
    <script>
	    // Fonction pour obtenir la date actuelle au format yyyy-mm-dd
	    function getCurrentDate() {
	        var today = new Date();
	        var year = today.getFullYear();
	        var month = ('0' + (today.getMonth() + 1)).slice(-2);
	        var day = ('0' + today.getDate()).slice(-2);
	        return year + '-' + month + '-' + day;
	    }
	    // Fonction pour obtenir l'heure actuelle au format hh:mm:ss
	    function getCurrentTime() {
	        var now = new Date();
	        var hours = ('0' + now.getHours()).slice(-2);
	        var minutes = ('0' + now.getMinutes()).slice(-2);
	        var seconds = ('0' + now.getSeconds()).slice(-2);
	        return hours + ':' + minutes + ':' + seconds;
	    }
	    // Remplir les champs cachés avec la date et l'heure actuelles lors du chargement de la page
	    //document.getElementById('date').value = getCurrentDate();
	    //document.getElementById('heure').value = getCurrentTime();
	    // Pré-remplir le champ d'heure avec l'heure actuelle et idem pour la date
    	//$("#heure").val(getCurrentTime());
    	//$("#date").val(getCurrentDate());
	</script>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script>
	    $(function() {
	      $("#date").datepicker({
	          dateFormat: 'yy-mm-dd'
	      });
	    });
	</script>
  	<script src="https://cdnjs.cloudflare.com/ajax/libs/clockpicker/0.0.7/bootstrap-clockpicker.min.js"></script>
  <script>
    $(function() {
      $('#heure').clockpicker({
        placement: 'bottom',
        align: 'left',
        autoclose: true,
        twelvehour: false,
        minutedStep: 1
      });
   	// Soumettre le formulaire
      $("form").on("submit", function(e) {
        // Ajouter les secondes au champ d'heure
        var selectedTime = $("#heure").val();
        var formattedTime = selectedTime + ':01'; // Ajouter les secondes : 01 car bloque sinon a 00
        $("#heure").val(formattedTime); // Mettre à jour la valeur du champ d'heure

        // Continuer l'envoi du formulaire normalement
        return true;
      });
    });
  </script>



    
    <h1>Liste des activités</h1>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Date</th>
                <th>Sport</th>
                <th>Heure de début</th>
                <th>Heure de fin</th>
                <th>Vitesse moyenne</th>
                <th>Distance parcourue</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <%@ page import="java.util.Map" %>
            <%@ page import="java.util.List" %>
            <%@ page import="java.util.ArrayList" %>
            <%@ page import="java.util.HashMap" %>
            <%@ page import="java.util.Map.Entry" %>
            
            <% 
                Map<Integer, List<String>> listeActivite = (Map<Integer, List<String>>) request.getAttribute("activite");
                if (listeActivite != null) {
                    for (Entry<Integer, List<String>> entry : listeActivite.entrySet()) {
                        int id = entry.getKey();
                        List<String> activite = entry.getValue();
                        String date = activite.get(0);
                        String sport = activite.get(1);
                        String heureDebut = activite.get(2);
                        String heureFin = activite.get(3);
                        String vitesseMoyenne = activite.get(4);
                        String distanceParcourue = activite.get(5);
            %>
            <tr>
                <td><%= id %></td>
                <td><%= date %></td>
                <td><%= sport %></td>
                <td><%= heureDebut %></td>
                <td><%= heureFin %></td>
                <td><%= vitesseMoyenne %>  km/h</td>
                <td><%= distanceParcourue %> km</td>
                <td>
                    <form method="POST" action="/activite/infoUtilisateur">
                        <input type="hidden" name="id" value="<%= id %>">
                        <input type="hidden" name="utilisateur" value="${utilisateur}">
                        <button type="submit" class="btn-rouge">Supprimer</button>
                    </form>
                    <form method="GET" action="/activite/infoUtilisateur/detailActivite">
                        <input type="hidden" name="id" value="<%= id %>">
                        <input type="hidden" name="utilisateur" value="${utilisateur}">
                        <button type="submit" class="btn-bleu">Afficher les détails</button>
                    </form>
                </td>
            </tr>
            <% 
                    }
                }
            %>
        </tbody>
    </table>

    
</body>
</html>