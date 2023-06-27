@echo off

set tomcatDir=C:\Program Files\Java\apache-tomcat-10.1.10
set activiteDir=C:\Users\grego\Bureau\CoursMaster\Master\RT805\TP_Projet\activite
set serverIP=localhost
set serverPort=8080

rem Vérifier si le serveur Tomcat est déjà en cours d'exécution
powershell -command "& {Test-NetConnection -ComputerName %serverIP% -Port %serverPort% -InformationLevel Quiet}" >nul
if %errorlevel% equ 0 (
    echo Le serveur Tomcat est déjà en cours d'exécution. Arrêt en cours.
	cd /d "%tomcatDir%\bin"
    call catalina.bat stop
) else (
    echo Le serveur Tomcat n'est pas démarré, il n'est pas nécessaire de le fermer.
)

rem Supprimer l'ancienne archive WAR et le dossier "activite" s'ils existent
if exist "%tomcatDir%\webapps\activite.war" (
    del /q "%tomcatDir%\webapps\activite.war"
)
if exist "%tomcatDir%\webapps\activite" (
    rmdir /s /q "%tomcatDir%\webapps\activite"
)
rem Se déplacer dans le dossier "activite"
cd /d "%activiteDir%"

rem Nettoyer et construire le projet Maven : méthode call pour bien executer la suite
call mvn clean package

rem Copier le fichier WAR généré dans le dossier "webapps" de Tomcat
copy "%activiteDir%\target\activite.war" "%tomcatDir%\webapps"

rem Redémarrer le serveur Tomcat
echo Démarrage du serveur Tomcat...
cd /d "%tomcatDir%\bin"
call catalina.bat start

echo Le script a terminé son exécution.
