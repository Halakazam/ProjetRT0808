package programmeTV;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ProgrammeTV {
    public static void main(String[] args) {
    	try {
            // Récupérer le document HTML à partir de l'URL
            Document doc = Jsoup.connect("https://tv-programme.com/programme-tv-tnt/").get();
            // Sélectionner l'élément contenant les informations
            Element containerFluid = doc.selectFirst("body div.container-fluid");
            Element row = containerFluid.selectFirst("div.row");
            Element colLg9 = row.selectFirst("div.col-lg-9");

            // Récupérer les informations dans chaque ligne (tr) du tbody
            Elements trElements = colLg9.select("tbody tr");
            for (Element trElement : trElements) {
                // Récupérer le nom de la chaîne
                Element channelElement = trElement.selectFirst("td > a.stretched-link > img");
                String channel = channelElement.attr("alt");
                System.out.println("Chaîne : " + channel);

                // Récupérer l'heure de début
                Element startTimeElement = trElement.selectFirst("big");
                String startTime = startTimeElement.text();
                System.out.println("Heure de début : " + startTime);

                // Récupérer le titre de l'émission
                Element titleElement = trElement.selectFirst("h2 > a.stretched-link > strong");
                String title = titleElement.text();
                System.out.println("Titre de l'émission : " + title);

                System.out.println("-----");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}