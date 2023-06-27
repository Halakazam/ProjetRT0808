<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Application de Tracking Sportif</title>
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
</head>
<body>
    <h1>Liste des utilisateurs</h1>
    <%@ page import="java.util.Map" %>
    <%@ page import="java.util.HashMap" %>
    <%@ page import="java.util.Iterator" %>
    <%@ page import="java.util.Map.Entry" %>
    <% Map<String, Integer> utilisateurs = (HashMap<String, Integer>) request.getAttribute("utilisateurs"); %>
    <% if (utilisateurs != null) { %>
        <% for (Entry<String, Integer> entry : utilisateurs.entrySet()) { %>
            <div class="utilisateur">
                <div>Nom : <%= entry.getKey() %></div>
                <div>Nombre d'activités : <%= entry.getValue() %></div>
                <form action="/activite/infoUtilisateur" method="get">
                    <input type="hidden" name="utilisateur" value="<%= entry.getKey() %>">
                    <input class="btn-bleu" type="submit" value="Voir l'historique">
                </form>
                <form action="/activite/accueil" method="post">
                    <input type="hidden" name="utilisateur" value="<%= entry.getKey() %>">
                    <input class="btn-rouge" type="submit" value="Supprimer l'utilisateur">
                </form>
            </div>
        <% } %>
    <% } else { %>
        <div>Aucun utilisateur disponible.</div>
    <% } %>

    <!-- Bouton pour ajouter un utilisateur -->
    <form action="/activite/ajouterUtilisateur" method="post">
        <input class="btn-vert" type="submit" value="Ajouter utilisateur">
    </form>
</body>
</html>
