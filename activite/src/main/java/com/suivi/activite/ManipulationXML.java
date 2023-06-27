package com.suivi.activite;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FilenameFilter;


public class ManipulationXML {
	//static String fichierLocal="./src/main/resources/";
	//on est initialement dans le bin, donc il faut revenir en arrière avec ..
	static String fichierLocal="../webapps/activite/WEB-INF/classes/";
    private Document document;
    private Map<String, Element> activityMap;

    //on précise le nom, s'il n'existe pas, on le creer, sinon on charge en mémoire les documents XML disponibles
    public ManipulationXML(String nom) throws SAXException, IOException, TransformerException {
    	//on va creer les repertoires nécessaires ici s'ils n'existent pas déja
    	String cheminRepertoires=fichierLocal;
    	File rep = new File(cheminRepertoires);
    	rep.mkdir();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File file = new File(fichierLocal+nom+".xml");
            if (file.exists()) {
            	this.document = builder.parse(file);
            	// Récupérer la liste des balises <activity>
            	NodeList activityList = this.document.getElementsByTagName("activity");
            	// Parcourir la liste des balises <activity> et mettre à jour la map des activités
            	this.activityMap = new HashMap<>();
            	for (int i = 0; i < activityList.getLength(); i++) {
            		Element activityElement = (Element) activityList.item(i);
            		String activityId = activityElement.getAttribute("id");
            		this.activityMap.put(activityId, activityElement);
            	}
            } else {
            	this.document = builder.newDocument();
            	this.activityMap = new HashMap<>();
            	this.createUser(nom);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    //fonction pour supprimer un fichier XML présent
    public static void supprimerUtilisateur(String utilisateur) {
    	File file = new File(fichierLocal+utilisateur+".xml");
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Le fichier a été supprimé avec succès.");
            } else {
                System.out.println("Impossible de supprimer le fichier.");
            }
        } else {
            System.out.println("Le fichier n'existe pas.");
        }
    }
    //fonction pour récupérer tous les fichiers XML présent (et donc avec un utilisateur)
    public static List<String> afficherUtilisateur() {
    	List<String> utilisateurs=new ArrayList<String>();
    	File path = new File(fichierLocal);
    	// Vérifier si le répertoire existe
        if (path.exists() && path.isDirectory()) {
            // Créer un filtre pour les fichiers avec l'extension .xml
            FilenameFilter xmlFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String nomFichier) {
                    return nomFichier.toLowerCase().endsWith(".xml");
                }
            };
            
            // Obtenir la liste des fichiers du répertoire en utilisant le filtre
            File[] fichiers = path.listFiles(xmlFilter);     
            // Parcourir les fichiers
            if (fichiers != null) {
                for (File fichier : fichiers) {
                    // Obtenir le nom du fichier sans l'extension
                    String nomFichierSansExtension = fichier.getName().replaceFirst("[.][^.]+$", "");
                    utilisateurs.add(nomFichierSansExtension);
                }
            }
        } else {
            System.out.println("Le répertoire spécifié n'existe pas.");
        }
    	return utilisateurs;
    }
    //fonction pour créer le fichier XML associé à un utilisateur
    public void createUser(String nom) throws ParserConfigurationException, TransformerException {
    	// Création du document XML fait avec manipulationXML
        // Élément utilisateur
        Element utilisateurElement = this.document.createElement("utilisateur");
        // Attribut nom
        utilisateurElement.setAttribute("nom", nom);
        this.document.appendChild(utilisateurElement);
        
        // Ajout de l'instruction de schéma
        ProcessingInstruction schemaInstruction = this.document.createProcessingInstruction("xml-model", "href=\"Schema.xsd\" type=\"application/xml\" schematypens=\"http://www.w3.org/2001/XMLSchema\"");
        this.document.insertBefore(schemaInstruction, utilisateurElement);

        // Élément ID
        Element idElement = this.document.createElement("ID");
        idElement.setAttribute("id", "1");//1 à la création du document
        utilisateurElement.appendChild(idElement);

        // Balise <activites>
        Element activitesElement = document.createElement("activites");
        Text emptyText = document.createTextNode("");
        activitesElement.appendChild(emptyText);
        utilisateurElement.appendChild(activitesElement);

        //enregistrement
        String filePath= fichierLocal+nom+".xml";
        // Création du répertoire parent si nécessaire
        File file = new File(filePath);
        if (file.exists()) {
            System.out.println("Le fichier utilisateur existe déjà : " + nom);
            return;
        }
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        saveToFile(filePath,this.document);
    }
    //récupère l'utilisateur
  //récupérer l'ID en cours dans le fichier pour avoir une unicité de l'activité
    public String getCurrentUser() {
        NodeList idElements = this.document.getElementsByTagName("utilisateur");
        if (idElements.getLength() > 0) {
            Element idElement = (Element) idElements.item(0);
            return idElement.getAttribute("nom");
        }
        return null;
    }
    //récupérer l'ID en cours dans le fichier pour avoir une unicité de l'activité
    public String getCurrentID() {
        NodeList idElements = this.document.getElementsByTagName("ID");
        if (idElements.getLength() > 0) {
            Element idElement = (Element) idElements.item(0);
            return idElement.getAttribute("id");
        }
        return null;
    }
    //incrémente l'ID après avoir ajouté une activité
    public void incrementID() throws TransformerException {
        NodeList idElements = this.document.getElementsByTagName("ID");
        if (idElements.getLength() > 0) {
            Element idElement = (Element) idElements.item(0);
            String currentID = idElement.getAttribute("id");
            int nextID = Integer.parseInt(currentID) + 1;
            idElement.setAttribute("id", String.valueOf(nextID));
            saveToFile(fichierLocal+this.getCurrentUser()+".xml",this.document);
        }
    }
    //ajoute une activité qui s'est déjà déroulé
    public void addActivity(Activite activite) throws TransformerException {
    	int id;
    	if(this.getCurrentID()!=null)
    		id=Integer.parseInt(this.getCurrentID());//on va le récupérer dans le XML
    	else{
    		System.out.println("Impossible de récupérer l'ID en cours dans le fichier.");
    		return;
    	}
    	String utilisateur;
    	if(this.getCurrentUser()!=null)
    		utilisateur=this.getCurrentUser();//on va le récupérer dans le XML
    	else{
    		System.out.println("Impossible de récupérer l'utilisateur en cours dans le fichier.");
    		return;
    	}
    	LocalDate date=activite.getDate();
    	String sport=activite.getSport();
    	LocalTime heureDebut=activite.getHeureDebut();
    	LocalTime heureFin=activite.getHeureFin();
    	List<PositionsGPS> positionsGPS=activite.getPositionsGPS();
    	double vitesseMoyenne=activite.getVitesseMoyenne();
    	double distanceParcourue=activite.getDistanceParcourue();
    	// Récupération de la balise <activites>
        Element activitesElement = (Element) this.document.getElementsByTagName("activites").item(0);
        // Création de la balise <activity>
        Element activityElement = this.document.createElement("activity");
        activityElement.setAttribute("id", String.valueOf(id));
        activityElement.setAttribute("date", date.toString());
        
        activityMap.put(String.valueOf(id), activityElement);

        Element sportElement = this.document.createElement("sport");
        sportElement.setTextContent(sport);
        activityElement.appendChild(sportElement);

        Element heureDebutElement = this.document.createElement("heure_debut");
        heureDebutElement.setTextContent(heureDebut.toString());
        activityElement.appendChild(heureDebutElement);

        Element heureFinElement = this.document.createElement("heure_fin");
        heureFinElement.setTextContent(heureFin.toString());
        activityElement.appendChild(heureFinElement);

        Element positionsGPSElement = this.document.createElement("positions_gps");
        for (PositionsGPS positionGPS : positionsGPS) {
            Element gpsElement = this.document.createElement("gps");

            Element latElement = this.document.createElement("lat");
            latElement.setTextContent(String.valueOf(positionGPS.getLat()));
            gpsElement.appendChild(latElement);

            Element lngElement = this.document.createElement("lng");
            lngElement.setTextContent(String.valueOf(positionGPS.getLng()));
            gpsElement.appendChild(lngElement);

            Element heureElement = this.document.createElement("heure");
            heureElement.setTextContent(positionGPS.getHeure().toString());
            gpsElement.appendChild(heureElement);

            positionsGPSElement.appendChild(gpsElement);
        }
        activityElement.appendChild(positionsGPSElement);

        Element vitesseMoyenneElement = this.document.createElement("vitesse_moyenne");
        vitesseMoyenneElement.setTextContent(String.valueOf(vitesseMoyenne));
        activityElement.appendChild(vitesseMoyenneElement);
        
        Element distanceParcourueElement = this.document.createElement("distance_parcourue");
        distanceParcourueElement.setTextContent(String.valueOf(distanceParcourue));
        activityElement.appendChild(distanceParcourueElement);
        
        activitesElement.appendChild(activityElement);

        saveToFile(fichierLocal+utilisateur+".xml", this.document);
        this.incrementID();
    }

    public void removeActivity(String id) throws TransformerException {
        Element activityElement = this.activityMap.get(id);
        String utilisateur;
    	if(this.getCurrentUser()!=null)
    		utilisateur=this.getCurrentUser();//on va le récupérer dans le XML
    	else{
    		System.out.println("Impossible de récupérer l'utilisateur en cours dans le fichier.");
    		return;
    	}
        if (activityElement != null) {
            Node parent = activityElement.getParentNode();
            parent.removeChild(activityElement);
            this.activityMap.remove(id);
            saveToFile(fichierLocal+utilisateur+".xml", this.document);
        }else {
        	System.out.println("Pas d'activité portant l'id "+id);
        }
    }
    public Activite recupActivity(String id) throws TransformerException {
    	Activite act=null;
        Element activityElement = this.activityMap.get(id);
        if (activityElement != null) {
            String sport=activityElement.getElementsByTagName("sport").item(0).getTextContent();
            LocalDate date=LocalDate.parse(activityElement.getAttribute("date"));
            LocalTime heureDebut = LocalTime.parse(activityElement.getElementsByTagName("heure_debut").item(0).getTextContent());
            LocalTime heureFin = LocalTime.parse(activityElement.getElementsByTagName("heure_fin").item(0).getTextContent());
            List<PositionsGPS> positionsGPS = new ArrayList<>();
            NodeList posGPS = activityElement.getElementsByTagName("positions_gps");

            for (int i = 0; i < posGPS.getLength(); i++) {
                Element posGPSElement = (Element) posGPS.item(i);
                NodeList gpsList = posGPSElement.getElementsByTagName("gps");
                
                for (int j = 0; j < gpsList.getLength(); j++) {
                    Element gpsElement = (Element) gpsList.item(j);
                    
                    double latitude = Double.valueOf(gpsElement.getElementsByTagName("lat").item(0).getTextContent());
                    double longitude = Double.valueOf(gpsElement.getElementsByTagName("lng").item(0).getTextContent());
                    LocalTime heure = LocalTime.parse(gpsElement.getElementsByTagName("heure").item(0).getTextContent());                
                    // Création de l'objet PositionsGPS
                    PositionsGPS positionsGPSObj = new PositionsGPS(latitude, longitude, heure);              
                    // Ajout à la liste
                    positionsGPS.add(positionsGPSObj);
                }
            }
            act=new Activite(sport, date, heureDebut, heureFin, positionsGPS);
        }else {
        	System.out.println("Pas d'activité portant l'id "+id);
        }
        return act;
    }
    
    public HashMap<Integer,Activite> getActivity(){
    	//ce sera une HashMap<Integer,Activite> pour avoir l'ID de l'activité
    	HashMap<Integer,Activite> activites=new HashMap<>();
    	//on parcours le XML et on rempli les activites
    	// Récupération de la liste des éléments "activity"
        NodeList nodeList = this.document.getElementsByTagName("activity");
        // Parcours des éléments "activity"
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("activity")) {
                Element element = (Element) node;
                // Récupérer les valeurs des attributs de l'élément "activity"
                String sport = element.getElementsByTagName("sport").item(0).getTextContent();
                int id=Integer.valueOf(element.getAttribute("id"));
                LocalDate date = LocalDate.parse(element.getAttribute("date"));
                LocalTime heureDebut = LocalTime.parse(element.getElementsByTagName("heure_debut").item(0).getTextContent());
                LocalTime heureFin = LocalTime.parse(element.getElementsByTagName("heure_fin").item(0).getTextContent());

                // Récupérer les positions GPS
                List<PositionsGPS> positionsGPS = new ArrayList<PositionsGPS>();
                NodeList gpsNodes = element.getElementsByTagName("gps");
                for (int j = 0; j < gpsNodes.getLength(); j++) {
                    Element gpsElement = (Element) gpsNodes.item(j);
                    double lat = Double.parseDouble(gpsElement.getElementsByTagName("lat").item(0).getTextContent());
                    double lng = Double.parseDouble(gpsElement.getElementsByTagName("lng").item(0).getTextContent());
                    LocalTime heure = LocalTime.parse(gpsElement.getElementsByTagName("heure").item(0).getTextContent());

                    PositionsGPS positionGPS = new PositionsGPS(lat, lng, heure);
                    positionsGPS.add(positionGPS);
                }
                // Créer une instance d'Activite et l'ajouter à la liste
                Activite activite = new Activite(sport, date, heureDebut, heureFin, positionsGPS);
                activites.put(id,activite);
            }
        }
    	return (HashMap<Integer,Activite>) activites;
    }

    public void saveToFile(String filePath,Document document) throws TransformerException {
    	// Enregistrement du document dans le fichier
    	File file = new File(filePath);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 2); // Indentation de 2 espaces
        Transformer transformer = transformerFactory.newTransformer();
        //transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Optionnel : pour formater le fichier avec l'indentation
        DOMSource source = new DOMSource(this.document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);

        //System.out.println("Le fichier utilisateur a été créé ou modifié : " + filePath);
    }

  
}
